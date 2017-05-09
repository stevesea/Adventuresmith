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
import org.stevesea.adventuresmith.core.DtoLoadingStrategy
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.ModelGenerator
import org.stevesea.adventuresmith.core.ModelGeneratorStrategy
import org.stevesea.adventuresmith.core.RangeMap
import org.stevesea.adventuresmith.core.Shuffler
import org.stevesea.adventuresmith.core.ViewStrategy
import org.stevesea.adventuresmith.core.getFinalPackageName
import java.util.Locale

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


val swnModule = Kodein.Module {

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

    val FACTION = "$GROUP/faction"

    val NAMES = "$GROUP/names"

    val generators = listOf(
            SwnConstantsCustom.FACTION
    )

}
