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

package org.stevesea.rpgpad

import com.samskivert.mustache.Mustache
import groovy.transform.Canonical
import org.junit.Test
import org.stevesea.rpgpad.data.RangeMap

class MustacheTest {
    @Canonical
    static class Thingy  {
        String str1
        String str2

        RangeMap activity = new RangeMap()
                .with(1, 'laying trap/ambush')
                .with(2, 'fighting/at war')
                .with(3, 'prowling/on patrol')
                .with(4, 'hunting/foraging')
                .with(5, 'eating/resting')
                .with(6, 'crafting/praying')
                .with(7, 'traveling/relocating')
                .with(8, 'exploring/lost')
                .with(9, 'returning home')
                .with(10, 'building/excavating')
                .with(11, 'sleeping')
                .with(12, 'dying')

        String getActivity() {
            return "choice!"
        }

    }


    @Test
    void test() {
        String tmpl = """
{{str1}}
{{str2}}
{{activity}}
"""
        Thingy obj = new Thingy()
        obj.with {
            str1 = "String1Val"
            str2 = "String2Val"
        }
        println Mustache.compiler().compile(tmpl).execute(obj)
    }
}
