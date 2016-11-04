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
        Judaism
        <h5>Evolution</h5>
        Schism; the faith's beliefs are actually almost identical to those of the majority of its origin tradition, save for a few minor points of vital interest to theologians and no practical difference whatsoever to believers. This does not prevent a burning resentment towards the parent faith.
        <h5>Leadership</h5>
        No hierarchy; Each region governs itself via [Council]
        """.trimIndent().trim(), getGenerator(SwnConstants.RELIGION, 5).generate())
        // picked 5 for Random because that'll exercise one of the options that'll send us
        // through another pass of the template processor
    }
}
