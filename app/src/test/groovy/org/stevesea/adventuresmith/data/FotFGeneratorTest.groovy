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
 */

package org.stevesea.adventuresmith.data

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.stevesea.adventuresmith.data.freebooters_on_the_frontier.FotFCharacters
import org.stevesea.adventuresmith.data.freebooters_on_the_frontier.FotFSpells

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@CompileStatic
@RunWith(MockitoJUnitRunner)
class FotFGeneratorTest {

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

        assertEquals("Acid Armor", new FotFSpells().withShuffler(shuffler).generate())

        assertEquals("""\
<strong>Fighter</strong> - Athelan
<br/>Human  male (Evil)
<br/>
<br/><strong><small>Strength:</small></strong> 3&nbsp;&nbsp;<strong><small>Dexterity:</small></strong> 3
<br/><strong><small>Charisma:</small></strong> 3&nbsp;&nbsp;<strong><small>Constitution:</small></strong> 3
<br/><strong><small>Wisdom:</small></strong> 3&nbsp;&nbsp;&nbsp;<strong><small>Intelligence:</small></strong> 3
<br/><strong><small>Luck:</small></strong> 3
<br/>
<br/><strong><small>Appearance:</small></strong> big mouth, big mustache
<br/>
<br/><strong><small>Vices:</small></strong> Aggressive, Alcoholic, Antagonistic

<br/>
<br/><strong><small>Gear:</small></strong>
<br/>&nbsp;&nbsp;Leather armor
<br/>&nbsp;&nbsp;Healing potion\
""", new FotFCharacters().withShuffler(shuffler).generate())
    }
}
