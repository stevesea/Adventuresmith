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

package org.stevesea.adventuresmith.data_k

import org.stevesea.adventuresmith.*
import org.stevesea.adventuresmith.html_builder.*

data class FpArtifactHeaders(val main: String,
                             val origin: String,
                             val pow: String)
data class FpArtifactConfigDto(val headers: FpArtifactHeaders)
data class FpArtifactDto(val config: FpArtifactConfigDto,
                         val origins: Map<String,List<String>>,
                         val powers: Map<String,List<String>>)
data class FpArtifactModel(val config: FpArtifactConfigDto,
                           val origin: Pair<String,String>,
                           val power: Pair<String, String>)

data class FpMonsterHeaders(val main: String,
                             val nature: String,
                             val role: String)
data class FpMonsterConfigDto(val headers: FpMonsterHeaders)
data class FpMonsterDto(val config: FpMonsterConfigDto,
                        val natures: Map<String,List<String>>,
                        val roles: Map<String,List<String>>)
data class FpMonsterModel(val config: FpMonsterConfigDto,
                          val nature: Pair<String,String>,
                          val role: Pair<String, String>)

data class FpCityHeaders(val main: String,
                         val feature: String,
                         val population: String,
                         val society: String,
                         val trouble: String)
data class FpCityConfigDto(val headers: FpCityHeaders)
data class FpCityDto(val config: FpCityConfigDto,
                     val features: Map<String,List<String>>,
                     val populations: Map<String,List<String>>,
                     val societies: Map<String,List<String>>,
                     val troubles: Map<String,List<String>>)
data class FpCityModel(val config: FpCityConfigDto,
                       val feature: Pair<String,String>,
                       val population: Pair<String,String>,
                       val society: Pair<String,String>,
                       val trouble: Pair<String, String>)

data class FpDungeonHeaders(val main: String,
                         val history: String,
                         val denizen: String,
                         val trial: String,
                         val secret: String)
data class FpDungeonConfigDto(val headers: FpDungeonHeaders)
data class FpDungeonDto(val config: FpDungeonConfigDto,
                        val histories: Map<String,List<String>>,
                        val denizens: Map<String,List<String>>,
                        val trials: Map<String,List<String>>,
                        val secrets: Map<String,List<String>>)
data class FpDungeonModel(val config: FpDungeonConfigDto,
                          val history: Pair<String,String>,
                          val denizen: Pair<String,String>,
                          val trial: Pair<String,String>,
                          val secret: Pair<String, String>)

class FpArtifactModelGenerator(val shuffler: Shuffler) :  ModelGeneratorStrategy<FpArtifactDto, FpArtifactModel> {
    override fun transform(dto: FpArtifactDto): FpArtifactModel {
        return FpArtifactModel(
                config = dto.config,
                origin = shuffler.pickPairFromMapofLists(dto.origins),
                power = shuffler.pickPairFromMapofLists(dto.powers)
        )
    }
}

class FpMonsterModelGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<FpMonsterDto, FpMonsterModel> {
    override fun transform(dto: FpMonsterDto): FpMonsterModel {
        return FpMonsterModel(
                config = dto.config,
                nature = shuffler.pickPairFromMapofLists(dto.natures),
                role = shuffler.pickPairFromMapofLists(dto.roles))
    }
}
class FpCityModelGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<FpCityDto, FpCityModel> {
    override fun transform(dto: FpCityDto): FpCityModel {
        return FpCityModel(
                config = dto.config,
                feature = shuffler.pickPairFromMapofLists(dto.features),
                population = shuffler.pickPairFromMapofLists(dto.populations),
                society = shuffler.pickPairFromMapofLists(dto.societies),
                trouble = shuffler.pickPairFromMapofLists(dto.troubles)
        )
    }
}

class FpDungeonModelGenerator(val shuffler: Shuffler) : ModelGeneratorStrategy<FpDungeonDto, FpDungeonModel> {
    override fun transform(dto: FpDungeonDto): FpDungeonModel {
        return FpDungeonModel(
                config = dto.config,
                history = shuffler.pickPairFromMapofLists(dto.histories),
                denizen = shuffler.pickPairFromMapofLists(dto.denizens),
                trial = shuffler.pickPairFromMapofLists(dto.trials),
                secret = shuffler.pickPairFromMapofLists(dto.secrets)
        )
    }
}
class FpArtifactView : ViewStrategy<FpArtifactModel, HTML> {
    override fun transform(model: FpArtifactModel): HTML {
        return html {
            body {
                h4 { + model.config.headers.main }
                h5 { + model.config.headers.origin }
                p {
                    strong { small { + model.origin.first } }
                    + "- ${model.origin.second}"
                }
                h5 { + model.config.headers.pow }
                p {
                    strong { small { + model.power.first } }
                    + "- ${model.power.second}"
                }
            }
        }
    }
}

