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

package org.stevesea.adventuresmith.data

import android.content.Context
import android.support.annotation.RawRes
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

@CompileStatic
abstract class AbstractDataDrivenGenerator extends AbstractGenerator {
    Charset charset = StandardCharsets.UTF_8
    JsonSlurper slurper = new JsonSlurper()
    Context context
    @RawRes
    int rawResId


    AbstractDataDrivenGenerator withContext(Context context) {
        this.context = context;
        this
    }

    AbstractDataDrivenGenerator withShuffler(Shuffler shuff) {
        this.shuffler = shuff
        this
    }

    AbstractDataDrivenGenerator withRawResource(@RawRes int id) {
        rawResId = id
        this
    }

    AbstractDataDrivenGenerator withSlurper(JsonSlurper slurper) {
        this.slurper = slurper;
        this
    }

    AbstractDataDrivenGenerator withCharset(Charset cs) {
        this.charset = cs
        this
    }

    /**
     *
     * @return a data structure of lists and maps
     */
    def parseResource() {
        assert context != null
        slurper.parse(new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawResId), charset)))
    }


}
