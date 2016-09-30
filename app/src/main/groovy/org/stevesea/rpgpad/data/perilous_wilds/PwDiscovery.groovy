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
import org.stevesea.rpgpad.data.Shuffler

import javax.inject.Inject;

@CompileStatic
class PwDiscovery extends AbstractGenerator {
    PwCreature pwCreature
    PwDetails pwDetails

    @Inject
    PwDiscovery(Shuffler shuffler, PwCreature pwCreature, PwDetails pwDetails) {
        super(shuffler)
        this.pwCreature = pwCreature
        this.pwDetails = pwDetails
    }

    @Override
    String generate() {
        switch(rollDice(1, 12)) {
            default: return generateUnnatural()
            case 2..4: return generateNatural()
            case 5..6: return generateEvidence()
            case 7..8: return generateCreature()
            case 9..12: return generateStructure()
        }
    }

    String generateStructure() {
        null
    }

    String generateCreature() {
        "Creature"
    }

    String generateEvidence() {
        null
    }

    static List<String> lair = """\
burrow
burrow
burrow
cave/tunnels
cave/tunnels
cave/tunnels
cave/tunnels
nest/aerie
nest/aerie
hive""".readLines()
    String pickLair() {
        switch(rollDice(1,12)) {
            case 11..12: return "ruins (see ${generateStructure()}"
            default: return pick(lair)
        }
    }
    static List<String> obstacle = """\
1-5 difficult ground (specific to terrain)
6-8 cliff/crevasse/chasm
9-10 ravine/gorge
11-12 Oddity""".readLines()

    String generateNatural() {
        List<String> result = ["<strong>Natural Feature</strong> - Describe how they notice it and what sets it apart."]
        switch(rollDice(1, 12)) {
            case 1..2:
                result.add("<strong>Lair</strong> - ${pickLair()}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString())
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
                break
            case 3..4:
                result.add("<strong>Obstacle/strong> - ${pick(planarFeature)}".toString())
                break
            default:
                result.add("<strong>Divine</strong> - ${pick(planarFeature)}".toString())
                result.add("<strong><small>Alignment</small></strong> - ${pwDetails.pickAlignment()}".toString())
                result.add("<strong><small>Aspect</small></strong> - ${pwDetails.pickAspect()}".toString())
                break
        }

        result.join("<br/>")
    }

    static List<String> arcaneFeature = """\
residue
residue
blight
blight
blight
alteration/mutation
alteration/mutation
enchantment
enchantment
enchantment
source/repository
source/repository""".readLines()
    static List<String> planarFeature = """\
distortion/warp
distortion/warp
distortion/warp
distortion/warp
portal/gate
portal/gate
portal/gate
portal/gate
rift/tear
rift/tear
outpost
outpost""".readLines()
    static List<String> divineFeature = """\
mark/sign
mark/sign
mark/sign
cursed place
cursed place
cursed place
hallowed place
hallowed place
hallowed place
watched place
watched place
presence""".readLines()

    String generateUnnatural() {
        List<String> result = ["<strong>Unnatural Feature</strong> - How does it affect its surroundings?"]
        switch(rollDice(1, 12)) {
            case 1..9:
                result.add("<strong>Arcane</strong> - ${pick(arcaneFeature)}".toString())
                result.add("<strong><small>Alignment</small></strong> - ${pwDetails.pickAlignment()}".toString())
                result.add("<strong><small>Magic Type</small></strong> - ${pwDetails.pickMagicType()}".toString())
                break
            case 10..11:
                result.add("<strong>Planar</strong> - ${pick(planarFeature)}".toString())
                result.add("<strong><small>Alignment</small></strong> - ${pwDetails.pickAlignment()}".toString())
                result.add("<strong><small>Element</small></strong> - ${pwDetails.pickElement()}".toString())
                break
            default:
                result.add("<strong>Divine</strong> - ${pick(planarFeature)}".toString())
                result.add("<strong><small>Alignment</small></strong> - ${pwDetails.pickAlignment()}".toString())
                result.add("<strong><small>Aspect</small></strong> - ${pwDetails.pickAspect()}".toString())
                break
        }

        result.join("<br/>")
    }
}
