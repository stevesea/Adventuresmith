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

package org.stevesea.adventuresmith.core.stars_without_number

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*
import java.util.*


data class WorldTag(val flavor: String,
                       val enemies: List<String>,
                       val friends: List<String>,
                       val complications: List<String>,
                       val things: List<String>,
                       val places: List<String>)

data class WorldTagsDto(val world_tags: Map<String, WorldTag>){
    companion object Resource {
        val resource_prefix = "world_tags"
    }
}

data class WorldConfigHeaders(val main: String,
                              val physical: String,
                              val atmosphere: String,
                              val temperature: String,
                              val biosphere: String,
                              val cultural: String,
                              val name: String,
                              val cultures: String,
                              val population: String,
                              val techlevel: String,
                              val worldtags: String,
                              val enemies: String,
                              val friends: String,
                              val complications: String,
                              val things: String,
                              val places: String)
data class WorldConfig(val headers: WorldConfigHeaders)
data class WorldDto(val config: WorldConfig,
                    val atmospheres: RangeMap,
                    val temperatures: RangeMap,
                    val biospheres: RangeMap,
                    val populations: RangeMap,
                    val techlevels: RangeMap){

    companion object Resource {
        val resource_prefix = "world"
    }
}

data class WorldRuleSub(val biosphere: List<Int>?,
                        val temperature: List<Int>?,
                        val population: List<Int>?)
data class WorldRule(val atmosphere: Map<Int, WorldRuleSub>,
                     val temperature: Map<Int, WorldRuleSub>,
                     val biosphere: Map<Int, WorldRuleSub>)
data class WorldRulesDto(val num_cultures: RangeMap,
                         val num_tags: String,
                         val num_tag_flavor: String,
                         val rules: WorldRule){
    companion object Resource {
        val resource_prefix = "world_rules"
    }
}

data class WorldBundleDto(val world: WorldDto,
                          val worldTags: WorldTagsDto,
                          val worldRules: WorldRulesDto,
                          val names: DataDrivenGenDto)

class SwnDtoLoader(override val kodein: Kodein): DtoLoadingStrategy<WorldBundleDto>, KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()
    override fun load(locale: Locale): WorldBundleDto {
        return WorldBundleDto(
                world = resourceDeserializer.deserialize(
                        WorldDto::class.java,
                        WorldDto.resource_prefix,
                        locale
                ),
                worldTags = resourceDeserializer.deserialize(
                        WorldTagsDto::class.java,
                        WorldTagsDto.resource_prefix,
                        locale
                ),
                worldRules = resourceDeserializer.deserialize(
                        WorldRulesDto::class.java,
                        WorldRulesDto.resource_prefix,
                        locale
                ),
                names = resourceDeserializer.deserialize(
                        DataDrivenGenDto::class.java,
                        SwnConstantsCustom.NAMES,
                        locale
                )
        )
    }
}

data class SwnWorldTagModel(val tags_and_flavor: Collection<Pair<String, String>>,
                            val enemies: Collection<String>,
                            val friends: Collection<String>,
                            val complications: Collection<String>,
                            val things: Collection<String>,
                            val places: Collection<String>)
data class SwnWorldModel(val config: WorldConfig,
                         val atmosphere: String,
                         val temperature: String,
                         val biosphere: String,
                         val population: String,
                         val techlevel: String,
                         val cultures: List<String>,
                         val name: String,
                         val worldTags: SwnWorldTagModel
                         )
