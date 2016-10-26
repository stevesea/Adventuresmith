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

package org.stevesea.adventuresmith.data.perilous_wilds

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.RangeMap
import org.stevesea.adventuresmith.data.Shuffler

@CompileStatic
class PwDiscovery extends AbstractGenerator {
    PwCreature pwCreature = new PwCreature()
    PwDetails pwDetails = new PwDetails()
    PwSteading pwSteading = new PwSteading()
    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwDetails.withShuffler(shuff)
        pwCreature.withShuffler(shuff)
        pwSteading.withShuffler(shuff)
        this
    }

    RangeMap arcane = new RangeMap()
            .with(1..2, 'residue')
            .with(3..5, 'blight')
            .with(6..7, 'alteration/mutation')
            .with(8..10, 'enchantment')
            .with(11..12, 'source/repository')
    RangeMap planar = new RangeMap()
            .with(1..4, 'distortion/warp')
            .with(5..8, 'portal/gate')
            .with(9..10, 'rift/tear')
            .with(11..12, 'outpost')
    RangeMap divine = new RangeMap()
            .with(1..3, 'mark/sign')
            .with(4..6, 'cursed place')
            .with(7..9, 'hallowed place')
            .with(10..11, 'watched place')
            .with(12, 'presence')
    RangeMap lair = new RangeMap()
            .with(1..3, 'burrow')
            .with(4..7, 'cave/tunnels')
            .with(8..9, 'nest/aerie')
            .with(10, 'hive')
            .with(11..12, 'ruins (see STRUCTURE)')
    RangeMap obstacle = new RangeMap()
            .with(1..5, 'difficult ground')
            .with(6..8, 'cliff/crevasse/chasm')
            .with(9..10, 'ravine/gorge')
            .with(11..12, "${ -> pwDetails.pickOddity()}")
    RangeMap terrain_change = new RangeMap()
            .with(1..4, 'limited area of an another Terrain type')
            .with(5..6, 'crevice/hole/pit/cave')
            .with(7..8, 'altitude change')
            .with(9..10, 'canyon/valley')
            .with(11..12, 'rise/peak in distance')
    RangeMap water_feature = new RangeMap()
            .with(1, 'spring/hotspring')
            .with(2, 'waterfall/geyser')
            .with(3..6, 'creek/stream/brook')
            .with(7..8, 'pond/lake')
            .with(9..10, 'river')
            .with(11..12, 'sea/ocean')
    RangeMap landmark = new RangeMap()
            .with(1..3, 'water-based (waterfall, geyser, etc.)')
            .with(4..6, 'plant-based (ancient tree, giant flowers, etc.)')
            .with(7..10, 'earth-based (peak, formation, crater, etc.)')
            .with(11..12, "${ -> pwDetails.pickOddity()}")
    RangeMap resource = new RangeMap()
            .with(1..4, 'game/fruit/vegetable')
            .with(5..6, 'herb/spice/dye source')
            .with(7..9, 'timber/stone')
            .with(10..11, 'ore (copper, iron, etc.)')
            .with(12, 'precious metal/gems')
    RangeMap tracks_spoor = new RangeMap()
            .with(1..3, 'faint/unclear')
            .with(4..6, 'definite/clear')
            .with(7..8, 'multiple')
            .with(9..10, 'signs of violence')
            .with(11..12, 'trail of blood/other')
    RangeMap remains_debris = new RangeMap()
            .with(1..4, 'bones')
            .with(5..7, 'corpse/carcass')
            .with(8..9, 'site of violence')
            .with(10, 'junk/refuse')
            .with(11, 'lost supplies/cargo')
            .with(12, 'tools/weapons/armor')
    RangeMap stash_cache = new RangeMap()
            .with(1..3, 'trinkets/coins')
            .with(4..5, 'tools/weapons/armor')
            .with(6..7, 'map')
            .with(8..9, 'food/supplies')
            .with(10..12, 'treasure (see TREASURE)')
    RangeMap enigmatic = new RangeMap()
            .with(1..4, 'earthworks')
            .with(5..8, 'megalith')
            .with(9..11, 'statue/idol/totem')
            .with(12, "${ -> pwDetails.pickOddity()}")
    RangeMap infrastructure = new RangeMap()
            .with(1..4, 'track/path')
            .with(5..8, 'road')
            .with(9..10, 'bridge/ford')
            .with(11, 'mine/quarry')
            .with(12, 'aqueduct/canal/portal')
    RangeMap dwelling = new RangeMap()
            .with(1..3, 'campsite')
            .with(4..6, 'hovel/hut')
            .with(7..8, 'farm')
            .with(9..10, 'inn/roadhouse')
            .with(11..12, 'tower/keep/estate')
    RangeMap burial_religious = new RangeMap()
            .with(1..2, 'grave marker/barrow')
            .with(3..4, 'graveyard/necropolis')
            .with(5..6, 'tomb/crypt')
            .with(7..9, 'shrine')
            .with(10..11, 'temple/retreat')
            .with(12, 'great temple')

    @Override
    String generate() {

        RangeMap unnaturalFeature = new RangeMap()
                .with(1..9, """\
${strong('Arcane')}
<br/>${pick(arcane)}
<br/>${ss('Alignment:')} ${pwDetails.pickAlignment()}
<br/>${ss('Magic Type:')} ${pwDetails.pickMagicType()}\
""")
                .with(10..11, """\
${strong('Planar')}
<br/>${pick(planar)}
<br/>${ss('Alignment:')} ${pwDetails.pickAlignment()}
<br/>${ss('Element:')} ${pwDetails.pickElement()}\
""")
                .with(12, """\
${strong('Divine')}
<br/>${pick(divine)}
<br/>${ss('Alignment:')} ${pwDetails.pickAlignment()}
<br/>${ss('Aspect:')} ${pwDetails.pickAspect()}\
""")

        RangeMap naturalFeature = new RangeMap()
                .with(1..2, """\
${strong('Lair')}
<br/>${pick(lair)}
<br/>${ss('Creature responsible:')} ${pick(pwCreature.creature_no_tags)}
<br/>${ss('Visibility:')} ${pwDetails.pickVisibility()}\
""")
                .with(3..4, """\
${strong('Obstacle')}
<br/>${pick(obstacle)}\
""")
                .with(5..7, """\
${strong('Terrain Change')}
<br/>${pick(terrain_change)}\
""")
                .with(8..9, """\
${strong('Water Feature')}
<br/>${pick(water_feature)}\
""")
                .with(10..11, """\
${strong('Landmark')}
<br/>${pick(landmark)}\
""")
                .with(12, """\
${strong('Resource')}
<br/>${pick(resource)}
<br/>${ss('Size:')} ${-> pwDetails.pickSize()}
<br/>${ss('Visibility:')} ${-> pwDetails.pickVisibility()}\
""")

        RangeMap evidence = new RangeMap()
                .with(1..6, """\
${strong('Tracks/Spoor')}
<br/>${pick(tracks_spoor)}
<br/>${ss('Age:')} ${-> pwDetails.pickAge()}
<br/>${ss('Creature responsible:')} ${-> pick(pwCreature.creature_no_tags)}\
""")
                .with(7..10,  """\
${strong('Remains/Debris')}
<br/>${pick(remains_debris)}
<br/>${ss('Age:')} ${-> pwDetails.pickAge()}
<br/>${ss('Visibility:')} ${-> pwDetails.pickVisibility()}\
""")
                .with(11..12, """\
${strong('Stash/Cache')}
<br/>${pick(stash_cache)}
""")

        RangeMap ruin = new RangeMap()
                .with(1..2, "${pick('1d6+6', infrastructure)}")
                .with(3..4, "${pick('1d8+4', dwelling)}")
                .with(5..6, "${pick('1d8+4', burial_religious)}")
                .with(7..8, "${pwSteading.generate('1d10+2')}")
                .with(9..12, '(roll DUNGEON)')


        RangeMap structure = new RangeMap()
                .with(1, """\
${strong('Enigmatic')}
<br/>${pick(enigmatic)}
<br/>${ss('Age:')} ${pwDetails.pickAge('1d8+4')}
<br/>${ss('Size:')} ${pwDetails.pickSize('1d8+4')}
<br/>${ss('Visibility:')} ${pwDetails.pickVisibility()}\
""")
                .with(2..3, """\
${strong('Infrastructure')}
<br/>${pick(infrastructure)}
<br/>${ss('Creature responsible:')} ${pick('1d4+4', pwCreature.creature_no_tags)}\
""")
                .with(4, """\
${strong('Dwelling')}
<br/>${pick(dwelling)}
<br/>${ss('Creature responsible:')} ${-> pick('1d4+4', pwCreature.creature_no_tags)}\
""")
                .with(5..6, """\
${strong('Burial/Religious')}
<br/>${pick(dwelling)}
<br/>${ss('Creature responsible:')} ${-> pick('1d4+4', pwCreature.creature_no_tags)}
<br/>${ss('Alignment:')} ${-> pwDetails.pickAlignment()}
<br/>${ss('Aspect:')} ${-> pwDetails.pickAspect()}\
""")
                .with(7..8, "${pwSteading.generate()}")
                .with(9..12, """\
${strong('Ruin')}
<br/>${pick(ruin)}
<br/>${ss('Creature responsible:')} ${-> pick('1d4+4', pwCreature.creature_no_tags)}
<br/>${ss('Age:')} ${-> pwDetails.pickAge('1d8+4')}
<br/>${ss('Ruination:')} ${-> pwDetails.pickRuination()}
<br/>${ss('Visibility:')} ${-> pwDetails.pickVisibility()}\
""")

            RangeMap discovery = new RangeMap()
                    .with(1, """\
        ${strong('Unnatural Feature')} -- ${small('How does it affect its surroundings?')}
        <br/>
        <br/>${pick(unnaturalFeature)}\
        """)
                    .with(2..4, """\
        ${strong('Natural Feature')} -- ${small('Describe how they notice it and what sets it apart.')}
        <br/>
        <br/>${pick(naturalFeature)}\
        """)
                    .with(5..6, """\
        ${strong('Evidence')} -- ${small('Consider the implications and be ready for them to take the bait.')}
        <br/>
        <br/>${pick(evidence)}\
        """)
                    .with(7..8, """\
        ${strong('Creature')} -- ${small('Not an immediate threat, but might become one.')}
        <br/>
        <br/>${pwCreature.generate()}\
        """)
                    .with(9..12, """\
        ${strong('Structure')} -- ${small('Who built it? Is it connected to anything else they made nearby?')}
        <br/>
        <br/>${pick(structure)}\
        """)

        pick(discovery)
    }
}
