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

data class FourthPageArtifactConfigDto(val header: String, val origin_header: String, val power_header: String)
data class FourthPageArtifactInputDto(val config: FourthPageArtifactConfigDto, val origins: Map<String,List<String>>, val powers: Map<String,List<String>>)
data class FourthPageArtifactDto(val origin: Pair<String,String>, val power: Pair<String, String>)

class FpArtifactGenerator(val shuffler: Shuffler<String>) : GeneratorTransformStrategy<FourthPageArtifactInputDto, FourthPageArtifactDto> {
    override fun transform(inDto: FourthPageArtifactInputDto): FourthPageArtifactDto {
        val okey = shuffler.pick(inDto.origins.keys)
        val pkey = shuffler.pick(inDto.powers.keys)
        return FourthPageArtifactDto(
                Pair(okey, shuffler.pick(inDto.origins.getOrElse(okey) {listOf("not found")})),
                Pair(pkey, shuffler.pick(inDto.powers.getOrElse(pkey) {listOf("not found")}))
        )
    }
}
class FpArtifactView : ViewTransformStrategy<FourthPageArtifactInputDto, FourthPageArtifactDto, HTML> {
    override fun transform(inData: FourthPageArtifactInputDto, outData: FourthPageArtifactDto): HTML {
        return html {
            body {
                h4 { + inData.config.header } // TODO: read headings from strings
                h5 { + inData.config.origin_header}
                p {
                    strong { small { + outData.origin.first } }
                    + "- ${outData.origin.second}"
                }
                h5 { + inData.config.power_header}
                p {
                    strong { small { + outData.power.first } }
                    + "- ${outData.power.second}"
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

class FourthPageArtifactPipeline(val shuffler: Shuffler<String> = Shuffler()) : GeneratorPipeline
    <FourthPageArtifactInputDto, FourthPageArtifactDto, HTML>(
        FpArtifactLoader(),
        FpArtifactGenerator(shuffler),
        FpArtifactView()
)





