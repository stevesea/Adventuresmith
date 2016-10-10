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
package org.stevesea.rpgpad.data.maze_rats

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

@CompileStatic
class MazeRatsPotionEffects extends AbstractGenerator {
    static final List<String> effects = '''\
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
'''.readLines()

    String generate() {
        pick(effects)
    }

}
