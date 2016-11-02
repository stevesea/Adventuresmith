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


data class FotfCharNames(val names: Map<String,Map<String, List<String>>>) {
    companion object Resource {
        val resource_prefix = "char_names"
    }
}
data class FotfCharNoTrans(val playbooks: RangeMap,
                           val hitdie: Map<String,String>,
                           val heritages: Map<String, RangeMap>,
                           val alignments: Map<String, RangeMap>,
                           val virtues: Map<String, Int>,
                           val vices: Map<String, Int>,
                           val genders: List<String>
                           ) {
    companion object Resource {
        val resource_prefix = "char_no_translate"
    }
}
data class FotfCharHeadersDto(val virtues_vices: String,
                              val playbook: String,
                              val name_heritage: String,
                              val appearance: String,
                              val gear: String,
                              val abilities: String)
data class FotfCharConfigDto(val headers: FotfCharHeadersDto,
                             val genders: Map<String, String>,
                             val heritages: Map<String, String>,
                             val playbooks: Map<String, String>,
                             val alignments: Map<String, String>,
                             val abilities: List<String>)
data class FotfCharDto(val config: FotfCharConfigDto,
                       val gear: Map<String, List<RangeMap>>,
                       val appearances: Map<String, List<String>>
                       ){
    companion object Resource {
        val resource_prefix = "char"
    }
}
data class FotfTraitsDto(val virtues: List<String>,
                         val vices:List<String>) {
    companion object Resource {
        val resource_prefix = "traits"
    }
}

data class FotfCharBundleDto(val char: FotfCharDto,
                             val charSetup: FotfCharNoTrans,
                             val names: FotfCharNames,
                             val traits: FotfTraitsDto)


class FotfCharDtoLoader(override val kodein: Kodein) : DtoLoadingStrategy<FotfCharBundleDto>, KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()
    override fun load(locale: Locale): FotfCharBundleDto {
        return FotfCharBundleDto(
                names = resourceDeserializer.deserialize(
                        FotfCharNames::class.java,
                        FotfCharNames.resource_prefix,
                        locale),
                char = resourceDeserializer.deserialize(
                        FotfCharDto::class.java,
                        FotfCharDto.resource_prefix,
                        locale),
                charSetup = resourceDeserializer.deserialize(
                        FotfCharNoTrans::class.java,
                        FotfCharNoTrans.resource_prefix,
                        locale),
                traits = resourceDeserializer.deserialize(
                        FotfTraitsDto::class.java,
                        FotfTraitsDto.resource_prefix,
                        locale)
        )
    }
}

data class FotfCharModel(val config: FotfCharConfigDto,
                         val gender: String,
                         val playbook: String,
                         val heritage: String,
                         val alignment: String,
                         val name: String,
                         val abilRolls: List<Int>,
                         val appearances: Collection<String>,
                         val virtues: Collection<String>,
                         val vices: Collection<String>,
                         val gear: Collection<String>)
class FotfCharModelGenerator(override val kodein: Kodein) : ModelGeneratorStrategy<FotfCharBundleDto, FotfCharModel> ,
        KodeinAware {
    val shuffler : Shuffler = instance()
    override fun transform(dto: FotfCharBundleDto): FotfCharModel {
        val gender = shuffler.pick(dto.charSetup.genders)
        val playbook = shuffler.pick(dto.charSetup.playbooks)
        val heritage = shuffler.pick(dto.charSetup.heritages.get(playbook))
        val alignment = shuffler.pick(dto.charSetup.alignments.get(playbook))
        val name = shuffler.pick(dto.names.names.get(heritage)?.get(gender))
        return FotfCharModel(
                config = dto.char.config,
                gender = dto.char.config.genders.get(gender)!!,
                playbook = dto.char.config.playbooks.get(playbook)!!,
                heritage = dto.char.config.heritages.get(heritage)!!,
                alignment = dto.char.config.alignments.get(alignment)!!,
                abilRolls = shuffler.dice("3d6").rollN(dto.char.config.abilities.size),
                name = name,
                appearances = shuffler.pickN(dto.char.appearances.get(playbook), shuffler.dice("1d2+1").roll()),
                virtues = shuffler.pickN(dto.traits.virtues, dto.charSetup.virtues.getOrElse(alignment) {0}),
                vices = shuffler.pickN(dto.traits.vices, dto.charSetup.vices.getOrElse(alignment) {0}),
                gear = dto.char.gear.get(playbook)?.map { shuffler.pick(it)}!!
        )
    }
}

class FotfCharacterView: ViewStrategy<FotfCharModel, HTML> {
    override fun transform(model: FotfCharModel): HTML {
        val abils = model.config.abilities.zip(model.abilRolls) { it1, it2 -> "${it1}: ${it2}"}
        return html {
            body {
                h4 {
                    + "${model.config.headers.playbook} - ${model.playbook}"
                }
                h5 {
                    + model.config.headers.name_heritage
                }
                p {
                    + model.name
                    em { + model.gender }
                    br {}
                    + "${model.heritage} ${model.alignment}"
                }
                h5 {
                    + model.config.headers.abilities
                }
                p {
                    + "${abils.elementAt(0)}&nbsp;&nbsp;${abils.elementAt(1)}"
                    br{}
                    + "${abils.elementAt(2)}&nbsp;&nbsp;${abils.elementAt(3)}"
                    br{}
                    + "${abils.elementAt(4)}&nbsp;&nbsp;${abils.elementAt(5)}"
                    br{}
                    + abils.elementAt(6)
                }
                h5 {
                    + model.config.headers.virtues_vices
                }
                p {
                    + listOf(model.virtues, model.vices).flatten().joinToString("<br/>")
                }
                h5 {
                    + model.config.headers.gear
                }
                p {
                    + model.gear.joinToString("<br/>")
                }
            }
        }
    }
}

val fotfModule = Kodein.Module {

    bind<ModelGenerator<FotfCharModel>>() with provider {
        BaseGenerator<FotfCharBundleDto, FotfCharModel> (
                loadingStrat = FotfCharDtoLoader(kodein),
                modelGeneratorStrat = FotfCharModelGenerator(kodein)
        )
    }
    bind<Generator>(FotfConstants.CHARS) with provider {
        BaseGeneratorWithView<FotfCharModel, HTML> (
                modelGen = instance(),
                viewTransform = FotfCharacterView()
        )
    }

    listOf(
            FotfConstants.SPELLS
    ).forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }


    bind<List<String>>(FotfConstants.GROUP) with singleton {
        listOf(
                FotfConstants.SPELLS,
                FotfConstants.CHARS
        )
    }
}

object FotfConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val SPELLS = "${GROUP}/spells"
    val CHARS = "${GROUP}/chars"
}
