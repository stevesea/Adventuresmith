/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stevesea.rpg_boy2000.data.maze_rats

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.stevesea.rpg_boy2000.data.Shuffler

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@CompileStatic
@RunWith(MockitoJUnitRunner)
class MazeRatsGeneratorTest {

    @Mock
    Random mockRandom

    Shuffler shuffler

    @Before
    void setup() {
        // set the shuffler up so that it's always just returning the first item
        shuffler = new Shuffler(mockRandom);
        when(mockRandom.nextInt(any(Integer.class))).thenReturn(0)
    }
    @Test
    void testGenerators() {
        assertEquals("Arc Acid", new MazeRatsMagic(shuffler).generate())
        assertEquals("Arc Amulet", new MazeRatsItems(shuffler).generate())
        assertEquals("Arc Ant", new MazeRatsMonsters(shuffler).generate())
        assertEquals("Ages backwards", new MazeRatsAfflictions(shuffler).generate())
        assertEquals("1-hour vampirism", new MazeRatsPotionEffects(shuffler).generate())
        assertEquals("<strong><small>Name</small>: <em>Adelaide Barrow</em></strong>\n" +
                "<br/>&nbsp;&nbsp;<small>STR: 3 DEX: 3 WIL: 3</small>\n" +
                "<br/>&nbsp;&nbsp;<small>HP: 1</small>\n" +
                "<br/><strong><small>Personality</small></strong>: Avant-Garde, Boastful\n" +
                "<br/><strong><small>Appearance</small></strong>: Battle Scars, Boney hands\n" +
                "<br/><strong><small>Weapons</small></strong>: Arming Sword (d6), Battered Halberd (d8)\n" +
                "<br/><strong><small>Equip</small></strong>: Animal Scent, Antitoxin, Armor", new MazeRatsCharacter(shuffler).generate())
    }
}