class FpMonsterViewTransformer : ViewStrategy<FpMonsterModel, HTML> {
    override fun transform(model: FpMonsterModel): HTML {
        return html {
            body {
                h4 { + model.config.headers.main }
                h5 { + model.config.headers.nature }
                p {
                    strong { small { + model.nature.first } }
                    + "- ${model.nature.second}"
                }
                h5 { + model.config.headers.role }
                p {
                    strong { small { + model.role.first } }
                    + "- ${model.role.second}"
                }
            }
        }
    }
}
class FpCityView : ViewStrategy<FpCityModel, HTML> {
    override fun transform(model: FpCityModel): HTML {
        return html {
            body {
                h4 { + model.config.headers.main }
                h5 { + model.config.headers.feature }
                p {
                    strong { small { + model.feature.first } }
                    + "- ${model.feature.second}"
                }
                h5 { + model.config.headers.population }
                p {
                    strong { small { + model.population.first } }
                    + "- ${model.population.second}"
                }
                h5 { + model.config.headers.society }
                p {
                    strong { small { + model.society.first } }
                    + "- ${model.society.second}"
                }
                h5 { + model.config.headers.trouble }
                p {
                    strong { small { + model.trouble.first } }
                    + "- ${model.trouble.second}"
                }
            }
        }
    }
}

class FpDungeonView : ViewStrategy<FpDungeonModel, HTML> {
    override fun transform(model: FpDungeonModel): HTML {
        return html {
            body {
                h4 { + model.config.headers.main }
                h5 { + model.config.headers.history }
                p {
                    strong { small { + model.history.first } }
                    + "- ${model.history.second}"
                }
                h5 { + model.config.headers.denizen }
                p {
                    strong { small { + model.denizen.first } }
                    + "- ${model.denizen.second}"
                }
                h5 { + model.config.headers.trial }
                p {
                    strong { small { + model.trial.first } }
                    + "- ${model.trial.second}"
                }
                h5 { + model.config.headers.secret }
                p {
                    strong { small { + model.secret.first } }
                    + "- ${model.secret.second}"
                }
            }
        }
    }
}

class FpArtifactDtoLoader : DtoLoadingStrategy<FpArtifactDto> {
    override fun load(): FpArtifactDto {
        return CachingRawResourceDeserializer.deserialize(R.raw.fourth_page_artifact, FpArtifactDto::class.java)
    }
}

class FpCityDtoLoader : DtoLoadingStrategy<FpCityDto> {
    override fun load(): FpCityDto {
        return CachingRawResourceDeserializer.deserialize(R.raw.fourth_page_city, FpCityDto::class.java)
    }
}
class FpDungeonDtoLoader : DtoLoadingStrategy<FpDungeonDto> {
    override fun load(): FpDungeonDto {
        return CachingRawResourceDeserializer.deserialize(R.raw.fourth_page_dungeon, FpDungeonDto::class.java)
    }
}
class FpMonsterDtoLoader : DtoLoadingStrategy<FpMonsterDto> {
    override fun load(): FpMonsterDto {
        return CachingRawResourceDeserializer.deserialize(R.raw.fourth_page_monster, FpMonsterDto::class.java)
    }
}

class FourthPageArtifactGenerator(shuffler: Shuffler = Shuffler()) : GeneratorLTV<FpArtifactDto, FpArtifactModel, HTML>(
        FpArtifactDtoLoader(),
        FpArtifactModelGenerator(shuffler),
        FpArtifactView()
)

class FourthPageCityGenerator(shuffler: Shuffler = Shuffler()) : GeneratorLTV<FpCityDto, FpCityModel, HTML>(
        FpCityDtoLoader(),
        FpCityModelGenerator(shuffler),
        FpCityView()
)

class FourthPageDungeonGenerator(shuffler: Shuffler = Shuffler()) : GeneratorLTV<FpDungeonDto, FpDungeonModel, HTML>(
        FpDungeonDtoLoader(),
        FpDungeonModelGenerator(shuffler),
        FpDungeonView()
)
class FourthPageMonsterGenerator(shuffler: Shuffler = Shuffler()) : GeneratorLTV<FpMonsterDto, FpMonsterModel, HTML>(
        FpMonsterDtoLoader(),
        FpMonsterModelGenerator(shuffler),
        FpMonsterViewTransformer()
)




