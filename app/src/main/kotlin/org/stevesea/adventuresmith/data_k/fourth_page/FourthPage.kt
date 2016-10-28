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
import java.nio.charset.*


data class FourthPageArtifactInputDto(val origins: Map<String,List<String>>, val powers: Map<String,List<String>>)
data class FourthPageArtifactDto(val map: Map<String,String>){
    val origin: String by map
    val pow: String by map
    val originVal: String by map
    val powVal: String by map
}

class FpArtifactGenerator(val shuffler: Shuffler<String>) : GeneratorTransformStrategy<FourthPageArtifactInputDto, FourthPageArtifactDto> {
    override fun transform(inDto: FourthPageArtifactInputDto): FourthPageArtifactDto {
        val okey = shuffler.pick(inDto.origins.keys)
        val pkey = shuffler.pick(inDto.powers.keys)
        return FourthPageArtifactDto(mapOf(
                "origin" to okey,
                "originVal" to shuffler.pick(inDto.origins[okey]!!),
                "power" to pkey,
                "powerVal" to shuffler.pick(inDto.origins[pkey]!!)
        ))
    }
}
class FpArtifactView : ViewTransformStrategy<FourthPageArtifactDto, String> {
    override fun transform(outData: FourthPageArtifactDto): String {
        return html {
            body {
                h4 { + "Artifact" } // TODO: read headings from strings
                h5 { + "Origin"}
                p {
                    strong { small { + outData.origin } }
                    + "- ${outData.originVal}"
                }
                h5 { + "Power"}
                p {
                    strong { small { + outData.pow } }
                    + "- ${outData.powVal}"
                }
            }
        }.toString()
    }
}

class FpArtifactLoader :
        RawResourceLoader<FourthPageArtifactInputDto>(
                R.raw.fourth_page_artifact, StandardCharsets.UTF_8) {

    // TODO: Having trouble with reified generic. feels like we should be able to stick
    //  this in base class
    val cached : FourthPageArtifactInputDto by lazy {
        open(resId).bufferedReader(charset).use {
            MapperProvider.getReader().readValue<FourthPageArtifactInputDto>(it)
        }
    }

    override fun getResource() : FourthPageArtifactInputDto{
        return cached
    }
}

class FourthPageArtifactPipeline(val shuffler: Shuffler<String> = Shuffler()) : GeneratorPipeline
    <FourthPageArtifactInputDto, FourthPageArtifactDto, String>(
        FpArtifactLoader(),
        FpArtifactGenerator(shuffler),
        FpArtifactView()
)





