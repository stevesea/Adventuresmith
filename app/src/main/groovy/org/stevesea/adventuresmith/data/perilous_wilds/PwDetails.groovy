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

@CompileStatic
class PwDetails extends AbstractGenerator {

    @Override
    String generate() {
        return """\
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
<br/><strong><small>Visbility: </small></strong>${pickVisibility()}\
"""
    }

    RangeMap ability = new RangeMap()
        .with(1, 'bless/curse')
        .with(2, 'entangle/trap/snare')
        .with(3, 'poison/disease')
        .with(4, 'paralyze/petrify')
        .with(5, 'mimic/camouflage')
        .with(6, 'seduce/hypnotize')
        .with(7, 'dissolve/disintegrate')
        .with(8, "${ -> pickMagicType()}")
        .with(9, 'drain life/magic')
        .with(10, "immunity: ${ -> pickElement()}")
        .with(11, 'read/control minds')
        .with(12, "${ -> pickN(ability, 2).join(', ')}")

    String pickAbility(String diceStr = '1d12') {
        return pick(diceStr, ability)
    }

    RangeMap activity = new RangeMap()
        .with(1, 'laying trap/ambush')
        .with(2, 'fighting/at war')
        .with(3, 'prowling/on patrol')
        .with(4, 'hunting/foraging')
        .with(5, 'eating/resting')
        .with(6, 'crafting/praying')
        .with(7, 'traveling/relocating')
        .with(8, 'exploring/lost')
        .with(9, 'returning home')
        .with(10, 'building/excavating')
        .with(11, 'sleeping')
        .with(12, 'dying')

    String pickActivity(String diceStr = '1d12') {
        return pick(diceStr, activity)
    }

    RangeMap adjective = new RangeMap()
            .with(1, 'slick/slimy')
            .with(2, 'rough/hard/sharp')
            .with(3, 'smooth/soft/dull')
            .with(4, 'corroded/rusty')
            .with(5, 'rotten/decaying')
            .with(6, 'broken/brittle')
            .with(7, 'stinking/smelly')
            .with(8, 'weak/thin/drained')
            .with(9, 'strong/fat/full')
            .with(10, 'pale/poor/shallow')
            .with(11, 'dark/rich/deep')
            .with(12, 'colorful')

    String pickAdjective(String diceStr = '1d12') {
        pick(diceStr, adjective)
    }
    RangeMap age = new RangeMap()
            .with(1, 'being born/built')
            .with(2..4, 'young/recent')
            .with(5..7, 'middle-aged')
            .with(8..9, 'old')
            .with(10..11, 'ancient')
            .with(12, 'pre-historic')

    String pickAge(String diceStr = '1d12') {
        pick(diceStr, age)
    }

    RangeMap alignment = new RangeMap()
            .with(1..2, 'Chaotic')
            .with(3..4, 'Evil')
            .with(5..8, 'Neutral')
            .with(9..10, 'Good')
            .with(11..12, 'Lawful')
    String pickAlignment(String diceStr = '1d12') {
        pick(diceStr, alignment)
    }
    RangeMap aspect = new RangeMap()
            .with(1, 'power/strength')
            .with(2, 'trickery/dexterity')
            .with(3, 'time/constitution')
            .with(4, 'knowledge/intelligence')
            .with(5, 'nature/wisdom')
            .with(6, 'culture/charisma')
            .with(7, 'war/lies/discord')
            .with(8, 'peace/truth/balance')
            .with(9, 'hate/envy')
            .with(10, 'love/admiration')
            .with(11, "${ -> pickElement()}")
            .with(12, "${ -> pickN(aspect, 2).join(', ')}")

    String pickAspect(String dice = '1d12') {
        pick(dice, aspect)
    }

    RangeMap condition = new RangeMap()
            .with(1, 'being built/born')
            .with(2..4, 'intact/healthy/stable')
            .with(5..7, 'occupied/active/alert')
            .with(8..9, 'worn/tired/weak')
            .with(10, 'vacant/lost')
            .with(11, 'ruined/defiled/dying')
            .with(12, 'disappeared/dead')
    String pickCondition(String dice='1d12') {
        pick(dice, condition)
    }

    RangeMap disposition = new RangeMap()
            .with(1, 'attacking')
            .with(2..4, 'hostile/aggressive')
            .with(5..6, 'cautious/doubtful')
            .with(7, 'fearful/fleeing')
            .with(8..10, 'neutral')
            .with(11, 'curious/hopeful')
            .with(12, 'friendly')

    String pickDisposition(String dice='1d12') {
        pick(dice, disposition)
    }


    RangeMap element = new RangeMap()
            .with(1..2, 'air')
            .with(3..4, 'earth')
            .with(5..6, 'fire')
            .with(7..8, 'water')
            .with(9..10, 'life')
            .with(11..12, 'death')

    String pickElement(String dice='1d12') {
        pick(dice, element)
    }
    RangeMap feature = new RangeMap()
            .with(1, 'heavily armored')
            .with(2..3, 'winged/flying')
            .with(4, 'multiple heads/headless')
            .with(5, 'many eyes/one eye')
            .with(6, 'many limbs/tails')
            .with(7, 'tentacles/tendrils')
            .with(8, "${ -> pickAspect()}")
            .with(9, "${ -> pickElement()}")
            .with(10, "${ -> pickMagicType()}")
            .with(11, "${ -> pickOddity()}")
            .with(12, "${ -> pickN(feature, 2).join(', ')}")

