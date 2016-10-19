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

package org.stevesea.rpgpad.data

import org.junit.Test
import org.stevesea.rpgpad.data.stars_without_number.SWNAlien
import org.stevesea.rpgpad.data.stars_without_number.SWNAnimal
import org.stevesea.rpgpad.data.stars_without_number.SWNWorld
import org.stevesea.rpgpad.data.stars_without_number.SWNarchitecture
import org.stevesea.rpgpad.data.stars_without_number.SWNcorporation
import org.stevesea.rpgpad.data.stars_without_number.SWNheresy
import org.stevesea.rpgpad.data.stars_without_number.SWNnames
import org.stevesea.rpgpad.data.stars_without_number.SWNnpc
import org.stevesea.rpgpad.data.stars_without_number.SWNpolitical_party
import org.stevesea.rpgpad.data.stars_without_number.SWNreligion
import org.stevesea.rpgpad.data.stars_without_number.SWNroom_dressing

class SWNTest {

    @Test
    void exerciser() {

        // run through the generators
        def gens = [
                new SWNAlien(),
                new SWNAnimal(),
                new SWNarchitecture(),
                new SWNcorporation(),
                new SWNheresy(),
                new SWNnames(),
                new SWNnpc(),
                new SWNpolitical_party(),
                new SWNreligion(),
                new SWNroom_dressing(),
                new SWNWorld(),
        ]
        gens.each { gen ->
            //println "################"
            100.times {
                //println "-------"
                /*println*/ gen.generate()
            }
        }
    }
}
