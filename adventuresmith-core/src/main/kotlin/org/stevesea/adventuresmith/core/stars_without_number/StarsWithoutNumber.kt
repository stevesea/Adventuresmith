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

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.samskivert.mustache.Mustache
import org.stevesea.adventuresmith.core.BaseGenerator
import org.stevesea.adventuresmith.core.BaseGeneratorWithView
import org.stevesea.adventuresmith.core.CachingResourceDeserializer
import org.stevesea.adventuresmith.core.DataDrivenGenDto
import org.stevesea.adventuresmith.core.DtoLoadingStrategy
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.HTML
import org.stevesea.adventuresmith.core.ModelGenerator
import org.stevesea.adventuresmith.core.ModelGeneratorStrategy
import org.stevesea.adventuresmith.core.RangeMap
import org.stevesea.adventuresmith.core.Shuffler
import org.stevesea.adventuresmith.core.ViewStrategy
import org.stevesea.adventuresmith.core.getFinalPackageName
import org.stevesea.adventuresmith.core.html
import java.util.Locale

data class WorldTag(val flavor: String,
                    val enemies: List<String>,
                    val friends: List<String>,
                    val complications: List<String>,
                    val things: List<String>,
                    val places: List<String>)

data class WorldTagsDto(val world_tags: Map<String, WorldTag>) {
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
                    val techlevels: RangeMap) {

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
                         val rules: WorldRule) {
    companion object Resource {
        val resource_prefix = "world_rules"
    }
}

data class WorldBundleDto(val world: WorldDto,
                          val worldTags: WorldTagsDto,
                          val worldRules: WorldRulesDto,
                          val names: DataDrivenGenDto)

class SwnWorldDtoLoader(override val kodein: Kodein) : DtoLoadingStrategy<WorldBundleDto>, KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return resourceDeserializer.deserialize(
                GeneratorMetaDto::class.java,
                SwnConstantsCustom.GROUP + "/" + WorldDto.resource_prefix + ".meta",
                locale
        )
    }
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
data class SwnFactionTypeDto(val stats_input: List<Int>,
                             val hp: Int,
                             val primary_assets: Int,
                             val secondary_assets: Int,
                             val ntags: List<Int>)
data class SwnFactionDto(val faction_types: Map<String, SwnFactionTypeDto>,
                         val faction_type_chance: RangeMap,
                         val goals: List<String>,
                         val tags: List<String>,
                         val assets: Map<String, Map<Int, List<String>>>,
                         val template: String) {
    companion object Resource {
        val resource_prefix = "faction"
    }
}
class SwnFactionDtoLoader(override val kodein: Kodein) : DtoLoadingStrategy<SwnFactionDto>, KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return resourceDeserializer.deserialize(
                GeneratorMetaDto::class.java,
                SwnConstantsCustom.GROUP + "/" + SwnFactionDto.resource_prefix + ".meta",
                locale
        )
    }

    override fun load(locale: Locale): SwnFactionDto {
        return resourceDeserializer.deserialize(
                SwnFactionDto::class.java,
                SwnFactionDto.resource_prefix,
                locale
        )
    }
}

data class SwnFactionModel(
        val template : String,
        val type: String,
        val hp: Int,
        val force: Int,
        val cunning: Int,
        val wealth: Int,
        val goal: String,
        val assets: List<String>,
        val tags: List<String>
)

