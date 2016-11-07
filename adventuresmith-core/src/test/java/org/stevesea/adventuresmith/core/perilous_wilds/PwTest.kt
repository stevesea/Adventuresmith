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

package org.stevesea.adventuresmith.core.perilous_wilds

import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class PwTest {

    @Test
    fun treasure_item() {
        Assert.assertEquals("key/lockpick".trimIndent(),
                getGenerator(PwConstants.TREASURE_ITEM, 0).generate(Locale.US))
    }

    @Test
    fun treasure_unguarded() {
        Assert.assertEquals("""
        <p>A useful item - potion/food
        <br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Adjective:</small></strong> rough/hard/sharp
        <br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Age:</small></strong> young/recent
        <br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Oddity:</small></strong> geometric</p>
        """.trimIndent(),
                getGenerator(PwConstants.TREASURE_UNGUARDED, 1).generate(Locale.US))
    }
    @Test
    fun npc() {
        Assert.assertEquals("""
        <h4>NPC Wilderness</h4>
        <strong><small>Occupation:</small></strong> Criminal: bandit/brigand/thug
        <br/><strong><small>Activity:</small></strong> fighting/at war
        <br/><strong><small>Alignment:</small></strong> Chaotic
        <br/>
        <br/><strong><small>Physical Appearance:</small></strong> lasting injury (bad leg, arm, etc.)
        <br/><strong><small>Personality:</small></strong> cruel/belligerent/bully
        <br/><strong><small>Quirk:</small></strong> addict (sweets, drugs, sex, etc.)
        """.trimIndent(),
                getGenerator(PwConstants.NPC, 1).generate(Locale.US))
    }
}
