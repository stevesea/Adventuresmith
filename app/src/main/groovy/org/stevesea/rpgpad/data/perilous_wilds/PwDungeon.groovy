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
 *
 */

package org.stevesea.rpgpad.data.perilous_wilds

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap
import org.stevesea.rpgpad.data.Shuffler

@CompileStatic
class PwDungeon extends AbstractGenerator{
    PwCreature pwCreature = new PwCreature()
    PwDetails pwDetails = new PwDetails()
    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwDetails.withShuffler(shuff)
        pwCreature.withShuffler(shuff)
        this
    }

    static enum dungeon_size {
        Small(2, '1d4',  6, '1d6+2'),
        Medium(3, '1d6', 6, '2d6+4'),
        Large(4, '1d6+1',6, '3d6+6'),
        Huge(5, '1d6+2', 6, '4d6+10');

        int theme
        String themeStr
        int areaLimit
        String areaLimitStr

        dungeon_size(int theme, String themeStr, int areaLimit, String areaLimitStr) {
            this.theme = theme
            this.themeStr = themeStr
            this.areaLimit = areaLimit
            this.areaLimitStr = areaLimitStr
        }

        @Override
        public String toString() {
            name()
        }
    }

    // size, themes, areas* (total common and unique)
    RangeMap sizeMap = new RangeMap()
            .with(1..3, dungeon_size.Small)
            .with(4..9, dungeon_size.Medium)
            .with(10..11, dungeon_size.Large)
            .with(12, dungeon_size.Huge)
    RangeMap dungeon_ruination = new RangeMap()
            .with(1, 'arcane disaster')
            .with(2, 'damnation/curse')
            .with(3..4, 'earthquake/fire/flood')
            .with(5..6, 'plague/famine/drought')
            .with(7..8, 'overrun by monsters')
            .with(9..10, 'war/invasion')
            .with(11, 'depleted resources')
            .with(12, 'better prospects elsewhere')
    // who built it, to what end?
    RangeMap foundation_builder = new RangeMap()
            .with(1, 'aliens/precursors')
            .with(2, 'demigod/demon')
            .with(3..4, 'natural (caves, etc.)')
            .with(5, 'religious order/cult')
            .with(6..7, "${ -> pick(pwCreature.humanoid)}")
            .with(8..9, 'dwarves/gnomes')
            .with(10, 'elves')
            .with(11, 'wizard/madman')
            .with(12, 'monarch/warlord')
    RangeMap foundation_function = new RangeMap()
            .with(1, 'source/portal')
            .with(2, 'mine')
            .with(3..4, 'tomb/crypt')
            .with(5, 'prison')
            .with(6..7, 'lair/den/hideout')
            .with(8..9, 'stronghold/sanctuary')
            .with(10, 'shrine/temple/oracle')
            .with(11, 'archive/library')
            .with(12, 'unknown/mystery')
    RangeMap theme_mundane = new RangeMap()
            .with(1, 'rot/decay')
            .with(2, 'torture/agony')
            .with(3, 'madness')
            .with(4, 'all is lost')
            .with(5, 'noble sacrifice')
            .with(6, 'savage fury')
            .with(7, 'survival')
            .with(8, 'criminal activity')
            .with(9, 'secrets/treachery')
            .with(10, 'tricks and traps')
            .with(11, 'invasion/infestation')
            .with(12, 'factions at war')
    RangeMap theme_unusual = new RangeMap()
            .with(1, 'creation/invention')
            .with(2, "${ -> pwDetails.pickElement()}")
            .with(3, 'knowledge/learning')
            .with(4, 'growth/expansion')
            .with(5, 'deepening mystery')
            .with(6, 'transformation/change')
            .with(7, 'chaos and destruction')
            .with(8, 'shadowy forces')
            .with(9, 'forbidden knowledge')
            .with(10, 'poison/disease')
            .with(11, 'corruption/blight')
            .with(12, 'impending disaster')
    RangeMap theme_extraordinary = new RangeMap()
            .with(1, 'scheming evil')
            .with(2, 'divination/scrying')
            .with(3, 'blasphemy')
            .with(4, 'arcane research')
            .with(5, 'occult forces')
            .with(6, 'an ancient curse')
            .with(7, 'mutation')
            .with(8, 'the unquiet dead')
            .with(9, 'bottomless hunger')
            .with(10, 'incredible power')
            .with(11, 'unspeakable horrors')
            .with(12, 'holy war')
    //What’s it all about? Choose or roll according to Dungeon Size.
    RangeMap theme = new RangeMap()
            .with(1..5, "${ -> pick(theme_mundane)}")
            .with(6..9, "${ -> pick(theme_unusual)}")
            .with(10..12, "${ -> pick(theme_extraordinary)}")

    @Override
    String generate() {
        String dsizeStr = (String)pick('1d12', sizeMap)
        def dsize = dungeon_size.valueOf(dsizeStr)
        int numThemes = roll(dsize.themeStr)
        int numAreas = roll(dsize.areaLimitStr)

        def countdowns = []
        numThemes.times{
            countdowns.add('&#x25A2')
        }
        def countdownsStr = countdowns.join('&nbsp;')

        return """\
${strong('Dungeon')}
<br/>
<br/>${ss('Size:')} ${dsizeStr} &nbsp;&nbsp;&nbsp;&nbsp;${ss('Area Limit:')} ${numAreas}
<br/>${ss('Builder:')} ${ -> pick(foundation_builder)}
<br/>${ss('Function:')} ${ -> pick(foundation_function)}
<br/>
<br/>${ss('Ruination:')} ${ -> pick(dungeon_ruination)}
<br/>
<br/>${strong('Themes:')}
<br/>&nbsp;&nbsp;${ -> pickN(theme, numThemes).collect{ it.toString() + '&nbsp;' + countdownsStr}.join("<br/>&nbsp;&nbsp;")}\
"""
    }
}
