/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.core

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import com.github.salomonbrys.kodein.*
import com.google.common.io.*
import mu.KLoggable
import org.stevesea.adventuresmith.core.dice_roller.DiceConstants
import org.stevesea.adventuresmith.core.dice_roller.diceModule
import org.stevesea.adventuresmith.core.freebooters_on_the_frontier.*
import org.stevesea.adventuresmith.core.stars_without_number.*
import java.io.*
import java.nio.charset.Charset
import java.security.*
import java.util.*


object AdventuresmithCore : KodeinAware, KLoggable {
    override val logger = logger()

    // all generators read from collection definitions
    val ALL_GENERATORS = "all-generators"

    // TODO: every time add a translation, add its locale here.
    val ALL_LOCALES = listOf(Locale.FRANCE, Locale.US, Locale("es"))

    // generators which shouldn't have classpath-loading-generators created for them
    val NON_RESOURCE_GENERATORS : Set<String> by lazy {
        val result : MutableSet<String> = mutableSetOf()
        result.addAll(FotfConstants.generators)
        result.addAll(SwnConstantsCustom.generators)
        result.addAll(DiceConstants.generators)
        result
    }

    override val kodein: Kodein by Kodein.lazy {
        import(adventureSmithModule)
    }

    val collections : Map<String, CollectionDto> by lazy {
        instance<Map<String, CollectionDto>>()
    }

    val objectMapper = ObjectMapper(YAMLFactory())
            .registerKotlinModule()

    val collMetaLoader : CollectionMetaLoader by lazy {
        instance<CollectionMetaLoader>()
    }
    /**
     * a map of generator-id to generator instance
     */
    val generators : Map<String,Generator> by lazy {
        val generators :  MutableMap<String, Generator> = mutableMapOf()

        for (g in instance<Set<String>>(ALL_GENERATORS)) {
            try {
                generators.put(g, instance<Generator>(g))
            } catch (e : Exception) {
                logger.warn("problem loading generator", e)
            }
        }
        generators
    }

    /**
     * retrieve a map of meta+generators . typically used for gathering favorites
     */
    fun getGeneratorsByIds(genIds: Set<String>) : List<Generator> {
        val result : MutableList<Generator> = mutableListOf()
        genIds.forEach {
            val gen = generators[it]
            if (gen != null) {
                result.add(gen)
            }
        }
        return result
    }


    fun getGeneratorsByGroup(collId: String, grpId: String? = null) : List<Generator> {
        val result : MutableList<Generator> = mutableListOf()

        val collDto = collections[collId]
        if (collDto == null) {
            throw IOException("Unknown collection: $collId")
        }

        val genIds = collDto.getGenerators(grpId)
        if (genIds.isEmpty()) {
            logger.warn("Unable to find coll/grp: $collId/$grpId")
            return listOf()
        }

        genIds.forEach {
            try {
                val gen = generators[it]
                if (gen != null) {
                    result.add(gen)
                }
            } catch (e : Exception) {
                logger.warn("problem loading generator", e)
            }
        }
        return result
    }

    // retrieve metadata for a specific collection
    fun getCollectionMetaData(collectionId: String, locale: Locale) : CollectionMetaDto {
        return collMetaLoader.load(collectionId, locale)
    }

    // retrieve all collection metadata
    fun getCollectionMetas(locale: Locale): Map<String, CollectionMetaDto> {
        val result: MutableMap<String, CollectionMetaDto> = linkedMapOf()
        for (collectionId in collections.keys) {
            try {
                result.put(collectionId, getCollectionMetaData(collectionId, locale))
            } catch (e: Exception) {
                logger.warn("problem loading collection metadata", e)
            }
        }
        return result
    }

    // create generator for an input file
    fun getGenerator(input: File) : Generator {
        val genFactory: (File) -> DataDrivenGeneratorForFiles = kodein.factory()
        return genFactory.invoke(input)
    }

    fun <T> loadCollectionResource(clazz: Class<T>, path: String, charset: Charset = Charsets.UTF_8) : T {
        val url = Resources.getResource(AdventuresmithCore.javaClass, path)
        val str = Resources.toString(url, charset)
        try {
            val result: T = objectMapper.reader().forType(clazz).readValue(str)
            return result
        } catch (ex: Exception) {
            throw IOException("Problem reading file: $url - ${ex.message}", ex)
        }
    }

}

val generatorModule = Kodein.Module {

    bind() from instance ( AdventuresmithCore.objectMapper )
    bind() from provider {
        val mapper: ObjectMapper = instance()
        mapper.reader()
    }
    bind() from provider {
        val mapper: ObjectMapper = instance()
        mapper.writer()
    }

    bind() from singleton {
        CachingResourceDeserializer(kodein)
    }

    bind<Random>() with singleton { SecureRandom() }
    bind<Shuffler>() with singleton { Shuffler(kodein) }

    bind<DataDrivenGenDtoCachingResourceLoader>() with factory { resource_prefix: String ->
        DataDrivenGenDtoCachingResourceLoader(resource_prefix, kodein)
    }
    bind<DataDrivenGenDtoFileDeserializer>() with factory { input: File ->
        DataDrivenGenDtoFileDeserializer(input, kodein)
    }
    bind<DataDrivenGeneratorForFiles>() with factory { input: File ->
        DataDrivenGeneratorForFiles(input, kodein)
    }
    bind<DataDrivenDtoTemplateProcessor>() with provider {
        DataDrivenDtoTemplateProcessor(kodein)
    }
    bind<DtoMerger>() with provider {
        DtoMerger(kodein)
    }

    bind<CollectionMetaLoader>() with singleton {
        CollectionMetaLoader(kodein)
    }

    val collectionsFile = "collections.yml"
    try {
        val collMap: MutableMap<String, CollectionDto> = mutableMapOf()
        val genList: MutableSet<String> = mutableSetOf()

        val collListDto = AdventuresmithCore.loadCollectionResource(CollectionListDto::class.java, collectionsFile)

        collListDto.collections.forEach { collId ->

            val collFile = "$collId/collection.yml"
            try {
                val collDto = AdventuresmithCore.loadCollectionResource(CollectionDto::class.java, collFile)
                collMap.put(collId, collDto)

                collDto.getAllGeneratorIds().forEach { genId ->
                    val genStr = "$collId/$genId"
                    genList.add(genStr)
                    if (!AdventuresmithCore.NON_RESOURCE_GENERATORS.contains(genStr)) {
                        bind<Generator>(genStr) with provider {
                            DataDrivenGeneratorForResources(genStr, kodein)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw IOException("Problem reading $collFile - ${ex.message}")
            }
        }
        bind<Set<String>>(AdventuresmithCore.ALL_GENERATORS) with instance(genList)
        bind<Map<String, CollectionDto>>() with instance(collMap)
    } catch (ex: IOException) {
        throw ex
    } catch (ex: Exception) {
        throw IOException("problem reading $collectionsFile - ${ex.message}")
    }
}

val adventureSmithModule = Kodein.Module {
    import(generatorModule)

    import(diceModule)
    import(fotfModule)
    import(swnModule)

    val non_resource_genIds : MutableSet<String> = mutableSetOf()
    non_resource_genIds.addAll(FotfConstants.generators)
    non_resource_genIds.addAll(SwnConstantsCustom.generators)
}
