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
package org.stevesea.rpgpad.data.perilous_wilds

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap

@CompileStatic
class PwRegion extends AbstractGenerator {
    static final List<String> adjectives = '''
        Ageless Ashen
        Black Blessed Blighted Blue Broken Burning
        Cold Cursed
        Dark Dead Deadly Deep Desolate Diamond Dim Dismal Dun
        Eerie Endless
        Fallen Far Fell
        Flaming Forgotten Forsaken Frozen
        Glittering Golden Green Grim
        Holy
        Impassable
        Jagged
        Light Long
        Misty
        Perilous Purple
        Red
        Savage Shadowy Shattered Shifting Shining Silver
        White Wicked
        Yellow
        '''.tokenize()

    static final List<String> nouns = '''
        [Name]
        Ash
        Bone Darkness Dead Death Desolation Despair Devil Doom Dragon
        Fate Fear Fire Fury
        Ghost Giant God Gold
        Heaven Hell Honor Hope Horror
        King
        Life Light Lord
        Mist
        Peril
        Queen
        Rain Refuge Regret
        Savior Shadow Silver Skull Sky Smoke Snake Sorrow Storm Sun
        Thorn Thunder Traitor Troll
        Victory
        Witch
        '''.tokenize()

    static final List<String> terrains = '''
        Bay Bluffs Bog
        Cliffs
        Desert Downs Dunes
        Expanse
        Fells Fen Flats Foothills Forest
        Groves
        Heath Heights Hills Hollows
        Jungle
        Lake
        Lowland
        March Marsh Meadows Moor Morass Mounds Mountains
        Peaks Plains Prairie
        Quagmire
        Range Reach
        Sands Savanna Scarps Sea Slough Sound Steppe Swamp Sweep
        Teeth Thicket
        Upland
        Wall Waste Wasteland Woods
        '''.tokenize()

    RangeMap regions = new RangeMap()
            .with(1..4, '{{adjective}} {{terrain}}')
            .with(5..6, '{{terrain}} of (the) {{noun}}')
            .with(7..8, 'The {{adjective}} {{terrain}}')
            .with(9..10, '{{noun}} {{terrain}}')
            .with(11, '{{noun}}\'s {{adjective}} {{terrain}}')
            .with(12, '{{adjective}} {{terrain}} of (the) {{noun}}')

    String generate() {
        Mustache.compiler().compile(pick(regions) as String).execute(
                [
                        terrain: pick(terrains),
                        adjective: pick(adjectives),
                        noun: pick(nouns),
                ]
        )
    }
}
