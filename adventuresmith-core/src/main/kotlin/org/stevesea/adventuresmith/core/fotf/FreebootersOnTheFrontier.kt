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

package org.stevesea.adventuresmith.core.fotf

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*
import java.util.*


// names live in a different YaML, since I expect most translators aren't going to want to
// explicitly
data class FotfSpellWizardNamesDto(val part1: List<String>,
                                   val part2: List<String>) {
    companion object Resource {
        val resource_prefix = "wizard_names"
    }
}

data class FotfSpellDto(val name_templates: List<String>,
                        val elements: List<String>,
                        val forms: List<String>,
                        val adjectives: List<String>){
    companion object Resource {
        val resource_prefix = "spells"
    }
}

// the inputbundle is the combined data from two separate resource reads
data class FotfSpellDtoBundle(val spellDto: FotfSpellDto,
                              val wizNameDto: FotfSpellWizardNamesDto)

class FotfSpellMapGenerator(override val kodein: Kodein) : ModelGeneratorStrategy<FotfSpellDtoBundle, TemplateMapModel> ,
        KodeinAware {
    val shuffler : Shuffler = instance()
    override fun transform(dto: FotfSpellDtoBundle): TemplateMapModel {
        val forms = shuffler.pickN(dto.spellDto.forms, 2)
        return TemplateMapModel(
                template = shuffler.pick(dto.spellDto.name_templates),
                map = mapOf(
                        "element" to shuffler.pick(dto.spellDto.elements),
                        "adjective" to shuffler.pick(dto.spellDto.adjectives),
                        "name_part1" to shuffler.pick(dto.wizNameDto.part1),
                        "name_part2" to shuffler.pick(dto.wizNameDto.part2),
                        "form1" to forms.elementAt(0),
                        "form2" to forms.elementAt(1)
                )
        )
    }
}

class FotfSpellDtoLoader : DtoLoadingStrategy<FotfSpellDtoBundle> {
    override fun load(locale: Locale): FotfSpellDtoBundle {
        return FotfSpellDtoBundle(
                spellDto = CachingResourceDeserializer.deserialize(
                        FotfSpellDto::class.java,
                        FotfSpellDto.resource_prefix,
                        locale),
                wizNameDto = CachingResourceDeserializer.deserialize(
                        FotfSpellWizardNamesDto::class.java,
                        FotfSpellWizardNamesDto.resource_prefix,
                        locale)
        )
    }
}

val fotfModule = Kodein.Module {
    bind<ModelGenerator<TemplateMapModel>>(FotfConstants.SPELLS) with provider {
        BaseGenerator<FotfSpellDtoBundle, TemplateMapModel> (
                loadingStrat = FotfSpellDtoLoader(),
                modelGeneratorStrat = FotfSpellMapGenerator(kodein)
        )
    }
    bind<Generator>(FotfConstants.SPELLS) with singleton {
        BaseGeneratorWithView<TemplateMapModel, String> (
                modelGen = instance(FotfConstants.SPELLS),
                viewTransform = ApplyTemplateView()
        )
    }


    bind<List<String>>(FotfConstants.GROUP) with singleton {
        listOf(FotfConstants.SPELLS)
    }
}

object FotfConstants {
    val GROUP = "fotf"

    val SPELLS = "${GROUP}.spell"
}
