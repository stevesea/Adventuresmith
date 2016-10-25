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
package org.stevesea.adventuresmith.data

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.stevesea.adventuresmith.data.perilous_wilds.PwCreature
import org.stevesea.adventuresmith.data.perilous_wilds.PwDanger
import org.stevesea.adventuresmith.data.perilous_wilds.PwDetails
import org.stevesea.adventuresmith.data.perilous_wilds.PwDiscovery
import org.stevesea.adventuresmith.data.perilous_wilds.PwDungeon
import org.stevesea.adventuresmith.data.perilous_wilds.PwDungeonDiscoveryAndDanger
import org.stevesea.adventuresmith.data.perilous_wilds.PwFollower
import org.stevesea.adventuresmith.data.perilous_wilds.PwNPC
import org.stevesea.adventuresmith.data.perilous_wilds.PwNames
import org.stevesea.adventuresmith.data.perilous_wilds.PwPlace
import org.stevesea.adventuresmith.data.perilous_wilds.PwRegion
import org.stevesea.adventuresmith.data.perilous_wilds.PwSteading
import org.stevesea.adventuresmith.data.perilous_wilds.PwTreasure
import org.stevesea.adventuresmith.data.perilous_wilds.PwTreasureGuarded

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
        assertEquals("Ageless Bay", new PwRegion().withShuffler(shuffler).generate())
        assertEquals("The Barrier", new PwPlace().withShuffler(shuffler).generate())
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
""", new PwDetails().withShuffler(shuffler).generate())
    }

    @Test
    void testNPC() {
        assertEquals("""\
<strong>NPC: Wilderness</strong>
<br/>
<br/><strong><small>Occupation:</small></strong> Criminal: bandit/brigand/thug
<br/><strong><small>Activity:</small></strong> laying trap/ambush
<br/><strong><small>Alignment:</small></strong> Chaotic
<br/><br/><strong><small>Physical Appearance:</small></strong> disfigured (missing teeth, eye, etc.)
<br/><strong><small>Personality:</small></strong> loner/alienated/antisocial
<br/><strong><small>Quirk:</small></strong> insecure/racist/xenophobic\
""", new PwNPC().withShuffler(shuffler).generate())
    }

    @Test
    void testFollower() {
        assertEquals("""\
<strong>Follower</strong>
<br/>
<br/><strong><small>Competence:</small></strong> A liability
<br/><strong><small>Background:</small></strong> Life of servitude/oppression
<br/><strong><small>Tags:</small></strong>
<br/>&nbsp;&nbsp;?-Wise,<br/>&nbsp;&nbsp;Meek
<br/><strong><small>Quality:</small></strong> -1&nbsp;&nbsp;<strong><small>Loyalty:</small></strong> 0&nbsp;&nbsp;<strong><small>Cost:</small></strong> Debauchery
<br/>
<br/><strong><small>Instinct:</small></strong> Loot, pillage, and burn
<br/>
<br/><strong><small>Hit Points:</small></strong> 3HP (weak/frail/soft)
<br/><strong><small>Damage Die:</small></strong> d4 (Not very dangerous)
<br/>
<br/><strong><small>Physical Appearance:</small></strong> disfigured (missing teeth, eye, etc.)
<br/><strong><small>Personality:</small></strong> loner/alienated/antisocial
<br/><strong><small>Quirk:</small></strong> insecure/racist/xenophobic\
""", new PwFollower().withShuffler(shuffler).generate())
    }

    @Test
    void testCreature() {
        assertEquals("""\
<strong>Beast</strong> -- <small>Start with a real-world creature, then put a spin on it.</small>
<br/>
<br/>termite/tick/louse
<br/>
<br/><strong><small>Activity:</small></strong> laying trap/ambush
<br/><strong><small>Disposition:</small></strong> attacking
<br/><strong><small>No. Appearing:</small></strong> Solitary (1)
<br/><strong><small>Size:</small></strong> Tiny\
""", new PwCreature().withShuffler(shuffler).generate())
    }

    @Test
    void testDiscovery() {
        assertEquals("""\
