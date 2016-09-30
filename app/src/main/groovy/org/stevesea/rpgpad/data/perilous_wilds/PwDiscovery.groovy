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
    PwTreasure pwTreasure

    @Inject
    PwDiscovery(Shuffler shuffler, PwCreature pwCreature, PwDetails pwDetails, PwTreasure pwTreasure) {
        super(shuffler)
        this.pwCreature = pwCreature
        this.pwDetails = pwDetails
        this.pwTreasure = pwTreasure
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

    static List<String> enigmatic = """\
earthworks
earthworks
earthworks
earthworks
megalith
megalith
megalith
megalith
statue/idol/totem
statue/idol/totem
statue/idol/totem""".readLines()
    String pickEnigmatic() {
        switch(rollDice(1,12)) {
            case 12: return pwDetails.pickOddity()
            default: return pick(enigmatic)
        }
    }

    static List<String> infrastructure = """\
track/path
track/path
track/path
track/path
road
road
road
road
bridge/ford
bridge/ford
mine/quarry
aqueduct/canal/portal
""".readLines()

    static List<String> dwelling = """\
campsite
campsite
campsite
hovel/hut
hovel/hut
hovel/hut
farm
farm
inn/roadhouse
inn/roadhouse
tower/keep/estate
tower/keep/estate""".readLines()

    static List<String> burial_religious = """\
grave marker/barrow
grave marker/barrow
graveyard/necropolis
graveyard/necropolis
tomb/crypt
tomb/crypt
shrine
shrine
shrine
temple/retreat
temple/retreat
great temple""".readLines()
    static List<String> ruin = """\
1-2 Infrastructure (1d6+6)
3-4 Dwelling (1d8+4)
5-6 Burial/Religious
(1d8+4)
7-8 Steading (1d10+2)
9-12 Dungeon (pp60-61)""".readLines()

    String generateStructure() {
        List<String> result = ["<strong>Structure</strong> - Who built it? Is it connected to anything else they made nearby?"]
        switch(rollDice(1, 12)) {
            case 1:
                result.add("<strong>Enigmatic</strong> - ${pickEnigmatic()}".toString())
                result.add("<strong><small>Age</small></strong> - ${pwDetails.pickAge()}".toString()) // TODO
                result.add("<strong><small>Size</small></strong> - ${pwDetails.pickSize()}".toString()) // TODO
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
                break
            case 2..3:
                result.add("<strong>Infrastructure</strong> - ${pick(infrastructure)}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString()) // TODO
                break
            case 4:
                result.add("<strong>Dwelling</strong> - ${pick(dwelling)}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString()) // TODO
                break
            case 5..6:
                result.add("<strong>Burial/Religious</strong> - ${pick(burial_religious)}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString()) // TODO
                result.add("<strong><small>Alignment</small></strong> - ${pwDetails.pickAlignment()}".toString()) // TODO
                result.add("<strong><small>Aspect</small></strong> - ${pwDetails.pickAspect()}".toString()) // TODO
                break
            case 7..8:
                result.add("<strong>Steading</strong> - TBD".toString()) // TODO
                break
            default:
                result.add("<strong>Ruin</strong> - ${pick(ruin)}".toString()) // TODO
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString()) // TODO
                result.add("<strong><small>Age</small></strong> - ${pwDetails.pickAge()}".toString()) // TODO
                result.add("<strong><small>Ruination</small></strong> - ${pwDetails.pickRuination()}".toString())
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
                break
        }

        result.join("<br/>")
    }

    String generateCreature() {
        List<String> result = ["<strong>Creature</strong> - Not an immediate threat, but might become one."]
        result.add("<strong>Creature</strong> - ${pwCreature.generate()}".toString())
        return result.join("<br/>")
    }

    static List<String> track_spoor = """\
faint/unclear
faint/unclear
faint/unclear
definite/clear
definite/clear
definite/clear
multiple
multiple
signs of violence
signs of violence
trail of blood/other
trail of blood/other""".readLines()
    static List<String> remains_debris = """\
bones
bones
bones
bones
corpse/carcass
corpse/carcass
corpse/carcass
site of violence
site of violence
junk/refuse
lost supplies/cargo
tools/weapons/armor""".readLines()
    static List<String> stash_cache = """\
trinkets/coins
trinkets/coins
trinkets/coins
tools/weapons/armor
tools/weapons/armor
map
map
food/supplies
food/supplies""".readLines()
    String pickStashCache() {
        switch(rollDice(1,12)) {
            case 10..12: return pwTreasure.generate()
            default: return pick(stash_cache)
        }
    }

    String generateEvidence() {
        List<String> result = ["<strong>Evidence</strong> - Consider the implications and be ready for them to take the bait."]
        switch(rollDice(1, 12)) {
            case 1..6:
                result.add("<strong>Tracks/Spoor</strong> - ${pick(track_spoor)}".toString())
                result.add("<strong><small>Age</small></strong> - ${pwDetails.pickAge()}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString())
                break
            case 7..10:
                result.add("<strong>Remains/Debris</strong> - ${pick(remains_debris)}".toString())
                result.add("<strong><small>Age</small></strong> - ${pwDetails.pickAge()}".toString())
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
                break
            default:
                result.add("<strong>Stash/Cache</strong> - ${pickStashCache()}".toString())
                break
        }

        result.join("<br/>")
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
difficult ground (specific to terrain)
difficult ground (specific to terrain)
difficult ground (specific to terrain)
difficult ground (specific to terrain)
difficult ground (specific to terrain)
cliff/crevasse/chasm
cliff/crevasse/chasm
cliff/crevasse/chasm
ravine/gorge
ravine/gorge""".readLines()
    String pickObstacle() {
        switch(rollDice(1,12)) {
            case 11..12: return pwDetails.getOddity()
            default: pick(obstacle)
        }
    }

    static List<String> terrainChange = """\
limited area of an another Terrain type
limited area of an another Terrain type
limited area of an another Terrain type
limited area of an another Terrain type
crevice/hole/pit/cave
crevice/hole/pit/cave
altitude change
altitude change
canyon/valley
canyon/valley
rise/peak in distance
rise/peak in distance""".readLines()

    static List<String> waterFeature = """\
spring/hotspring
waterfall/geyser
creek/stream/brook
creek/stream/brook
creek/stream/brook
creek/stream/brook
pond/lake
pond/lake
river
river
sea/ocean
sea/ocean""".readLines()

    static List<String> landmark = """\
water-based (waterfall, geyser, etc.)
water-based (waterfall, geyser, etc.)
water-based (waterfall, geyser, etc.)
plant-based (ancient tree, giant flowers, etc.)
plant-based (ancient tree, giant flowers, etc.)
plant-based (ancient tree, giant flowers, etc.)
earth-based (peak, formation, crater, etc.)
earth-based (peak, formation, crater, etc.)
earth-based (peak, formation, crater, etc.)
earth-based (peak, formation, crater, etc.)""".readLines()
    String pickLandmark() {
        switch(rollDice(1, 12)) {
            case 11..12: return pwDetails.getOddity()
            default: return pick(landmark)
        }
    }

    static List<String> resource = """\
game/fruit/vegetable
game/fruit/vegetable
game/fruit/vegetable
game/fruit/vegetable
herb/spice/dye source
herb/spice/dye source
timber/stone
timber/stone
timber/stone
ore (copper, iron, etc.)
ore (copper, iron, etc.)
precious metal/gems""".readLines()
    String generateNatural() {
        List<String> result = ["<strong>Natural Feature</strong> - Describe how they notice it and what sets it apart."]
        switch(rollDice(1, 12)) {
            case 1..2:
                result.add("<strong>Lair</strong> - ${pickLair()}".toString())
                result.add("<strong><small>Creature</small></strong> - ${pwCreature.generate()}".toString())
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
                break
            case 3..4:
                result.add("<strong>Obstacle</strong> - ${pickObstacle()}".toString())
                break
            case 5..7:
                result.add("<strong>Terrain Change</strong> - ${pick(terrainChange)}".toString())
                break
            case 8..9:
                result.add("<strong>Water Feature</strong> - ${pick(waterFeature)}".toString())
                break
            case 10..11:
                result.add("<strong>Landmark</strong> - ${pickLandmark()}".toString())
                break
            default:
                result.add("<strong>Resource</strong> - ${pick(resource)}".toString())
                result.add("<strong><small>Size</small></strong> - ${pwDetails.pickSize()}".toString())
                result.add("<strong><small>Visibility</small></strong> - ${pwDetails.pickVisibility()}".toString())
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
