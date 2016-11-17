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
import org.stevesea.adventuresmith.core.freebooters_on_the_frontier.*
import org.stevesea.adventuresmith.core.stars_without_number.*
import java.io.*
import java.security.*
import java.util.*
import javax.validation.*


object AdventuresmithCore : KodeinAware {
    // 'all' the generators in core
    val GENERATORS = "core-generators"
    // the tag for the resource-generators held in core
    val RESOURCE_GENERATORS = "resource_generators"

    override val kodein: Kodein by Kodein.lazy {
        import(adventureSmithModule)
    }

    val generatorNames by lazy {
        kodein.instance<Set<String>>(GENERATORS)
    }

    val generators by lazy {
        val generators :  MutableMap<String, Generator> = mutableMapOf()

        for (g in generatorNames) {
            generators.put(g, instance<Generator>(g))
        }
        generators
    }

    fun getCollections(locale: Locale): Set<CollectionMetaDto> {
        val result: MutableSet<CollectionMetaDto> = mutableSetOf()

        val collMetaLoader = kodein.instance<CollectionMetaLoader>()

        for (gen in generators) {
            val genMeta = gen.value.getMetadata(locale)
            result.add(collMetaLoader.load(genMeta.collectionId, locale))
        }

        return result
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

    bind() from singleton {
        Validation.buildDefaultValidatorFactory()
    }
    bind() from provider {
        val valFactory: ValidatorFactory = instance()
        valFactory.validator
    }
    bind<Random>() with singleton { SecureRandom() }
    bind<Shuffler>() with singleton { Shuffler(kodein) }

    bind<DataDrivenGenDtoCachingResourceLoader>() with factory { resource_prefix: String ->
        DataDrivenGenDtoCachingResourceLoader(resource_prefix, kodein)
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
                    DataDrivenGenerator(genStr, kodein)
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