<strong>Unnatural Feature</strong> -- <small>How does it affect its surroundings?</small>
<br/>
<br/><strong>Arcane</strong>
<br/>residue
<br/><strong><small>Alignment:</small></strong> Chaotic
<br/><strong><small>Magic Type:</small></strong> divination""", new PwDiscovery().withShuffler(shuffler).generate())
    }
    @Test
    void testSteading() {
        assertEquals("""\
<strong>Village</strong>
<br/>
<br/><strong><small>Built by:</small></strong> human
<br/>Natural defenses
<br/>Surrounded by arid or uncultivable land
<br/>
<br/><strong><small>Prosperity:</small></strong> Poor
<br/><strong><small>Population:</small></strong> Steady
<br/><strong><small>Defenses:</small></strong> Militia
<br/>
<br/>Need: food<br/>Oath: other steading<br/>Resource: game/fruit/vegetable<br/>Safe""", new PwSteading().withShuffler(shuffler).generate())
    }

    @Test
    void testDanger() {
        assertEquals("""\
<strong>Unnatural Entity</strong>
<br/>
<br/><strong><small>Undead</small></strong> - haunt/wisp
<br/>
<br/><strong><small>Ability:</small></strong> bless/curse
<br/><strong><small>Activity:</small></strong> laying trap/ambush
<br/><strong><small>Alignment:</small></strong> Chaotic
<br/><strong><small>Disposition:</small></strong> attacking""", new PwDanger().withShuffler(shuffler).generate())
    }

    @Test
    void testTreasure() {
        assertEquals("""\
A few coins, 2d8 or so
&nbsp;&nbsp;<small>[2]</small>""", new PwTreasure().withShuffler(shuffler).generate())
    }

    @Test
    void testTreasureGuarded() {
        assertEquals("""\
<strong>damage die - D4 + 2d4</strong>
<br/>Several coins, about 4d10
<br/>&nbsp;&nbsp;<small>[4]</small>
<br/>
<br/><strong>damage die - D6 + 2d4</strong>
<br/>Several coins, about 4d10
<br/>&nbsp;&nbsp;<small>[4]</small>
<br/>
<br/><strong>damage die - D8 + 2d4</strong>
<br/>Several coins, about 4d10
<br/>&nbsp;&nbsp;<small>[4]</small>
<br/>
<br/><strong>damage die - D10 + 2d4</strong>
<br/>Several coins, about 4d10
<br/>&nbsp;&nbsp;<small>[4]</small>
<br/>
<br/><strong>damage die - D12 + 2d4</strong>
<br/>Several coins, about 4d10
<br/>&nbsp;&nbsp;<small>[4]</small>""", PwTreasureGuarded.gg.get('2bonus').withShuffler(shuffler).generate())
    }

    @Test
    void testDungeon() {
        assertEquals("""\
<strong>Dungeon</strong>
<br/>
<br/><strong><small>Size:</small></strong> Small &nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Area Limit:</small></strong> 3
<br/><strong><small>Builder:</small></strong> aliens/precursors
<br/><strong><small>Function:</small></strong> source/portal
<br/>
<br/><strong><small>Ruination:</small></strong> arcane disaster
<br/>
<br/><strong>Themes:</strong>
<br/>&nbsp;&nbsp;rot/decay: &nbsp;O""", new PwDungeon().withShuffler(shuffler).generate())
    }

    @Test
    void exerciser() {

        // run through the generators
        def gens = [
                new PwCreature(),
                new PwDanger(),
                new PwDiscovery(),
                new PwDungeon(),
                new PwDungeonDiscoveryAndDanger(),
                new PwFollower(),
                new PwNames(),
                new PwNPC(),
                new PwPlace(),
                new PwRegion(),
                new PwSteading(),
                new PwTreasure(),
                new PwTreasureGuarded(),
                ]
        gens.each { gen ->
            //println "################"
            100.times {
                //println "-------"
                /*println*/ gen.generate()
            }
        }
    }

}
