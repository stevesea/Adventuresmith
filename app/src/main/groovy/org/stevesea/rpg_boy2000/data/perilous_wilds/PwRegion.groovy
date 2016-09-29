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
package org.stevesea.rpg_boy2000.data.perilous_wilds

import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.AbstractGenerator
import org.stevesea.rpg_boy2000.data.Shuffler

@CompileStatic
class PwRegion extends AbstractGenerator {
    static final List<String> adjectives = """
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
        """.tokenize()

    static final List<String> nouns = """
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
        """.tokenize()

    static final List<String> terrains = """
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
        """.tokenize()

    PwRegion() {
    }

    PwRegion(Shuffler shuffler) {
        super(shuffler)
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(adjectives)} ${ -> pick(terrains)}",
                "${ -> pick(terrains)} of (the) ${ -> pick(nouns)}",
                "The ${ -> pick(adjectives)} ${ -> pick(terrains)}",
                "${ -> pick(nouns)} ${ -> pick(terrains)}",
                "${ -> pick(nouns)}\'s ${ -> pick(adjectives)} ${ -> pick(terrains)}",
                "${ -> pick(adjectives)} ${ -> pick(terrains)} of (the) ${ -> pick(nouns)}"
        ]
    }

    String generate() {
        return pick(getFormatters())
    }
}
