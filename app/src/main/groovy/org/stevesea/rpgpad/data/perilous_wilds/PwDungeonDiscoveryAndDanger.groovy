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
class PwDungeonDiscoveryAndDanger extends AbstractGenerator{
    PwCreature pwCreature = new PwCreature()
    PwDetails pwDetails = new PwDetails()
    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwDetails.withShuffler(shuff)
        pwCreature.withShuffler(shuff)
        this
    }

    RangeMap discovery_dressing = new RangeMap()
            .with(1, 'junk/debris')
            .with(2, 'tracks/marks')
            .with(3, 'signs of battle')
            .with(4, 'writing/carving')
            .with(5, 'warning')
            .with(6, "dead ${ -> pick(pwCreature.creature_no_tags)}")
            .with(7, 'bones/remains')
            .with(8, 'book/scroll/map')
            .with(9, 'broken door/wall')
            .with(10, 'breeze/wind/smell')
            .with(11, 'lichen/moss/fungus')
            .with(12, "${ -> pwDetails.pickOddity()}")
    RangeMap discovery_feature = new RangeMap()
            .with(1, 'cave-in/collapse')
            .with(2, 'pit/shaft/chasm')
            .with(3, 'pillars/columns')
            .with(4, 'locked door/gate')
            .with(5, 'alcoves/niches')
            .with(6, 'bridge/stairs/ramp')
            .with(7, 'fountain/well/pool')
            .with(8, 'puzzle')
            .with(9, 'altar/dais/platform')
            .with(10, 'statue/idol')
            .with(11, 'magic pool/statue/idol')
            .with(12, 'connection to another dungeon')
    RangeMap discovery_find = new RangeMap()
            .with(1, 'trinkets')
            .with(2, 'tools')
            .with(3, 'weapons/armor')
            .with(4, 'supplies/trade goods')
            .with(5, 'coins/gems/jewelry')
            .with(6, 'poisons/potions')
            .with(7, 'adventurer/captive')
            .with(8, 'magic item')
            .with(9, 'scroll/book')
            .with(10, 'magic weapon/armor')
            .with(11, 'artifact')
            .with(12, "${ -> pickN(discovery_find, 2).join(', ')}")

    //A starting point: extrapolate, embellish, integrate.
    RangeMap discovery = new RangeMap()
            .with(1..3, "${ -> pick(discovery_dressing)}")
            .with(4..9, "${ -> pick(discovery_feature)}")
            .with(10..12, "${ -> pick(discovery_find)}")

    RangeMap danger_trap = new RangeMap()
            .with(1, 'alarm')
            .with(2, 'ensnaring/paralyzing')
            .with(3, 'pit')
            .with(4, 'crushing')
            .with(5, 'piercing/puncturing')
            .with(6, 'chopping/slashing')
            .with(7, 'confusing (maze, etc.)')
            .with(8, 'gas (poison, etc.)')
            .with(9, "${ -> pwDetails.pickElement()}")
            .with(10, 'ambush')
            .with(11, 'magical')
            .with(12, "${ -> pickN(danger_trap, 2).join(', ')}")
    RangeMap danger_creature = new RangeMap()
            .with(1, 'waiting in ambush')
            .with(2, 'fighting/squabbling')
            .with(3, 'prowling/on patrol')
            .with(4, 'looking for food')
            .with(5, 'eating/resting')
            .with(6, 'guarding')
            .with(7, 'on the move')
            .with(8, 'searching/scavenging')
            .with(9, 'returning to den')
            .with(10, 'making plans')
            .with(11, 'sleeping')
            .with(12, 'dying')
    RangeMap danger_entity = new RangeMap()
            .with(1, 'alien interloper')
            .with(2, 'vermin lord')
            .with(3, 'criminal mastermind')
            .with(4, 'warlord')
            .with(5, 'high priest')
            .with(6, 'oracle')
            .with(7, 'wizard/witch/alchemist')
            .with(8, "${ -> pick(pwCreature.monster)} lord")
            .with(9, 'evil spirit/ghost')
            .with(10, 'undead lord (lich, etc.)')
            .with(11, 'demon')
            .with(12, 'dark god')
    // if they would notice, show signs of an approaching threat
    RangeMap danger = new RangeMap()
            .with(1..4, "${ -> pick(danger_trap)}")
            .with(5..11, """\
${ -> pick(pwCreature.creature_no_tags)} : ${ -> pick(danger_creature)}
<br/>&nbsp;&nbsp;${ssem('Alignment:')} ${-> pwDetails.pickAlignment()}
<br/>&nbsp;&nbsp;${ssem('Disposition:')} ${-> pwDetails.pickDisposition()}
<br/>&nbsp;&nbsp;${ssem('No. Appearing:')} ${-> pwDetails.pickNumberAppearing()}
""")
            .with(12, "${ -> pick(danger_entity)}")

    RangeMap area_type_and_contents = new RangeMap()
            .with(1, """\
${strong('Area Type:')} Common [Unthemed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;empty""")
            .with(2, """\
${strong('Area Type:')} Common [Unthemed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(3..4, """\
${strong('Area Type:')} Common [Unthemed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(5..6, """\
${strong('Area Type:')} Common [Unthemed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}""")
            .with(7, """\
${strong('Area Type:')} Common [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(8, """\
${strong('Area Type:')} Common [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(9, """\
${strong('Area Type:')} Common [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}""")
            .with(10, """\
${strong('Area Type:')} Unique [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(11, """\
${strong('Area Type:')} Unique [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}
<br/>&nbsp;&nbsp;${ -> pick(danger)}""")
            .with(12, """\
${strong('Area Type:')} Unique [Themed]
<br/>
<br/>${strong('Contents:')}
<br/>&nbsp;&nbsp;${ -> pick(discovery)}""")

    @Override
    String generate() {
        """\
${ -> pick(area_type_and_contents)}
<br/>
<br/>${ss('Optional:')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Orientation:')} ${-> pwDetails.pickOrientation()}\
"""
    }
}
