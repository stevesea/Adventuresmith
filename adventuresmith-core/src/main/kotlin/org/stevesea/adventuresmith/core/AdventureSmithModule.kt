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
import org.stevesea.adventuresmith.core.freebooters_on_the_frontier.*
import org.stevesea.adventuresmith.core.stars_without_number.*
import java.io.*
import java.security.*
import java.util.*


object AdventuresmithCore : KodeinAware, KLoggable {
    override val logger = logger()
    // 'all' the generators in core
    val GENERATORS = "core-generators"
    // the tag for the resource-generators held in core
    val RESOURCE_GENERATORS = "resource_generators"

    override val kodein: Kodein by Kodein.lazy {
        import(adventureSmithModule)
    }

    /**
     * a map of generator-id to generator instance
     */
    val generators : Map<String,Generator> by lazy {
        val generators :  MutableMap<String, Generator> = mutableMapOf()

        for (g in instance<Set<String>>(GENERATORS)) {
            try {
                generators.put(g, instance<Generator>(g))
            } catch (e : Exception) {
                logger.warn("problem loading generator", e)
            }
        }
        generators
    }

    fun getGeneratorsByIds(locale: Locale, genIds: Set<String>) : Map<GeneratorMetaDto, Generator> {
        val result : MutableMap<GeneratorMetaDto, Generator> = mutableMapOf()
        genIds.forEach {
            val gen = generators[it]
            if (gen != null) {
                val genMeta = gen.getMetadata(locale)
                result.put(genMeta,gen)
            }
        }
        return result
    }


    fun getGeneratorsByGroup(locale: Locale, collId: String, grpId: String? = null) : List<Generator> {
        val result : MutableList<Generator> = mutableListOf()

        val collMeta = getCollectionMetaData(collId, locale)

        val genIds = collMeta.getGenerators(grpId)
        if (genIds.isEmpty()) {
            logger.warn("Unable to find coll/grp: $collId/$grpId (locale: $locale)")
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

    fun getCollectionMetaData(collectionId: String, locale: Locale) : CollectionMetaDto {
        val collMetaLoader = kodein.instance<CollectionMetaLoader>()
        return collMetaLoader.load(collectionId, locale)
    }

    fun getCollections(locale: Locale): Map<String, CollectionMetaDto> {
        val result: MutableMap<String, CollectionMetaDto> = mutableMapOf()

        val collMetaLoader = kodein.instance<CollectionMetaLoader>()

        for (collectionId in kodein.instance<CollectionListDto>().collections) {
            try {
                result.put(collectionId, collMetaLoader.load(collectionId, locale))
            } catch (e: Exception) {
                logger.warn("problem loading collection metadata", e)
            }
        }
        return result
    }

    fun getGenerator(input: File) : Generator {
        val genFactory: (File) -> DataDrivenGeneratorForFiles = kodein.factory()
        return genFactory.invoke(input)
    }
}

val generatorModule = Kodein.Module {

    val objectMapper = ObjectMapper(YAMLFactory())
            .registerKotlinModule()

    bind() from instance ( objectMapper )
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
        val collectionsUrl = Resources.getResource(AdventuresmithCore.javaClass, collectionsFile)
        val collectionsStr = Resources.toString(collectionsUrl, Charsets.UTF_8)
        val collListDto: CollectionListDto = objectMapper.reader().forType(CollectionListDto::class.java).readValue(collectionsStr)

        val genList : MutableList<String> = mutableListOf()

        bind<CollectionListDto>() with instance(collListDto)

        for (generatorPkg in generatorsListDto.generators) {
            for (id in generatorPkg.value) {
                val genStr = "${generatorPkg.key}/${id}"

                genList.add(genStr)

                bind<Generator>(genStr) with provider {
                    DataDrivenGeneratorForResources(genStr, kodein)
                }
            }
        }
        bind<List<String>>(AdventuresmithCore.RESOURCE_GENERATORS) with instance(genList)

    } catch (ex: Exception) {
        throw IOException("problem reading $collectionsFile - ${ex.message}")
    }
}

val adventureSmithModule = Kodein.Module {
    import(generatorModule)

    import(diceModule)
    import(fotfModule)
    import(swnModule)

    bind<Set<String>>(AdventuresmithCore.GENERATORS) with provider {
        val res : MutableSet<String> = TreeSet<String>()
        res.addAll(instance<List<String>>(FotfConstants.GROUP))
        res.addAll(instance<List<String>>(SwnConstantsCustom.GROUP))
        res.addAll(instance<List<String>>(DiceConstants.GROUP))
        res.addAll(instance<List<String>>(AdventuresmithCore.RESOURCE_GENERATORS))
        res
    }
}
