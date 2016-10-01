/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.stevesea.rpgpad.data

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.stevesea.rpgpad.data.perilous_wilds.PwDetails
import org.stevesea.rpgpad.data.perilous_wilds.PwNPC
import org.stevesea.rpgpad.data.perilous_wilds.PwPlace
import org.stevesea.rpgpad.data.perilous_wilds.PwRegion

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@CompileStatic
@RunWith(MockitoJUnitRunner)
class PerilousWildsGeneratorTest {

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
        assertEquals("Ageless Bay", new PwRegion(shuffler).generate())
        assertEquals("The Barrier", new PwPlace(shuffler).generate())
    }

    @Test
    void testDetails() {
        assertEquals("""\
<br/><strong><small>Ability: </small></strong>bless/curse
<br/><strong><small>Activity: </small></strong>laying trap/ambush
<br/><strong><small>Adjective: </small></strong>slick/slimy
<br/><strong><small>Age: </small></strong>being born/built
<br/><strong><small>Aspect: </small></strong>power/strength
<br/><strong><small>Condition: </small></strong>being built/born
<br/><strong><small>Disposition: </small></strong>attacking
<br/><strong><small>Element: </small></strong>air
<br/><strong><small>Feature: </small></strong>heavily armored
<br/><strong><small>Magic Type: </small></strong>divination
<br/><strong><small>No. Appearing: </small></strong>Solitary (1)
<br/><strong><small>Oddity: </small></strong>weird color/smell/sound
<br/><strong><small>Orientation: </small></strong>down/earthward
<br/><strong><small>Ruination: </small></strong>arcane disaster
<br/><strong><small>Size: </small></strong>Tiny
<br/><strong><small>Tag: </small></strong>Amorphous
<br/><strong><small>Terrain: </small></strong>wasteland/desert
<br/><strong><small>Visbility: </small></strong>buried/camouflaged/nigh invisible\
""", new PwDetails(shuffler).generate())
    }

    @Test
    void testNPC() {
        assertEquals("""\
<h4>Wilderness</h4>
<br/><strong>Occupation</strong>: Criminal: bandit/brigand/thug
<br/><strong>Activity</strong>: laying trap/ambush
<br/><strong>Alignment</strong>: Chaotic
<br/><br/>Physical Appearance: disfigured (missing teeth, eye, etc.)
<br/>Personality: loner/alienated/antisocial
<br/>Quirk: insecure/racist/xenophobic
<h4>Rural</h4>
<br/><strong>Occupation</strong>: beggar/urchin
<br/><strong>Activity</strong>: laying trap/ambush
<br/><strong>Alignment</strong>: Chaotic
<br/><br/>Physical Appearance: disfigured (missing teeth, eye, etc.)
<br/>Personality: loner/alienated/antisocial
<br/>Quirk: insecure/racist/xenophobic
<h4>Urban</h4>
<br/><strong>Occupation</strong>: beggar/urchin
<br/><strong>Activity</strong>: laying trap/ambush
<br/><strong>Alignment</strong>: Chaotic
<br/><br/>Physical Appearance: disfigured (missing teeth, eye, etc.)
<br/>Personality: loner/alienated/antisocial
<br/>Quirk: insecure/racist/xenophobic\
""", new PwNPC(shuffler, new PwDetails(shuffler)).generate())
    }

    @Test
    void doit() {
        def templates = new RangeMap()
            .with((1..5), "asdf ${ -> Dice.roll('1d12')}")
            .with((6..9), "sdfg ${ -> Dice.roll('1d12')}")
        println templates
    }



}
