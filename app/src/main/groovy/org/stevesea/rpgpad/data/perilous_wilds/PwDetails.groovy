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

// TODO : these are broken. discovery.structure and others want to pick via 1d8+4 and similar. need to be sure to retain the list order n stuff
@CompileStatic
class PwDetails  extends AbstractGenerator {

    @Inject
    PwDetails(Shuffler shuffler) {
        super(shuffler)
    }

    @Override
    String generate() {
        return """
<br/><strong><small>Ability: </small></strong>${pickAbility()}
<br/><strong><small>Activity: </small></strong>${pickActivity()}
<br/><strong><small>Adjective: </small></strong>${pickAdjective()}
<br/><strong><small>Age: </small></strong>${pickAge()}
<br/><strong><small>Aspect: </small></strong>${pickAspect()}
<br/><strong><small>Condition: </small></strong>${pickCondition()}
<br/><strong><small>Disposition: </small></strong>${pickDisposition()}
<br/><strong><small>Element: </small></strong>${pickElement()}
<br/><strong><small>Feature: </small></strong>${pickFeature()}
<br/><strong><small>Magic Type: </small></strong>${pickMagicType()}
<br/><strong><small>No. Appearing: </small></strong>${pickNumberAppearing()}
<br/><strong><small>Oddity: </small></strong>${pickOddity()}
<br/><strong><small>Orientation: </small></strong>${pickOrientation()}
<br/><strong><small>Ruination: </small></strong>${pickRuination()}
<br/><strong><small>Size: </small></strong>${pickSize()}
<br/><strong><small>Tag: </small></strong>${pickTag()}
<br/><strong><small>Terrain: </small></strong>${pickTerrain()}
<br/><strong><small>Visbility: </small></strong>${pickVisibility()}
"""
    }

    static List<String> ability = """\
bless/curse
entangle/trap/snare
poison/disease
paralyze/petrify
mimic/camouflage
seduce/hypnotize
dissolve/disintegrate
drain life/magic
read/control minds""".readLines()

    String pickAbility() {
        def dice = roll("1d12")
        return pickAbility(dice)
    }

    String pickAbility(int dice) {
        switch (dice) {
            case 8:
                return pickMagicType()
            case 10:
                return "immunity " + pickElement()
            case 12:
                return pickN(ability, 2).join(", ")
            default:
                return pick(ability)
        }
    }

    static List<String> activity = """\
laying trap/ambush
fighting/at war
prowling/on patrol
hunting/foraging
eating/resting
crafting/praying
traveling/relocating
exploring/lost
returning home
building/excavating
sleeping
dying""".readLines()

    String pickActivity() {
        pick(activity)
    }

    static List<String> adjective = """\
slick/slimy
rough/hard/sharp
smooth/soft/dull
corroded/rusty
rotten/decaying
broken/brittle
stinking/smelly
weak/thin/drained
strong/fat/full
pale/poor/shallow
dark/rich/deep
colorful""".readLines()

    String pickAdjective() {
        pick(adjective)
    }

    static List<String> age = """\
being born/built
young/recent
young/recent
young/recent
middle-aged
middle-aged
middle-aged
old
old
ancient
ancient
pre-historic
""".readLines()

    String pickAge() {
        pick(age)
    }

    static List<String> alignment = """
Chaotic
Chaotic
Evil
Evil
Neutral
Neutral
Neutral
Neutral
Good
Good
Lawful
Lawful""".readLines()
    String pickAlignment() {
        pick(alignment)
    }

    static List<String> aspect = """\
power/strength
trickery/dexterity
time/constitution
knowledge/intelligence
nature/wisdom
culture/charisma
war/lies/discord
peace/truth/balance
hate/envy
love/admiration""".readLines()

    String pickAspect() {
        switch(roll("1d12")) {
            case 11:
                return pickElement()
            case 12:
                return pickN(aspect, 2).join(", ")
            default:
                return pick(aspect)
        }
    }

    static List<String> condition = """\
being built/born
intact/healthy/stable
intact/healthy/stable
intact/healthy/stable
occupied/active/alert
occupied/active/alert
occupied/active/alert
worn/tired/weak
worn/tired/weak
vacant/lost
ruined/defiled/dying
disappeared/dead""".readLines()
    String pickCondition() {
        pick(condition)
    }

