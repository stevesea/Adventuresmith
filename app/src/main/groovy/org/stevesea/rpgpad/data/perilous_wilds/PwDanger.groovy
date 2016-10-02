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

import javax.inject.Inject

@CompileStatic
class PwDanger extends AbstractGenerator {
    final PwCreature pwCreature
    final PwDetails pwDetails
    @Inject
    PwDanger(PwCreature pwCreature) {
        super(pwCreature.shuffler)
        this.pwCreature = pwCreature
        this.pwDetails = pwCreature.pwDetails
    }

    static RangeMap undead = new RangeMap()
            .with(1..4, 'haunt/wisp')
            .with(5..8, 'ghost/spectre')
            .with(9, 'banshee')
            .with(10..11, 'wraith/wight')
            .with(12, 'spirit lord/master')

    static RangeMap planar = new RangeMap()
            .with(1..3, 'imp (Small)')
            .with(4..6, 'lesser elemental')
            .with(7..9, 'lesser demon/horror')
            .with(10, 'greater elemental')
            .with(11, 'greater demon/horror')
            .with(12, 'devil/elemental lord')
    static RangeMap divine = new RangeMap()
            .with(1..5, 'agent')
            .with(6..9, 'champion')
            .with(10..11, 'army (Host)')
            .with(12, 'avatar')
    static RangeMap hazard_unnatural = new RangeMap()
            .with(1..3, 'taint/blight/curse')
            .with(4..8, 'arcane trap/effect')
            .with(9..11, 'planar trap/effect')
            .with(12, 'divine')
    RangeMap hazard_natural = new RangeMap()
            .with(1..2, 'blinding mist/fog')
            .with(3..4, 'bog/mire/quicksand')
            .with(5..7, 'pitfall/sinkhole/chasm')
            .with(8..9, 'poison/disease')
            .with(10..11, 'flood/fire/tornado')
            .with(12, "${ -> pwDetails.pickOddity()}")
    static RangeMap hazard_trap = new RangeMap()
            .with(1..2, 'alarm')
            .with(3..5, 'ensnaring/paralyzing')
            .with(6..8, 'injurious (pit, etc.)')
            .with(9, 'gas/fire/poison')
            .with(10..12, 'ambush')


    RangeMap unnatural_entity = new RangeMap()
            .with(1..8, """\
${ -> pick(undead)}
<br/>
<br/>${ss('Ability:')} ${ -> pwDetails.pickAbility()}
<br/>${ss('Activity:')} ${ -> pwDetails.pickActivity()}
<br/>${ss('Alignment:')} ${ -> pwDetails.pickAlignment()}
<br/>${ss('Disposition:')} ${ -> pwDetails.pickDisposition()}\
""")
            .with(9..11, """\
${ -> pick(planar)}
<br/>
<br/>${ss('Ability:')} ${ -> pwDetails.pickAbility()}
<br/>${ss('Activity:')} ${ -> pwDetails.pickActivity()}
<br/>${ss('Alignment:')} ${ -> pwDetails.pickAlignment()}
<br/>${ss('Disposition:')} ${ -> pwDetails.pickDisposition()}
<br/>${ss('Element:')} ${ -> pwDetails.pickElement()}
<br/>${ss('Feature:')} ${ -> pwDetails.pickFeature()}
<br/>${ss('Tag:')} ${ -> pwDetails.pickTag()}\
""")
            .with(12, """\
${ -> pick(divine)}
<br/>
<br/>${ss('Ability:')} ${ -> pwDetails.pickAbility()}
<br/>${ss('Activity:')} ${ -> pwDetails.pickActivity()}
<br/>${ss('Alignment:')} ${ -> pwDetails.pickAlignment()}
<br/>${ss('Aspect:')} ${ -> pwDetails.pickAspect()}
<br/>${ss('Disposition:')} ${ -> pwDetails.pickDisposition()}
<br/>${ss('Element:')} ${ -> pwDetails.pickElement()}
<br/>${ss('Feature:')} ${ -> pwDetails.pickFeature()}
<br/>${ss('Tag:')} ${ -> pwDetails.pickTag()}\
""")
    RangeMap hazard = new RangeMap()
            .with(1..2, """\
${ -> pick(hazard_unnatural)}
<br/>
<br/>${ss('Aspect:')} ${ -> pwDetails.pickAbility()}
<br/>${ss('Visbility:')} ${ -> pwDetails.pickVisibility()}\
""")
            .with(3..10, """\
${ -> pick(hazard_natural)}
""")
            .with(11..12, """\
${ -> pick(hazard_trap)}
<br/>
<br/>${ss('Aspect:')} ${-> pwDetails.pickAbility()}
<br/>${ss('Visbility:')} ${-> pwDetails.pickVisibility()}\
<br/>
<br/>${ss('Creature responsible:')} ${ -> pick(pwCreature.creature_no_tags)}
""")

    RangeMap dangerMap = new RangeMap()
            .with(1, """\
${strong('Unnatural Entity')}
<br/>${ -> pick(unnatural_entity)}\
""")
            .with(2..6, """\
${strong('Hazard')}
<br/>${ -> pick(hazard)}\
""")
            .with(7..12,"${ -> pwCreature.generate()}")

    @Override
    String generate() {
        return pick(dangerMap)
    }
}
