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
        Assert.assertEquals("Always honest", MrAfflictionsGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Bleeds seawater", MrAfflictionsGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Caveman speech", MrAfflictionsGenerator(GeneratorTest.getShuffler(8)).generate())
        Assert.assertEquals("Inner meltdown", MrAfflictionsGenerator(GeneratorTest.getShuffler(16)).generate())
    }
    @Test
    fun potion_effects() {
        Assert.assertEquals("Alter face", MrPotionEffectsGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Anti-gravity", MrPotionEffectsGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Blurry outlines", MrPotionEffectsGenerator(GeneratorTest.getShuffler(8)).generate())
        Assert.assertEquals("Control element", MrPotionEffectsGenerator(GeneratorTest.getShuffler(16)).generate())
    }
    @Test
    fun magic() {
        Assert.assertEquals("Accelerating Aether", MrMagicGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Amber of Beacon", MrMagicGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Attracting Aura", MrMagicGenerator(GeneratorTest.getShuffler(2)).generate())
        Assert.assertEquals("Ash of Binding Beam", MrMagicGenerator(GeneratorTest.getShuffler(5)).generate())
    }
    @Test
    fun item() {
        Assert.assertEquals("Accelerating Arrow", MrItemGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Bell of Bewildering Beacon", MrItemGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Attracting Aura Arrowhead", MrItemGenerator(GeneratorTest.getShuffler(2)).generate())
        Assert.assertEquals("Ash Belt", MrItemGenerator(GeneratorTest.getShuffler(5)).generate())
    }
    @Test
    fun creature() {
        Assert.assertEquals("Accelerating Assassin Ant", MrCreatureGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Ant Badger", MrCreatureGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Attracting Ant", MrCreatureGenerator(GeneratorTest.getShuffler(2)).generate())
        Assert.assertEquals("Binding Ant Badger", MrCreatureGenerator(GeneratorTest.getShuffler(5)).generate())
    }
    @Test
    fun char() {
        Assert.assertEquals("asdf", MrCharacterGenerator(GeneratorTest.getShuffler()).generate())
    }

}
