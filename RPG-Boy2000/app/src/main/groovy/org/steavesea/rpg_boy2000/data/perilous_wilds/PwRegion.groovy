package org.steavesea.rpg_boy2000.data.perilous_wilds

import org.steavesea.rpg_boy2000.data.AbstractGenerator
import org.steavesea.rpg_boy2000.data.Shuffler
import org.steavesea.rpg_boy2000.data.RpgBoyData

import javax.inject.Inject

class PwRegion extends AbstractGenerator {
    public String getName() {
        return "Region"
    }
    public String getDataset() {
        return RpgBoyData.PERILOUS_WILDS
    }


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

    @Inject
    PwRegion(Shuffler shuffler) {
        super(shuffler);
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

}
