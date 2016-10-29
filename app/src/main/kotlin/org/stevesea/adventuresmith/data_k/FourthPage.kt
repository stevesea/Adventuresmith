/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.data_k

import org.stevesea.adventuresmith.*
import org.stevesea.adventuresmith.html_builder.*

/*
seems neat... but let's do for different class
data class FpArtifactDto(val map: Map<String,String>){
    val origin: String by map
    val pow: String by map
    val originVal: String by map
    val powVal: String by map
}

usage:
        return FpArtifactDto(mapOf(
                "origin" to okey,
                "originVal" to shuffler.pick(inDto.origins[okey]!!),
                "pow" to pkey,
                "powVal" to shuffler.pick(inDto.powers[pkey]!!)
        ))
*/

data class FpArtifactHeaders(val main: String,
                             val origin: String,
                             val pow: String)
data class FpArtifactConfigDto(val headers: FpArtifactHeaders)
data class FpArtifactInputDto(val config: FpArtifactConfigDto,
                              val origins: Map<String,List<String>>,
                              val powers: Map<String,List<String>>)
data class FpArtifactDto(val config: FpArtifactConfigDto,
                         val origin: Pair<String,String>,
                         val power: Pair<String, String>)

data class FpMonsterHeaders(val main: String,
                             val nature: String,
                             val role: String)
data class FpMonsterConfigDto(val headers: FpMonsterHeaders)
data class FpMonsterInputDto(val config: FpMonsterConfigDto,
                             val natures: Map<String,List<String>>,
                             val roles: Map<String,List<String>>)
data class FpMonsterDto(val config: FpMonsterConfigDto,
                        val nature: Pair<String,String>,
                        val role: Pair<String, String>)

data class FpCityHeaders(val main: String,
                         val feature: String,
                         val population: String,
                         val society: String,
                         val trouble: String)
data class FpCityConfigDto(val headers: FpCityHeaders)
data class FpCityInputDto(val config: FpCityConfigDto,
                          val features: Map<String,List<String>>,
                          val populations: Map<String,List<String>>,
                          val societies: Map<String,List<String>>,
                          val troubles: Map<String,List<String>>)
