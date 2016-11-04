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

package org.stevesea.adventuresmith.core.fourth_page

import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class FpGeneratorTest {

    @Test
    fun testFpArtifact() {
        Assert.assertEquals("""
<h4>Artifact</h4>
<h5>Origin</h5>
Magic - It's powered by a trapped soul.
<h5>Power</h5>
Curse - You've been polymorphed.
""".trim(), getGenerator(FpConstants.ARTIFACT, 1).generate(Locale.ENGLISH))
    }

    @Test
    fun testFpDungeon() {
        Assert.assertEquals("""
<h4>Dungeon</h4>
<h5>History</h5>
Downfall - The war left the land in ruins.
<h5>Denizen</h5>
Creature - A dragon has made this its lair.
<h5>Trial</h5>
Hazard - The air is filled with poison spores.
<h5>Secret</h5>
Lore - There's something strange about this map.
""".trim(), getGenerator(FpConstants.DUNGEON, 1).generate(Locale.ENGLISH))
    }

    @Test
    fun testFpCity() {
        Assert.assertEquals("""
<h4>City</h4>
<h5>Feature</h5>
Resource - There's good hunting here.
<h5>Population</h5>
Caste - You never know what the mages are up to.
<h5>Society</h5>
Guild - There's no honour amongst these thieves.
<h5>Trouble</h5>
Threat - The sick have been quarantined.
""".trim(),  getGenerator(FpConstants.CITY, 1).generate(Locale.ENGLISH))

    }

    @Test
    fun testFpMonster() {
        Assert.assertEquals("""
<h4>Monster</h4>
<h5>Nature</h5>
Construct - They repair their own injuries.
<h5>Role</h5>
Brute - It gets in your way.
""".trim(),  getGenerator(FpConstants.MONSTER, 1).generate(Locale.ENGLISH))

    }

}

