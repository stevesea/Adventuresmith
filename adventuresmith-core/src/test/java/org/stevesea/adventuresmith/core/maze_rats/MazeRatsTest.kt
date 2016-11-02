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

package org.stevesea.adventuresmith.core.maze_rats;

import org.junit.*
import org.stevesea.adventuresmith.core.*


class MazeRatsTest {

    @Test
    fun affliction() {
        Assert.assertEquals("Always honest", getGenerator(MrConstants.AFFLICTIONS, 1).generate())
        Assert.assertEquals("Bleeds seawater", getGenerator(MrConstants.AFFLICTIONS, 4).generate())
        Assert.assertEquals("Caveman speech", getGenerator(MrConstants.AFFLICTIONS, 8).generate())
        Assert.assertEquals("Inner meltdown", getGenerator(MrConstants.AFFLICTIONS, 16).generate())
    }
    @Test
    fun potion_effects() {
        Assert.assertEquals("Alter face", getGenerator(MrConstants.POTION_EFFECTS, 1).generate())
        Assert.assertEquals("Anti-gravity", getGenerator(MrConstants.POTION_EFFECTS, 4).generate())
        Assert.assertEquals("Blurry outlines", getGenerator(MrConstants.POTION_EFFECTS, 8).generate())
        Assert.assertEquals("Control element", getGenerator(MrConstants.POTION_EFFECTS, 16).generate())
    }
    @Test
    fun magic() {
        Assert.assertEquals("Accelerating Aether", getGenerator(MrConstants.MAGIC, 1).generate())
        Assert.assertEquals("Amber of Beacon", getGenerator(MrConstants.MAGIC, 4).generate())
        Assert.assertEquals("Attracting Aura", getGenerator(MrConstants.MAGIC, 2).generate())
        Assert.assertEquals("Ash of Binding Beam", getGenerator(MrConstants.MAGIC, 5).generate())
    }
    @Test
    fun item() {
        Assert.assertEquals("Accelerating Arrow", getGenerator(MrConstants.ITEM, 1).generate())
        Assert.assertEquals("Bell of Bewildering Beacon", getGenerator(MrConstants.ITEM, 4).generate())
        Assert.assertEquals("Attracting Aura Arrowhead", getGenerator(MrConstants.ITEM, 2).generate())
        Assert.assertEquals("Ash Belt", getGenerator(MrConstants.ITEM, 5).generate())
    }
    @Test
    fun monster() {
        Assert.assertEquals("Accelerating Assassin Ape", getGenerator(MrConstants.MONSTER, 1).generate())
        Assert.assertEquals("Bear Bear", getGenerator(MrConstants.MONSTER, 4).generate())
        Assert.assertEquals("Attracting Badger", getGenerator(MrConstants.MONSTER, 2).generate())
        Assert.assertEquals("Binding Beaver Beaver", getGenerator(MrConstants.MONSTER, 5).generate())
    }
    @Test
    fun char() {
        Assert.assertEquals("""
        Name: Adelaide Barrow
        STR: 3 DEX: 3 WIL: 3  HP: 1
        Appearance: Acid Scars, Acid Scars
        Personality: Arrogant, Arrogant
        Weapons: Ancient Spear (d6), Ancient Spear (d6)
        Equipment: Acid, Acid, Acid
        """.trimIndent().trim(), getGenerator(MrConstants.CHAR, 0).generate())
    }
}