    static List<String> disposition = """\
attacking
hostile/aggressive
hostile/aggressive
hostile/aggressive
cautious/doubtful
cautious/doubtful
fearful/fleeing
neutral
neutral
neutral
curious/hopeful
friendly""".readLines()
    String pickDisposition() {
        pick(disposition)
    }

    static List<String> element = """\
air
earth
fire
water
life
death""".readLines()
    String pickElement() {
        pick(element)
    }

    static List<String> feature = """\
heavily armored
winged/flying
winged/flying
multiple heads/headless
many eyes/one eye
many limbs/tails
tentacles/tendrils""".readLines()

    String pickFeature() {
        switch(roll("1d12")) {
            case 8:
                return pickAspect()
            case 9:
                return pickAspect()
            case 10:
                return pickMagicType()
            case 11:
                return pickOddity()
            case 12:
                return pickN(feature, 2).join(", ")
            default:
                return pick(feature)
        }
    }

    static List<String> magic_type = """\
divination
enchantment
evocation
illusion
necromancy
summoning""".readLines()
    String pickMagicType() {
        pick(magic_type)
    }

    static List<String> number_appearing = """\
Solitary (1)
Solitary (1)
Solitary (1)
Solitary (1)
Group (1d6+2)
Group (1d6+2)
Group (1d6+2)
Group (1d6+2)
Group (1d6+2)
Horde (4d6 per wave)
Horde (4d6 per wave)
Horde (4d6 per wave)""".readLines()
    String pickNumberAppearing() {
        pick(number_appearing)
    }

    static List<String> oddity = """\
weird color/smell/sound
geometric
web/network/system
crystalline/glass-like
fungal
gaseous/smokey
mirage/illusion
volcanic/explosive
magnetic/repellant
devoid of life
unexpectedly alive""".readLines()
    String pickOddity() {
        switch(roll("1d12")) {
            case 12: return pickN(oddity, 2).join(", ")
            default: return pick(oddity)
        }
    }

    static List<String> orientation = """\
down/earthward
down/earthward
north
northeast
east
southeast
south
southwest
west
northwest
up/skyward
up/skyward""".readLines()
    String pickOrientation() {
        pick(orientation)
    }

    static List<String> ruination = """\
arcane disaster
damnation/curse
earthquake/fire/flood
earthquake/fire/flood
plague/famine/drought
plague/famine/drought
overrun by monsters
overrun by monsters
war/invasion
war/invasion
depleted resources
better prospects elsewhere""".readLines()
    String pickRuination() {
        pick(ruination)
    }

    static List<String> size = """\
Tiny
Small
Small
medium-sized
medium-sized
medium-sized
medium-sized
medium-sized
medium-sized
Large
Large
Huge""".readLines()

    String pickSize() {
        pick(size)
    }

    static List<String> tag = """\
Amorphous
Cautious
Construct
Devious
Intelligent
Magical
Organized
Organized
Planar
Stealthy
Terrifying""".readLines()
    String pickTag() {
        switch(roll("1d12")) {
            case 12: return pickN(tag, 2).join(", ")
            default: return pick(tag)
        }
    }

    static List<String> terrain = """\
wasteland/desert
flatland/plain
flatland/plain
wetland/marsh/swamp
woodland/forest/jungle
woodland/forest/jungle
woodland/forest/jungle
highland/hills
highland/hills
mountains
mountains
Oddity""".readLines()
    String pickTerrain() {
        switch(roll("1d12")) {
            case 12: return pickOddity()
            default: return pick(terrain)
        }
    }

    static List<String> visibility = """\
buried/camouflaged/nigh invisible
buried/camouflaged/nigh invisible
partly covered/overgrown/hidden
partly covered/overgrown/hidden
partly covered/overgrown/hidden
partly covered/overgrown/hidden
obvious/in plain sight
obvious/in plain sight
obvious/in plain sight
visible at near distance
visible at near distance
visible at great distance/focal point""".readLines()
    String pickVisibility() {
        pick(visibility)
    }
}
