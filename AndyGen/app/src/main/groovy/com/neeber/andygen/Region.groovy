package com.neeber.andygen

import java.util.concurrent.ThreadLocalRandom

class Region {
    static final String[] adjectives = """
        Ageless Ashen Black Blessed Blighted Blue Broken Burning Cold Cursed
        Dark Dead Deadly Deep Desolate Diamond Dim Dismal Dun Eerie Endless
        Fallen Far Fell Flaming Forgotten Forsaken Frozen Glittering Golden
        Green Grim Holy Impassable Jagged Light Long Misty Perilous Purple Red
        Savage Shadowy Shattered Shifting Shining Silver White Wicked Yellow
        """.split()

    static final String[] nouns = """
        Ash Bone Darkness Dead Death Desolation Despair Devil Doom Dragon Fate
        Fear Fire Fury Ghost Giant God Gold Heaven Hell Honor Hope Horror King
        Life Light Lord Mist Peril Queen Rain Refuge Regret Savior Shadow Silver
        Skull Sky Smoke Snake Sorrow Storm Sun Thorn Thunder Traitor Troll
        Victory Witch
        """.split()

    static final String[] terrains = """
        Bay Bluffs Bog Cliffs Desert Downs Dunes Expanse Fells Fen Flats
        Foothills Forest Groves Heath Heights Hills Hollows Jungle Lake
        Lowland March Marsh Meadows Moor Morass Mounds Mountains Peaks Plains
        Prairie Quagmire Range Reach Sands Savanna Scarps Sea Slough Sound
        Steppe Swamp Sweep Teeth Thicket Upland Wall Waste Wasteland Woods
        """.split()


    static String getRandomItem(String[] items) {
        return items[ThreadLocalRandom.current().nextInt(items.size())]
    }

    static String generate() {
        String adjective = getRandomItem(adjectives)
        String noun = getRandomItem(nouns)
        String terrain = getRandomItem(terrains)

        def formatters = [
                "${adjective} ${terrain}",
                "${terrain} of (the) ${noun}",
                "The ${adjective} ${terrain}",
                "${noun} ${terrain}",
                "${noun}\'s ${adjective} ${terrain}",
                "${adjective} ${terrain} of (the) ${noun}"
        ]
        return formatters.get(ThreadLocalRandom.current().nextInt(formatters.size()))
    }

    static String[] generate(int num) {
        def strings = []
        num.times {
            strings << generate()
        }
        return strings as String[]
    }



}