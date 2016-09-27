/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.steavesea.rpg_boy2000.data.maze_rats

import groovy.transform.CompileStatic
import org.steavesea.rpg_boy2000.data.AbstractGenerator
import org.steavesea.rpg_boy2000.data.RpgBoyData
import org.steavesea.rpg_boy2000.data.Shuffler

import javax.inject.Inject

@CompileStatic
class MazeRatsPotionEffects extends AbstractGenerator {
    public String getName() {
        return "Potion Effects"
    }
    public String getDataset() {
        return RpgBoyData.MAZERATS
    }
    static final List<String> effects = """\
1-hour vampirism
Alter face
Alter voice
Animal-form
Anti-gravity
Anti-magic
Astral travel
Blink
Blurry outlines
Body-swap
Chamaeleon skin
Charm
Clairaudience
Clairvoyance
Command insects
Control animals
Control element
Contr. humanoids
Control plants
Control spirits
Control undead
Cure Disease
Cure Poison
Deafening voice
Detect evil
Detect gold
Detect magic
Detect secret doors
Detect undead
Direction Sense
Dream-walk
Element-form
Enhance all attacks
Expert artisan
Expert blacksmith
Expert cook
Expert engineer
Expert musician
Expert orator
Expert surgeon
Extra arm
Fire breathing
Flying
Frog tongue
Gain a tail
Gender swap
Growth
Haste
Heal stats
Heal Wounds
Hear thoughts
Heat vision
Identify magic
Immune to cold
Immune to heat
Immune to metal
Immune to poison
Invisibility
Invulnerability
Iron belly
Item-form
Jumping
Levitation
Mirror image
Never hungry
Never lost
Night vision
Nullify gravity
Panglottism
Pass as undead
Radiance
Random affliction
Random spell
Regeneration
Removes curse
Scorching gaze
Scry
Second sight
Sharp claws
Shrink
Slow
Speak with animals
Speak with dead
Speak w/elements
Speak with plants
Spider-climbing
Stretchy
Super strength
Telekinesis
Telepathy
Terror-presence
Throw Voice
Tongues
Too boring to see
True Sight
Truthsay
Water breathing
Water walking
Web-slinging
X-Ray vision\
""".readLines()

    @Inject
    MazeRatsPotionEffects(Shuffler shuffler) {
        super(shuffler);
    }

    String generate() {
        return "${ -> pick(effects)}"
    }

}
