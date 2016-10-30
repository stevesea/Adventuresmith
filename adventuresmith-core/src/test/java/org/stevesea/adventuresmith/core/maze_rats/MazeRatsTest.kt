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
        Assert.assertEquals("Accelerating Assassin Ant", MrMonsterGenerator(GeneratorTest.getShuffler()).generate())
        Assert.assertEquals("Ant Badger", MrMonsterGenerator(GeneratorTest.getShuffler(4)).generate())
        Assert.assertEquals("Attracting Ant", MrMonsterGenerator(GeneratorTest.getShuffler(2)).generate())
        Assert.assertEquals("Binding Ant Badger", MrMonsterGenerator(GeneratorTest.getShuffler(5)).generate())
    }
    @Test
    fun char() {
        Assert.assertEquals("""
<html>
  <body>
    <p>
      <strong>
        <small>
          Name
        </small>
      </strong>
      Adelaide Barrow
    </p>
    <p>
      <small>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;STR: 3 DEX: 3 WIL: 3 &nbsp;&nbsp;&nbsp;&nbsp;HP: 1
      </small>
    </p>
    <p>
      <strong>
        <small>
          Personality
        </small>
      </strong>
      Avant-Garde, Boastful
    </p>
    <p>
      <strong>
        <small>
          Appearance
        </small>
      </strong>
      Battle Scars, Boney hands
    </p>
    <p>
      <strong>
        <small>
          Weapons
        </small>
      </strong>
      Arming Sword (d6), Battered Halberd (d8)
    </p>
    <p>
      <strong>
        <small>
          Equipment
        </small>
      </strong>
      Animal Scent, Antitoxin, Armor
    </p>
  </body>
</html>
        """.trim(), MrCharacterGenerator(GeneratorTest.getShuffler(0)).generate())
    }

    @Test
    fun exerciser() {
        for(i in 1..50) {
            for (g in listOf(MrItemGenerator(), MrAfflictionsGenerator(), MrPotionEffectsGenerator(),
                    MrMonsterGenerator(), MrMagicGenerator())) {
                println(g.generate())
            }
        }

    }

}
