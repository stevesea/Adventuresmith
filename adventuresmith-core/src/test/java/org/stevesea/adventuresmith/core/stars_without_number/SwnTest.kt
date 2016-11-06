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
import java.util.*


class SwnTest {

    @Test
    fun dressing() {
        Assert.assertEquals("Armory: locked gun cabinets, armor racks",
                getGenerator(SwnConstants.ROOM_DRESSING, 0).generate(Locale.US))
    }
    @Test
    fun arch() {
        Assert.assertEquals("""
        <h4>Architectural Elements</h4>

        Square foundations<br/>Bas-relief on walls
        <br/>Canals and pools
        """.trimIndent(), getGenerator(SwnConstants.ARCHITECTURE, 0).generate(Locale.US))
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
        No universal leadership; no regional hierarchy
        """.trimIndent(), getGenerator(SwnConstants.RELIGION, 5).generate(Locale.US))
        // picked 5 for Random because that'll exercise one of the options that'll send us
        // through another pass of the template processor
    }

    @Test
    fun alien() {
        Assert.assertEquals("""
        <h4>Alien</h4>
        <h5>Body Type</h5>
        Humanlike
        <h5>Lenses</h5>
        Curiosity<br/>Despair
        <h5>Social Structure</h5>
        Democratic
        """.trimIndent(), getGenerator(SwnConstants.ALIEN, 0).generate(Locale.US))
    }

    @Test
    fun animal() {
        Assert.assertEquals("""
        <h4>Animal</h4>
        Insectile<br/>&nbsp;&nbsp;Chewing mouthparts<br/>&nbsp;&nbsp;Jewel-colored chitin
        """.trimIndent(), getGenerator(SwnConstants.ANIMAL, 0).generate(Locale.US))
    }

    @Test
    fun corp() {
        Assert.assertEquals("""
        <h4>Corporation</h4>
        Ad Astra Alliance
        <h5>Businesses</h5>
        Agriculture<br/>Art
        <h5>Rumors</h5>
        Reckless with the lives of their employees<br/>Reckless with the lives of their employees
        """.trimIndent(), getGenerator(SwnConstants.CORPORATION, 0).generate(Locale.US))
    }
    @Test
    fun heresy() {
        Assert.assertEquals("""
        <h4>Heresy</h4>
        <h5>Founder</h5>
        <em>Defrocked clergy:</em> founded by a cleric outcast from the faith
        <h5>Major Heresy:</h5>
        <em>Manichaeanism:</em> the sect believes in harsh austerities and rejection of matter as something profane and evil
        <h5>Attitude towards orthodoxy</h5>
        <em>Filial:</em> the sect honors and respects the orthodox faith, but feels it is substantially in error
        <h5>Quirks</h5>
        Dietary prohibitions<br/>Characteristic item of clothing or jewelry
        """.trimIndent(), getGenerator(SwnConstants.HERESY, 0).generate(Locale.US))
    }
}