class SwnFactionModelGenerator(override val kodein: Kodein) :
ModelGeneratorStrategy<SwnFactionDto, SwnFactionModel>,
KodeinAware {
    val shuffler: Shuffler = instance()

    override fun transform(dto: SwnFactionDto, input: Map<String, String>?): SwnFactionModel {
        val ftype = shuffler.pick(dto.faction_type_chance)

        val ftypeDto: SwnFactionTypeDto = dto.faction_types.get(ftype)!!

        val statMap: MutableMap<String, Int> = mutableMapOf()
        val stats: List<String> = shuffler.pickN<String>(listOf("force", "cunning", "wealth"), 3) as List<String>
        val ntags = shuffler.pick(ftypeDto.ntags)

        stats.zip(ftypeDto.stats_input) { it1, it2 -> statMap.put(it1, it2) }

        // handle primary/secondaries for assets
        val primary: String = stats.take(1)[0]
        val secondaries = stats.takeLast(2)

        val assets: MutableList<String> = mutableListOf()

        val primaryVal: Int = statMap.get(primary)!!

        val primaryAssets = dto.assets.get(primary).orEmpty()
        val possibleAssets: MutableList<String> = mutableListOf()
        for (v in 1..primaryVal) {
            if (primaryAssets.containsKey(v)) {
                possibleAssets.addAll(primaryAssets[v]!!)
            }
        }

        assets.addAll(shuffler.pickN(possibleAssets, ftypeDto.primary_assets))

        possibleAssets.clear()
        for (s in secondaries) { // iterate over secondary stats
            for (v in 1..statMap.get(s)!!) {
                if (dto.assets[s].orEmpty().containsKey(v)) {
                    possibleAssets.addAll(dto.assets[s].orEmpty()[v]!!)
                }
            }
        }

        assets.addAll(shuffler.pickN(possibleAssets, ftypeDto.secondary_assets))

        return SwnFactionModel(
                goal = shuffler.pick(dto.goals),
                type = ftype.capitalize(),
                force = statMap.get("force")!!,
                cunning = statMap.get("cunning")!!,
                wealth = statMap.get("wealth")!!,
                hp = ftypeDto.hp,
                tags = shuffler.pickN<String>(dto.tags, ntags) as List<String>,
                assets = assets,
                template = dto.template
        )
    }
}

class SwnFactionView : ViewStrategy<SwnFactionModel, String> {
    override fun transform(model: SwnFactionModel): String {
        return Mustache.compiler()
                .escapeHTML(false)
                .compile(model.template)
                .execute(model)
                .trim()
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
    override fun transform(dto: WorldBundleDto, input: Map<String, String>?): SwnWorldModel {
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

    private fun getWorldTags(worldRules: WorldRulesDto, worldTags: Map<String, WorldTag>): SwnWorldTagModel {
        // pick N tags
        val n = shuffler.roll(worldRules.num_tags)
        val tags : List<String> = shuffler.pickN(worldTags.keys, n).toList()

        val flavors : MutableList<Pair<String, String>> = mutableListOf()
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

class SwnWorldView : ViewStrategy<SwnWorldModel, HTML> {
    override fun transform(model: SwnWorldModel) : HTML {
        return html {
            body {
                h3 { + model.config.headers.main }
                h4 { + model.config.headers.physical }
                p {
                    strong { small { + model.config.headers.atmosphere } }
                    + model.atmosphere
                    br { }
                    strong { small { + model.config.headers.temperature } }
                    + model.temperature
                    br { }
                    strong { small { + model.config.headers.biosphere } }
                    + model.biosphere
                }
                h4 { + model.config.headers.cultural }
                p {
                    strong { small { + model.config.headers.name } }
                    + model.name
                    br { }
                    strong { small { + model.config.headers.cultures } }
                    + model.cultures.joinToString(", ")
                }
                p {
                    strong { small { + model.config.headers.population } }
                    + model.population
                    br { }
                    strong { small { + model.config.headers.techlevel } }
                    + model.techlevel
                }
                h4 { + model.config.headers.worldtags }

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
                loadingStrat = SwnWorldDtoLoader(kodein),
                modelGeneratorStrat = SwnWorldModelGenerator(kodein)
        )
    }
    bind<Generator>(SwnConstantsCustom.WORLD) with provider {
        BaseGeneratorWithView<SwnWorldModel, HTML>(
                SwnConstantsCustom.WORLD,
                modelGen = instance(),
                viewTransform = SwnWorldView()
        )
    }

    bind<ModelGenerator<SwnFactionModel>>() with provider {
        BaseGenerator<SwnFactionDto, SwnFactionModel>(
                loadingStrat = SwnFactionDtoLoader(kodein),
                modelGeneratorStrat = SwnFactionModelGenerator(kodein)
        )
    }
    bind<Generator>(SwnConstantsCustom.FACTION) with provider {
        BaseGeneratorWithView<SwnFactionModel, String>(
                SwnConstantsCustom.FACTION,
                modelGen = instance(),
                viewTransform = SwnFactionView()
        )
    }
}

object SwnConstantsCustom {
    val GROUP = getFinalPackageName(this.javaClass)

    val WORLD = "$GROUP/world"
    val FACTION = "$GROUP/faction"

    val NAMES = "$GROUP/names"

    val generators = listOf(
            SwnConstantsCustom.WORLD,
            SwnConstantsCustom.FACTION
    )

}
