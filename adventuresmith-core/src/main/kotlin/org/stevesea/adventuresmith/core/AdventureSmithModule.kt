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
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import com.google.common.collect.Table
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

    val BehindTheTables = "behind_the_tables"
    val MazeRats = "maze_rats"
    val Kaigaku = "kaigaku"
    val PerilousWilds = "perilous_wilds"
    val HackSlash = "hack_and_slash"
    val AugmentedReality = "augmented_reality"
    val RollXX = "roll_xx"
    val SSandSS = "ss_and_ss"

    override val kodein: Kodein by Kodein.lazy {
        import(adventureSmithModule)
    }

    val shuffler : Shuffler by lazy {
        kodein.instance<Shuffler>()
    }

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

    data class GroupKey(val coll: String, val grpId: String? = null)

    val groupedGenerators: Map<GroupKey, List<Generator>> by lazy {
        val groupedGens : MutableMap<GroupKey, MutableList<Generator>> = mutableMapOf()
        generators.forEach {
            // can't cache metadata, since the locale might change at runtime. But, we can crack open the
            // default locale's metadata to get collection/group IDs
            val genMeta = it.value.getMetadata()
            val grpKey = GroupKey(genMeta.collectionId, genMeta.groupId)
            val genList : MutableList<Generator> = groupedGens.getOrPut(grpKey,
                    {
                        mutableListOf()
                    }
            )
            genList.add(it.value)
        }
        groupedGens
    }

    fun getGeneratorsByIds(locale: Locale, genIds: Set<String>) : Map<GeneratorMetaDto, Generator> {
        val result : MutableMap<GeneratorMetaDto, Generator> = mutableMapOf()
        genIds.forEach {
            val gen = generators.get(it)
            if (gen != null) {
                val genMeta = gen.getMetadata(locale)
                result.put(genMeta,gen)
            }
        }
        return result.toSortedMap()
    }


    fun getGeneratorsByGroup(locale: Locale, collId: String, grpId: String? = null) : Map<GeneratorMetaDto, Generator> {
        val result : MutableMap<GeneratorMetaDto, Generator> = mutableMapOf()

        val grpKey = GroupKey(collId, grpId)
        groupedGenerators.getOrElse(grpKey, {mutableListOf()}).forEach {
            try {
                val genMeta = it.getMetadata(locale)
                if (collId == genMeta.collectionId &&
                        grpId.orEmpty() == genMeta.groupId.orEmpty()) {
                    result.put(genMeta, it)
                }
            } catch (e : Exception) {
                logger.warn("problem loading generator", e)
            }
        }
        return result.toSortedMap()
    }


    // we have two lazy-init maps (which don't actually hold generator metadata/data)
    //   a map of genid -> generator
    //   and a map of collId/grpId -> list<generator>
    // calling this during app startup ensures those maps have been initialized.
    // otherwise, there was noticeable lag the first time we try to traverse the generators.
    // NOTE: we don't cache metadata, since locale might change during runtime
    fun initCaches() {
        for (g in groupedGenerators) {
            logger.debug { g.key }
        }
    }

    fun getCollectionMetaData(collectionId: String, locale: Locale) : CollectionMetaDto {
        val collMetaLoader = kodein.instance<CollectionMetaLoader>()
        return collMetaLoader.load(collectionId, locale)
    }

    fun getCollections(locale: Locale): Map<String, CollectionMetaDto> {
        val result: MutableMap<String, CollectionMetaDto> = mutableMapOf()

        val collMetaLoader = kodein.instance<CollectionMetaLoader>()

        for (gen in generators) {
            try {
                val genMeta = gen.value.getMetadata(locale)
                if (result.containsKey(genMeta.collectionId)) {
                    continue
                }

                result.put(genMeta.collectionId, collMetaLoader.load(genMeta.collectionId, locale))
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

    val generatorsFile = "core_generators.yml"
    try {
        val generatorsUrl = Resources.getResource(AdventuresmithCore.javaClass, generatorsFile)
        val generatorsStr = Resources.toString(generatorsUrl, Charsets.UTF_8)
        val generatorsListDto: GeneratorListDto = objectMapper.reader().forType(GeneratorListDto::class.java).readValue(generatorsStr)
        val genList : MutableList<String> = mutableListOf()

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
        throw IOException("problem reading $generatorsFile - ${ex.message}")
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