class SwnWorldModelGenerator(override val kodein: Kodein) :
        ModelGeneratorStrategy<WorldBundleDto, SwnWorldModel>,
        KodeinAware {
    val shuffler: Shuffler = instance()
    override fun transform(dto: WorldBundleDto): SwnWorldModel {
        val numCultures = shuffler.pick(dto.worldRules.num_cultures).toInt()
        val cultures = shuffler.pickN(dto.names.nested_tables!!.keys, numCultures)
        val primaryCulture = cultures.elementAt(0)
        val worldName = shuffler.pick(dto.names.nested_tables[primaryCulture]?.get("place"))

        val atmoRoll = shuffler.roll("2d6")
        var tempRoll = shuffler.roll("2d6")
        var bioRoll = shuffler.roll("2d6")
        var popRoll = shuffler.roll("2d6")
        var techRoll = shuffler.roll("2d6")

        if (dto.worldRules.rules.atmosphere.containsKey(atmoRoll)) {
            val rule = dto.worldRules.rules.atmosphere.get(atmoRoll)
            rule?.biosphere?.let {
                bioRoll = shuffler.pick(rule.biosphere)
            }
            rule?.temperature?.let {
                tempRoll = shuffler.pick(rule.temperature)
            }
        }
        if (dto.worldRules.rules.temperature.containsKey(tempRoll)) {
            val rule = dto.worldRules.rules.temperature.get(tempRoll)
            rule?.biosphere?.let {
                bioRoll = shuffler.pick(rule.biosphere)
            }
            rule?.population?.let {
                popRoll = shuffler.pick(rule.population)
            }
        }
        if (dto.worldRules.rules.biosphere.containsKey(bioRoll)) {
            val rule = dto.worldRules.rules.biosphere.get(bioRoll)
            rule?.population?.let {
                popRoll = shuffler.pick(rule.population)
            }
        }

        return SwnWorldModel(
                config = dto.world.config,
                cultures = cultures.toList(),
                name = worldName,
                atmosphere = dto.world.atmospheres.select(atmoRoll),
                temperature = dto.world.temperatures.select(tempRoll),
                biosphere = dto.world.biospheres.select(bioRoll),
                population = dto.world.populations.select(popRoll),
                techlevel = dto.world.techlevels.select(techRoll),
                worldTags = getWorldTags(dto.worldRules, dto.worldTags.world_tags)
        )

    }

    private fun getWorldTags(worldRules: WorldRulesDto, worldTags: Map<String,WorldTag>): SwnWorldTagModel{
        // pick N tags
        val n = shuffler.roll(worldRules.num_tags)
        val tags : List<String> = shuffler.pickN(worldTags.keys, n).toList()

        val flavors : MutableList<Pair<String,String>> = mutableListOf()
        val enemies : MutableList<String> = mutableListOf()
        val friends : MutableList<String> = mutableListOf()
        val complications : MutableList<String> = mutableListOf()
        val things : MutableList<String> = mutableListOf()
        val places : MutableList<String> = mutableListOf()

        tags.forEach {
            flavors.add(Pair(it, worldTags.get(it)!!.flavor))
            enemies.addAll(worldTags.get(it)!!.enemies)
            friends.addAll(worldTags.get(it)!!.friends)
            complications.addAll(worldTags.get(it)!!.complications)
            things.addAll(worldTags.get(it)!!.things)
            places.addAll(worldTags.get(it)!!.places)
        }

        return SwnWorldTagModel(
                tags_and_flavor = flavors,
                enemies = shuffler.pickN(enemies, shuffler.roll(worldRules.num_tag_flavor)),
                friends = shuffler.pickN(friends, shuffler.roll(worldRules.num_tag_flavor)),
                complications = shuffler.pickN(complications, shuffler.roll(worldRules.num_tag_flavor)),
                things = shuffler.pickN(things, shuffler.roll(worldRules.num_tag_flavor)),
                places = shuffler.pickN(places, shuffler.roll(worldRules.num_tag_flavor))

        )
    }
}

class SwnWorldView: ViewStrategy<SwnWorldModel, HTML> {
    override fun transform(model: SwnWorldModel): HTML {
        return html {
            body {
                h3 { + model.config.headers.main }
                h4 { + model.config.headers.physical}
                p {
                    strong { small { + model.config.headers.atmosphere } }
                    + model.atmosphere
                    br {  }
                    strong { small { + model.config.headers.temperature } }
                    + model.temperature
                    br {  }
                    strong { small { + model.config.headers.biosphere } }
                    + model.biosphere
                }
                h4 { + model.config.headers.cultural}
                p {
                    strong { small { + model.config.headers.name } }
                    + model.name
                    br {  }
                    strong { small { + model.config.headers.cultures } }
                    + model.cultures.joinToString(", ")
                }
                p {
                    strong { small { + model.config.headers.population } }
                    + model.population
                    br {  }
                    strong { small { + model.config.headers.techlevel } }
                    + model.techlevel
                }
                h4 { + model.config.headers.worldtags}

                model.worldTags.tags_and_flavor.forEach {
                    h6 { + it.first }
                    blockquote { em { + it.second } }
                }
                h6 { + model.config.headers.enemies }
                p { + model.worldTags.enemies.joinToString("<br/>") }
                h6 { + model.config.headers.friends }
                p { + model.worldTags.friends.joinToString("<br/>") }
                h6 { + model.config.headers.complications }
                p { + model.worldTags.complications.joinToString("<br/>") }
                h6 { + model.config.headers.things }
                p { + model.worldTags.things.joinToString("<br/>") }
                h6 { + model.config.headers.places }
                p { + model.worldTags.places.joinToString("<br/>") }

            }
        }


    }
}

val swnModule = Kodein.Module {

    bind<ModelGenerator<SwnWorldModel>>() with provider {
        BaseGenerator<WorldBundleDto, SwnWorldModel>(
                loadingStrat = SwnDtoLoader(kodein),
                modelGeneratorStrat = SwnWorldModelGenerator(kodein)
        )
    }
    bind<Generator>(SwnConstantsCustom.WORLD) with provider {
        BaseGeneratorWithView<SwnWorldModel, HTML>(
                modelGen = instance(),
                viewTransform = SwnWorldView()
        )
    }

    bind<List<String>>(SwnConstantsCustom.GROUP) with singleton {
        listOf(SwnConstantsCustom.WORLD)
    }
}

object SwnConstantsCustom {
    val GROUP = getFinalPackageName(this.javaClass)

    val WORLD = "${GROUP}/world"


    val NAMES = "${GROUP}/names"

}
