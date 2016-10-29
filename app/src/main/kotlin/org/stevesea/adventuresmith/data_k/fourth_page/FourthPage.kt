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

package org.stevesea.adventuresmith.data_k.fourth_page

import org.stevesea.adventuresmith.*
import org.stevesea.adventuresmith.data_k.*
import org.stevesea.adventuresmith.html_builder.*

/*
seems neat... but let's do for different class
data class FourthPageArtifactDto(val map: Map<String,String>){
    val origin: String by map
    val pow: String by map
    val originVal: String by map
    val powVal: String by map
}

usage:
        return FourthPageArtifactDto(mapOf(
                "origin" to okey,
                "originVal" to shuffler.pick(inDto.origins[okey]!!),
                "pow" to pkey,
                "powVal" to shuffler.pick(inDto.powers[pkey]!!)
        ))
*/

data class FpArtifactHeaders(val main: String, val origin: String, val pow: String)
data class FourthPageArtifactConfigDto(val headers: FpArtifactHeaders)
data class FourthPageArtifactInputDto(
        val config: FourthPageArtifactConfigDto,
        val origins: Map<String,List<String>>,
        val powers: Map<String,List<String>>)
data class FourthPageArtifactDto(
        val config: FourthPageArtifactConfigDto,
        val origin: Pair<String,String>,
        val power: Pair<String, String>)

data class FpCityHeaders(val main: String, val feature: String, val population: String, val society: String, val trouble: String)
data class FourthPageCityConfigDto(val headers: FpCityHeaders)
data class FourthPageCityInputDto(val config: FourthPageCityConfigDto,
                                  val features: Map<String,List<String>>,
                                  val populations: Map<String,List<String>>,
                                  val societies: Map<String,List<String>>,
                                  val troubles: Map<String,List<String>>)
data class FourthPageCityDto(val config: FourthPageCityConfigDto,
                             val feature: Pair<String,String>,
                             val population: Pair<String,String>,
                             val society: Pair<String,String>,
                             val trouble: Pair<String, String>)

class FpArtifactGenerator(val shuffler: Shuffler<String>) : GeneratorTransformStrategy<FourthPageArtifactInputDto, FourthPageArtifactDto> {
    override fun transform(inDto: FourthPageArtifactInputDto): FourthPageArtifactDto {
        val okey = shuffler.pick(inDto.origins.keys)
        val pkey = shuffler.pick(inDto.powers.keys)
        return FourthPageArtifactDto(
                config = inDto.config,
                origin = Pair(okey, shuffler.pick(inDto.origins.getOrElse(okey) {listOf("not found")})),
                power = Pair(pkey, shuffler.pick(inDto.powers.getOrElse(pkey) {listOf("not found")}))
        )
    }
}
class FpCityGenerator(val shuffler: Shuffler<String>) : GeneratorTransformStrategy<FourthPageCityInputDto, FourthPageCityDto> {
    override fun transform(inDto: FourthPageCityInputDto): FourthPageCityDto {
        val fkey = shuffler.pick(inDto.features.keys)
        val pkey = shuffler.pick(inDto.populations.keys)
        val skey = shuffler.pick(inDto.societies.keys)
        val tkey = shuffler.pick(inDto.troubles.keys)
        return FourthPageCityDto(
                config = inDto.config,
                feature = Pair(fkey, shuffler.pick(inDto.features.getOrElse(fkey) {listOf("not found")})),
                population = Pair(pkey, shuffler.pick(inDto.populations.getOrElse(pkey) {listOf("not found")})),
                society = Pair(skey, shuffler.pick(inDto.societies.getOrElse(skey) {listOf("not found")})),
                trouble = Pair(tkey, shuffler.pick(inDto.troubles.getOrElse(tkey) {listOf("not found")}))
        )
    }
}
class FpArtifactView : ViewTransformStrategy<FourthPageArtifactDto, HTML> {
    override fun transform(outData: FourthPageArtifactDto): HTML {
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
class FpCityView : ViewTransformStrategy<FourthPageCityDto, HTML> {
    override fun transform(outData: FourthPageCityDto): HTML {
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

class FpArtifactLoader : DataLoadingStrategy<FourthPageArtifactInputDto>{
    override fun load(): FourthPageArtifactInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_artifact, FourthPageArtifactInputDto::class.java)
    }
}

class FpCityLoader : DataLoadingStrategy<FourthPageCityInputDto>{
    override fun load(): FourthPageCityInputDto {
        return RawResourceDeserializer.cachedDeserialize(R.raw.fourth_page_city, FourthPageCityInputDto::class.java)
    }
}

class FourthPageArtifactPipeline(val shuffler: Shuffler<String> = Shuffler()) : GeneratorPipeline
    <FourthPageArtifactInputDto, FourthPageArtifactDto, HTML>(
        FpArtifactLoader(),
        FpArtifactGenerator(shuffler),
        FpArtifactView()
)

class FourthPageCityPipeline(val shuffler: Shuffler<String> = Shuffler()) : GeneratorPipeline
<FourthPageCityInputDto, FourthPageCityDto, HTML>(
        FpCityLoader(),
        FpCityGenerator(shuffler),
        FpCityView()
)





