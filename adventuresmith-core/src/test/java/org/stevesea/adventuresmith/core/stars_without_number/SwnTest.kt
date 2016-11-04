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

package org.stevesea.adventuresmith.core.stars_without_number;

import org.junit.*
import org.stevesea.adventuresmith.core.*


class SwnTest {

    @Test
    fun dressing() {
        Assert.assertEquals("Armory: locked gun cabinets, armor racks",
                getGenerator(SwnConstants.ROOM_DRESSING, 0).generate())
    }

    @Test
    fun religion() {
        Assert.assertEquals("""
        <h4>Religion</h4>
        <h5>Origin Tradition</h5>
        Paganism
        <h5>Evolution</h5>
        New holy book; someone in the faith's past penned or discovered a text that is now taken to be holy writ and the expressed will of the divine.
        <h5>Leadership</h5>
        Patriarch/Matriarch; a single leader determines doctrine for the entire religion, possibly in consultation wiht other clerics.
        """.trimIndent().trim(), getGenerator(SwnConstants.RELIGION, 0).generate())
    }
}
