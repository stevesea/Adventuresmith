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

import org.stevesea.adventuresmith.core.*
import java.util.*

data class MrAfflictionsDto(val afflictions: List<String>) {
    companion object Resource {
        val resource_prefix = "afflictions"
    }
}

data class MrPotionEffectsDto(val potion_effects: List<String>) {
    companion object Resource {
        val resource_prefix = "potion_effects"
    }
}

data class MrMagicDto(val templates: List<String>,
                      val effects: List<String>,
                      val forms: List<String>,
                      val elements: List<String>){
    companion object Resource {
        val resource_prefix = "magic"
    }
}

data class MrMonstersDto(val templates: List<String>,
                          val creatures: List<String>){
    companion object Resource {
        val resource_prefix = "monsters"
    }
}

data class MrMonsterBundleDto(val magicDto: MrMagicDto,
                               val creatureDto: MrMonstersDto)

data class MrItemsDto(val templates: List<String>,
                      val items: List<String>){
    companion object Resource {
        val resource_prefix = "items"
    }
}

data class MrItemBundleDto(val magicDto: MrMagicDto, val itemDto: MrItemsDto)

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


class MrAfflictionsDtoLoader:  SimpleListLoader {
    override fun load(locale: Locale) : List<String> {
        return CachingResourceDeserializer.deserialize(
                MrAfflictionsDto::class.java,
                MrAfflictionsDto.resource_prefix,
                locale).afflictions
    }
}

class MrPotionEffectsDtoLoader: SimpleListLoader {
    override fun load(locale: Locale): List<String> {
        return CachingResourceDeserializer.deserialize(
                MrPotionEffectsDto::class.java,
                MrPotionEffectsDto.resource_prefix,
                locale).potion_effects
    }
}

class MrMagicDtoLoader: DtoLoadingStrategy<MrMagicDto> {
    override fun load(locale: Locale): MrMagicDto {
        return CachingResourceDeserializer.deserialize(
                MrMagicDto::class.java,
                MrMagicDto.resource_prefix,
                locale)
    }
}
class MrCharacterBundleDtoLoader: DtoLoadingStrategy<MrCharacterBundleDto> {
    override fun load(locale: Locale): MrCharacterBundleDto {
        return MrCharacterBundleDto(
                characterDto = CachingResourceDeserializer.deserialize(
                        MrCharactersDto::class.java,
                        MrCharactersDto.resource_prefix,
                        locale),
                namesDto = CachingResourceDeserializer.deserialize(
                        MrNamesDto::class.java,
                        MrNamesDto.resource_prefix,
                        locale))
    }
}

class MrMonsterBundleDtoLoader: DtoLoadingStrategy<MrMonsterBundleDto> {
    override fun load(locale: Locale): MrMonsterBundleDto {
        return MrMonsterBundleDto(
                magicDto = CachingResourceDeserializer.deserialize(
                        MrMagicDto::class.java,
                        MrMagicDto.resource_prefix,
                        locale),
                creatureDto = CachingResourceDeserializer.deserialize(
                        MrMonstersDto::class.java,
                        MrMonstersDto.resource_prefix,
                        locale))
    }
}

class MrItemBundleDtoLoader: DtoLoadingStrategy<MrItemBundleDto> {
    override fun load(locale: Locale): MrItemBundleDto {
        return MrItemBundleDto(
                magicDto = CachingResourceDeserializer.deserialize(
                        MrMagicDto::class.java,
                        MrMagicDto.resource_prefix,
                        locale),
                itemDto = CachingResourceDeserializer.deserialize(
                        MrItemsDto::class.java,
                        MrItemsDto.resource_prefix,
                        locale))
    }
}


fun magicMapHelper(shuffler: Shuffler, dto: MrMagicDto) : Map<String, String> {
    return mapOf(
            "element" to shuffler.pick(dto.elements),
            "effect" to shuffler.pick(dto.effects),
            "form" to shuffler.pick(dto.forms)
    )
}

class MrMagicMapGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<MrMagicDto, TemplateMapModel> {
    override fun transform(dto: MrMagicDto): TemplateMapModel {
        return TemplateMapModel(
                template = shuffler.pick(dto.templates),
                map = magicMapHelper(shuffler, dto)
        )
    }
}

class MrItemMapGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<MrItemBundleDto, TemplateMapModel> {
    override fun transform(dto: MrItemBundleDto): TemplateMapModel {
        val m = mutableMapOf(
                "item" to shuffler.pick(dto.itemDto.items)
        )
        m.putAll(magicMapHelper(shuffler, dto.magicDto))
        return TemplateMapModel(
                template = shuffler.pick(dto.itemDto.templates),
                map = m
        )
    }
}

class MrMonsterMapGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<MrMonsterBundleDto, TemplateMapModel> {
    override fun transform(dto: MrMonsterBundleDto): TemplateMapModel {
        val creatures = shuffler.pickN(dto.creatureDto.creatures, 2)
        val m = mutableMapOf(
                "creature1" to creatures.elementAt(0),
                "creature2" to creatures.elementAt(1)
        )
        m.putAll(magicMapHelper(shuffler, dto.magicDto))
        return TemplateMapModel(
                template = shuffler.pick(dto.creatureDto.templates),
                map = m
        )
    }
}

class MrAfflictionsGenerator(shuffler: Shuffler = Shuffler()) : BaseSimpleGenerator(
        MrAfflictionsDtoLoader(), shuffler
)

class MrPotionEffectsGenerator(shuffler: Shuffler = Shuffler()) : BaseSimpleGenerator(
        MrPotionEffectsDtoLoader(), shuffler
)

class MrMagicGenerator(shuffler: Shuffler = Shuffler()) : BaseGenerator<MrMagicDto, TemplateMapModel, String>(
        MrMagicDtoLoader(),
        MrMagicMapGenerator(shuffler),
        ApplyTemplateView()
)
class MrItemGenerator(shuffler: Shuffler = Shuffler()) : BaseGenerator<MrItemBundleDto, TemplateMapModel, String>(
        MrItemBundleDtoLoader(),
        MrItemMapGenerator(shuffler),
        ApplyTemplateView()
)

class MrMonsterGenerator(shuffler: Shuffler = Shuffler()) : BaseGenerator<MrMonsterBundleDto, TemplateMapModel, String>(
        MrMonsterBundleDtoLoader(),
        MrMonsterMapGenerator(shuffler),
        ApplyTemplateView()
)



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

class MrCharacterModelGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<MrCharacterBundleDto, MrCharacterModel> {
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

class MrCharacterGenerator(shuffler: Shuffler = Shuffler()) : BaseGenerator<MrCharacterBundleDto, MrCharacterModel, HTML>(
        MrCharacterBundleDtoLoader(),
        MrCharacterModelGenerator(shuffler),
        MrCharacterView()
)

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