data class FpCityDto(val config: FpCityConfigDto,
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
data class FpDungeonInputDto(val config: FpDungeonConfigDto,
                             val histories: Map<String,List<String>>,
                             val denizens: Map<String,List<String>>,
                             val trials: Map<String,List<String>>,
                             val secrets: Map<String,List<String>>)
data class FpDungeonDto(val config: FpDungeonConfigDto,
                        val history: Pair<String,String>,
                        val denizen: Pair<String,String>,
                        val trial: Pair<String,String>,
                        val secret: Pair<String, String>)

class FpArtifactGenerator(val shuffler: Shuffler) :  GeneratorTransformStrategy<FpArtifactInputDto, FpArtifactDto> {
    override fun transform(inDto: FpArtifactInputDto): FpArtifactDto {
        return FpArtifactDto(
                config = inDto.config,
                origin = shuffler.pickPairFromMapofLists(inDto.origins),
                power = shuffler.pickPairFromMapofLists(inDto.powers)
        )
    }
}

class FpMonsterGenerator(val shuffler: Shuffler) : GeneratorTransformStrategy<FpMonsterInputDto, FpMonsterDto> {
    override fun transform(inDto: FpMonsterInputDto): FpMonsterDto {
        return FpMonsterDto(
                config = inDto.config,
                nature = shuffler.pickPairFromMapofLists(inDto.natures),
                role = shuffler.pickPairFromMapofLists(inDto.roles))
    }
}
class FpCityGenerator(val shuffler: Shuffler) : GeneratorTransformStrategy<FpCityInputDto, FpCityDto> {
    override fun transform(inDto: FpCityInputDto): FpCityDto {
        return FpCityDto(
                config = inDto.config,
                feature = shuffler.pickPairFromMapofLists(inDto.features),
                population = shuffler.pickPairFromMapofLists(inDto.populations),
                society = shuffler.pickPairFromMapofLists(inDto.societies),
                trouble = shuffler.pickPairFromMapofLists(inDto.troubles)
        )
    }
}

class FpDungeonGenerator(val shuffler: Shuffler) : GeneratorTransformStrategy<FpDungeonInputDto, FpDungeonDto> {
    override fun transform(inDto: FpDungeonInputDto): FpDungeonDto {
        return FpDungeonDto(
                config = inDto.config,
                history = shuffler.pickPairFromMapofLists(inDto.histories),
                denizen = shuffler.pickPairFromMapofLists(inDto.denizens),
                trial = shuffler.pickPairFromMapofLists(inDto.trials),
                secret = shuffler.pickPairFromMapofLists(inDto.secrets)
        )
    }
}
class FpArtifactView : ViewTransformStrategy<FpArtifactDto, HTML> {
    override fun transform(outData: FpArtifactDto): HTML {
        return html {
            body {
                h4 { + outData.config.headers.main }
                h5 { + outData.config.headers.origin }
                p {
                    strong { small { + outData.origin.first } }
                    + "- ${outData.origin.second}"
                }
                h5 { + outData.config.headers.pow }
                p {
                    strong { small { + outData.power.first } }
                    + "- ${outData.power.second}"
                }
            }
        }
    }
}

class FpMonsterView : ViewTransformStrategy<FpMonsterDto, HTML> {
    override fun transform(outData: FpMonsterDto): HTML {
        return html {
            body {
                h4 { + outData.config.headers.main }
                h5 { + outData.config.headers.nature }
                p {
                    strong { small { + outData.nature.first } }
                    + "- ${outData.nature.second}"
                }
                h5 { + outData.config.headers.role }
                p {
                    strong { small { + outData.role.first } }
                    + "- ${outData.role.second}"
                }
            }
        }
    }
}
class FpCityView : ViewTransformStrategy<FpCityDto, HTML> {
    override fun transform(outData: FpCityDto): HTML {
        return html {
            body {
                h4 { + outData.config.headers.main }
                h5 { + outData.config.headers.feature }
                p {
                    strong { small { + outData.feature.first } }
                    + "- ${outData.feature.second}"
                }
                h5 { + outData.config.headers.population }
                p {
                    strong { small { + outData.population.first } }
                    + "- ${outData.population.second}"
                }
                h5 { + outData.config.headers.society }
                p {
                    strong { small { + outData.society.first } }
                    + "- ${outData.society.second}"
                }
                h5 { + outData.config.headers.trouble }
                p {
                    strong { small { + outData.trouble.first } }
                    + "- ${outData.trouble.second}"
                }
            }
        }
    }
}

class FpDungeonView : ViewTransformStrategy<FpDungeonDto, HTML> {
    override fun transform(outData: FpDungeonDto): HTML {
        return html {
            body {
                h4 { + outData.config.headers.main }
                h5 { + outData.config.headers.history }
                p {
                    strong { small { + outData.history.first } }
                    + "- ${outData.history.second}"
                }
                h5 { + outData.config.headers.denizen }
                p {
                    strong { small { + outData.denizen.first } }
                    + "- ${outData.denizen.second}"
                }
                h5 { + outData.config.headers.trial }
                p {
                    strong { small { + outData.trial.first } }
                    + "- ${outData.trial.second}"
                }
                h5 { + outData.config.headers.secret }
                p {
                    strong { small { + outData.secret.first } }
                    + "- ${outData.secret.second}"
                }
            }
        }
    }
}

class FpArtifactLoader : DataLoadingStrategy<FpArtifactInputDto> {
    override fun load(): FpArtifactInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_artifact, FpArtifactInputDto::class.java)
    }
}

class FpCityLoader : DataLoadingStrategy<FpCityInputDto> {
    override fun load(): FpCityInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_city, FpCityInputDto::class.java)
    }
}
class FpDungeonLoader : DataLoadingStrategy<FpDungeonInputDto> {
    override fun load(): FpDungeonInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_dungeon, FpDungeonInputDto::class.java)
    }
}
class FpMonsterLoader : DataLoadingStrategy<FpMonsterInputDto> {
    override fun load(): FpMonsterInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_monster, FpMonsterInputDto::class.java)
    }
}

class FourthPageArtifactPipeline(val shuffler: Shuffler = Shuffler()) : GeneratorPipeline<FpArtifactInputDto, FpArtifactDto, HTML>(
        FpArtifactLoader(),
        FpArtifactGenerator(shuffler),
        FpArtifactView()
)

class FourthPageCityPipeline(val shuffler: Shuffler = Shuffler()) : GeneratorPipeline<FpCityInputDto, FpCityDto, HTML>(
        FpCityLoader(),
        FpCityGenerator(shuffler),
        FpCityView()
)

class FourthPageDungeonPipeline(val shuffler: Shuffler = Shuffler()) : GeneratorPipeline<FpDungeonInputDto, FpDungeonDto, HTML>(
        FpDungeonLoader(),
        FpDungeonGenerator(shuffler),
        FpDungeonView()
)
class FourthPageMonsterPipeline(val shuffler: Shuffler = Shuffler()) : GeneratorPipeline<FpMonsterInputDto, FpMonsterDto, HTML>(
        FpMonsterLoader(),
        FpMonsterGenerator(shuffler),
        FpMonsterView()
)




