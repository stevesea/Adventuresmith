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

package org.stevesea.adventuresmith.core.maze_rats

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*
import java.util.*

data class MrNamesDto(val surnames: List<String>,
                      val forenames: List<String>){
    companion object Resource {
        val resource_prefix = "names"
    }
}

data class MrCharAbilitiesDto(val str: String,
                              val dex: String,
                              val wil: String,
                              val hp: String)
data class MrCharacterHeaderDto(val name: String,
                                val personality: String,
                                val appearance: String,
                                val weapons: String,
                                val equipment: String,
                                val abilities: MrCharAbilitiesDto)
data class MrCharacterConfigDto(val headers: MrCharacterHeaderDto)
data class MrCharactersDto(val config: MrCharacterConfigDto,
                           val personalities: List<String>,
                           val appearances: List<String>,
                           val weapons: List<String>,
                           val equipment: List<String>){
    companion object Resource {
        val resource_prefix = "characters"
    }
}
data class MrCharacterBundleDto(val characterDto: MrCharactersDto,
                                val namesDto: MrNamesDto)


class MrCharacterBundleDtoLoader(override val kodein: Kodein): DtoLoadingStrategy<MrCharacterBundleDto> , KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()
    override fun load(locale: Locale): MrCharacterBundleDto {
        return MrCharacterBundleDto(
                characterDto = resourceDeserializer.deserialize(
                        MrCharactersDto::class.java,
                        MrCharactersDto.resource_prefix,
                        locale),
                namesDto = resourceDeserializer.deserialize(
                        MrNamesDto::class.java,
                        MrNamesDto.resource_prefix,
                        locale))
    }
}

data class MrCharacterModel(val config: MrCharacterConfigDto,
                            val name: String,
                            val str: Int,
                            val dex: Int,
                            val wil: Int,
                            val hp: Int,
                            val personalities: Collection<String>,
                            val appearances: Collection<String>,
                            val weapons: Collection<String>,
                            val equipment: Collection<String>)

class MrCharGenerator(override val kodein: Kodein) : ModelGeneratorStrategy<MrCharacterBundleDto, MrCharacterModel> ,
        KodeinAware {
    val shuffler : Shuffler = instance()
    override fun transform(dto: MrCharacterBundleDto): MrCharacterModel {
        return MrCharacterModel(
                config = dto.characterDto.config,
                name = "${shuffler.pick(dto.namesDto.forenames)} ${shuffler.pick(dto.namesDto.surnames)}",
                str = shuffler.dice("3d6").roll(),
                dex = shuffler.dice("3d6").roll(),
                wil = shuffler.dice("3d6").roll(),
                hp = shuffler.dice("1d6").roll(),
                personalities = shuffler.pickN(dto.characterDto.personalities, shuffler.dice("1d2+1").roll()),
                appearances = shuffler.pickN(dto.characterDto.appearances, shuffler.dice("1d2+1").roll()),
                weapons = shuffler.pickN(dto.characterDto.weapons, 2),
                equipment = shuffler.pickN(dto.characterDto.equipment, 3)
        )
    }
}

class MrCharacterView : ViewStrategy<MrCharacterModel, HTML> {
    override fun transform(model: MrCharacterModel): HTML {
        return html {
            body {
                p {
                    strong { small { + model.config.headers.name } }
                    + model.name
                }
                p {
                    small {
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;STR: ${model.str} DEX: ${model.dex} WIL: ${model.wil} &nbsp;&nbsp;&nbsp;&nbsp;HP: ${model.hp}"
                    }
                }
                p {
                    strong { small { + model.config.headers.personality } }
                    + model.personalities.joinToString(", ")
                }
                p {
                    strong { small { + model.config.headers.appearance } }
                    + model.appearances.joinToString(", ")
                }
                p {
                    strong { small { + model.config.headers.weapons } }
                    + model.weapons.joinToString(", ")
                }
                p {
                    strong { small { + model.config.headers.equipment } }
                    + model.equipment.joinToString(", ")
                }
            }
        }

    }
}

val mrModule = Kodein.Module {
    bind<ModelGenerator<MrCharacterModel>>() with provider {
        BaseGenerator<MrCharacterBundleDto, MrCharacterModel>(
                MrCharacterBundleDtoLoader(kodein),
                MrCharGenerator(kodein))
    }
    bind<Generator>(MrConstants.CHAR) with provider {
        BaseGeneratorWithView<MrCharacterModel, HTML>(
                instance(),
                MrCharacterView())
    }

    listOf(
            MrConstants.MONSTER,
            MrConstants.ITEM,
            MrConstants.MAGIC,
            MrConstants.POTION_EFFECTS,
            MrConstants.AFFLICTIONS
    ).forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }

    bind<List<String>>(MrConstants.GROUP) with singleton {
        listOf(
                MrConstants.AFFLICTIONS,
                MrConstants.CHAR,
                MrConstants.ITEM,
                MrConstants.MAGIC,
                MrConstants.MONSTER,
                MrConstants.POTION_EFFECTS
        )
    }
}

object MrConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val AFFLICTIONS = "${GROUP}/afflictions"
    val POTION_EFFECTS = "${GROUP}/potion_effects"
    val ITEM = "${GROUP}/items"
    val MAGIC = "${GROUP}/magic"
    val MONSTER = "${GROUP}/monsters"
    val CHAR = "${GROUP}/char"
}