    String pickFeature(String dice = '1d12') {
        pick(dice, feature)
    }

    RangeMap magic_type = new RangeMap()
            .with(1..2, 'divination')
            .with(3..4, 'enchantment')
            .with(5..6, 'evocation')
            .with(7..8, 'illusion')
            .with(9..10, 'necromancy')
            .with(11..12, 'summoning')
    String pickMagicType(String dice='1d12') {
        pick(dice, magic_type)
    }
    RangeMap number_appearing = new RangeMap()
            .with(1..4, 'Solitary (1)')
            .with(5..9, "Group (1d6+2) [${ -> roll('1d6+2')}]")
            .with(10..12, "Horde (4d6 per wave) [${ ->  roll('4d6')}]")

    String pickNumberAppearing(String dice='1d12') {
        pick(dice, number_appearing)
    }

    RangeMap oddity = new RangeMap()
            .with(1, 'weird color/smell/sound')
            .with(2, 'geometric')
            .with(3, 'web/network/system')
            .with(4, 'crystalline/glass-like')
            .with(5, 'fungal')
            .with(6, 'gaseous/smokey')
            .with(7, 'mirage/illusion')
            .with(8, 'volcanic/explosive')
            .with(9, 'magnetic/repellant')
            .with(10, 'devoid of life')
            .with(11, 'unexpectedly alive')
            .with(12, "${ -> pickN(oddity, 2).join(', ')}")
    String pickOddity(String dice = '1d12') {
        pick(dice, oddity)
    }

    RangeMap orientation = new RangeMap()
            .with(1..2, 'down/earthward')
            .with(3, 'north')
            .with(4, 'northeast')
            .with(5, 'east')
            .with(6, 'southeast')
            .with(7, 'south')
            .with(8, 'southwest')
            .with(9, 'west')
            .with(10, 'northwest')
            .with(11..12, 'up/skyward')
    String pickOrientation(String dice = '1d12') {
        pick(dice, orientation)
    }

    RangeMap ruination = new RangeMap()
            .with(1, 'arcane disaster')
            .with(2, 'damnation/curse')
            .with(3..4, 'earthquake/fire/flood')
            .with(5..6, 'plague/famine/drought')
            .with(7..8, 'overrun by monsters')
            .with(9..10, 'war/invasion')
            .with(11, 'depleted resources')
            .with(12, 'better prospects elsewhere')
    String pickRuination(String dice = '1d12') {
        pick(dice, ruination)
    }

    RangeMap size = new RangeMap()
            .with(1, 'Tiny')
            .with(2..3, 'Small')
            .with(4..9, 'medium-sized')
            .with(10..11, 'Large')
            .with(12, 'Huge')

    String pickSize(String dice = '1d12') {
        pick(dice, size)
    }

    RangeMap tag = new RangeMap()
            .with(1, 'Amorphous')
            .with(2, 'Cautious')
            .with(3, 'Construct')
            .with(4, 'Devious')
            .with(5, 'Intelligent')
            .with(6, 'Magical')
            .with(7..8, 'Organized')
            .with(9, 'Planar')
            .with(10, 'Stealthy')
            .with(11, 'Terrifying')
            .with(12, "${ -> pickN(tag, 2).join(', ')}")
    String pickTag(String dice = '1d12') {
        pick(dice, tag)
    }

    RangeMap terrain = new RangeMap()
            .with(1, 'wasteland/desert')
            .with(2..3, 'flatland/plain')
            .with(4, 'wetland/marsh/swamp')
            .with(5..7, 'woodland/forest/jungle')
            .with(8..9, 'highland/hills')
            .with(10..11, 'moutnains')
            .with(12, "${ -> pickOddity()}")
    String pickTerrain(String dice = '1d12') {
        pick(dice, terrain)
    }
    RangeMap visibility = new RangeMap()
            .with(1..2, 'buried/camouflaged/nigh invisible')
            .with(3..6, 'partly covered/overgrown/hidden')
            .with(7..9, 'obvious/in plain sight')
            .with(10..11, 'visible at near distance')
            .with(12, 'visible at great distance/focal point')
    String pickVisibility(String dice='1d12') {
        pick(dice, visibility)
    }


    RangeMap undead = new RangeMap()
            .with(1..4, 'haunt/wisp')
            .with(5..8, 'ghost/spectre')
            .with(9, 'banshee')
            .with(10..11, 'wraith/wight')
            .with(12, 'spirit lord/master')

    RangeMap planar = new RangeMap()
            .with(1..3, 'imp (Small)')
            .with(4..6, 'lesser elemental')
            .with(7..9, 'lesser demon/horror')
            .with(10, 'greater elemental')
            .with(11, 'greater demon/horror')
            .with(12, 'devil/elemental lord')

    RangeMap divine = new RangeMap()
            .with(1..5, 'agent')
            .with(6..9, 'champion')
            .with(10..11, 'army (Host)')
            .with(12, 'avatar')

    RangeMap resource = new RangeMap()
            .with(1..4, 'game/fruit/vegetable')
            .with(5..6, 'herb/spice/dye source')
            .with(7..9, 'timber/stone')
            .with(10..11, 'ore (copper, iron, etc.)')
            .with(12, 'precious metal/gems')
}
