package com.neeber.andygen.data.maze_rats

import com.neeber.andygen.data.AbstractGenerator
import com.neeber.andygen.data.Shuffler

import javax.inject.Inject

class MazeRatsCharacter extends AbstractGenerator {
    static final List<String> forenames = """Adelaide
Balthazar
Barsaba
Basil
Beatrix
Bertram
Bianca
Blaxton
Chadwick
Cleopha
Clover
Constance
Cromwell
Damaris
Daphne
Demona
Destrian
Elsbeth
Erasmus
Esme
Faustus
Fern
Finn
Forthwind
Fox
Godwin
Hannibal
Hester
Hippolyta
Jasper
Jiles
Jilly
Jules
Marga
Merrick
Minerva
Mortimer
Odette
Ogden
Olga
Oswald
Pepper
Percival
Peregrine
Phoebe
Piety
Poppy
Quentin
Redmaine
Silas
Silence
Stilton
Stratford
Sybil
Tenpiece
Trilby
Tuesday
Ursula
Webster
Zora""".readLines()

    static final List<String> surnames = """Barrow
Beetleman
Belvedere
Birdwhistle
Bithesea
Bobich
Calaver
Carvolo
Chips
Coffin
Crumpling
De Rippe
Digworthy
Dregs
Droll
Dunlow
Erelong
Fernsby
Fisk
Gimble
Girdwood
Gorgos
Graveworm
Greelish
Grimeson
Gruger
Hardwick
Hitheryon
Hovel
Knibbs
La Marque
Loverly
Midnighter
Mitre
Nethercoat
Oblington
Onymous
Pestle
Phillifent
Relish
Romatet
Rothery
Rumbold
Rummage
Sallow
Saltmarsh
Silverless
Skitter
Skorbeck
Slee
Slitherly
Stavish
Stoker
Tarwater
Vandermeer
Villin
Wellbelove
Westergren
Wexley
Wilberforce""".readLines()

    static final List<String> personalities = """Arrogant
Avant-Garde
Boastful
Bored
Bossy
Can-do
Chatterbox
Chirpy
Cryptic
Ditz
Egomaniac
Extravagant
Fast-talker
Flake
Flirtatious
Gossip
Hard-boiled
Hears voices
Hillbilly
Hothead
Iconoclast
Idealistic
Illiterate
Jerk
Klutz
Love-struck
Misanthrope
Mopey
Naïve
Nerd
No-nonsense
Obsessive
Orator
Overeducated
Paranoid
Pouty
Prickly
Proselytizer
Refined
Ruthless
Self-pitying
Serene
Slacker
Slimy
Slovenly
Snarky
Snitch
Snob
Sophist
Spacey
Thick
Toady
Twitchy
Vain
Vegan
Vengeful
Whiner
Wild Child
Wisecracking
World-weary""".readLines()

    static final List<String> appearances = """Acid Scars
Battle Scars
Boney hands
Braided Hair
Brawny
Broken Nose
Bulbous Nose
Burn Scars
Bushy Brows
Chiseled
Curly Hair
Dark Skin
Disfigured
Disheveled
Filthy
Gaunt
Gap-toothed
Grey Hair
Groomed
Hairless
Hawk Nose
Immense
Lantern Jaw
Limp
Long Hair
Loud Voice
Meat Hooks
Missing Ear
Missing Eye
Nine Fingers
Oily Skin
Pale Skin
Perfect Skin
Perfect Teeth
Perfumed
Pierced
Plump
Pockmarked
Pointed Chin
Rosy Cheek
Rotten Teeth
Scrubbed
Shaved Head
Shifty Eyes
Short
Slender
Slouched
Smelly
Smiling
Soft Voice
Squinty Eyes
Steely Gaze
Sunken Eyes
Sweaty
Tattooed
Towering
Unsmiling
Weathered
White Hair
Wild Hair""".readLines()


    static final List<String> weapons = """Ancient Spear (d6)
Arming Sword (d6)
Battered Halberd (d8)
Battleaxe (d8)
Bronze Dagger (d6)
Bronze-tip Spear (d6)
Carved Spear (d6)
Claymore (d8)
Crank Crossbow (d10)
Etched Glaive (d8)
Falchion (d8)
Flail (d8)
Gleaming Halberd (d8)
Hand Crossbow (d6)
Hatchet (d6)
Heirloom Glaive (d8)
Hunting Bow (d6)
Hunting Knife (d6)
Iron Club (d6)
Ivory Spear (d6)
Longbow (d8)
Maul (d8)
Messer (d8)
Morningstar (d8)
Obsidian Dagger (d6)
Painted Spear (d6)
Pull Crossbow (d10)
Quarterstaff (d6)
Rapier (d6)
Recurve Bow (d6)
Rusty Mace (d6)
Sling with bullets (d6)
Sling with stones (d6)
Spiked Club (d6)
Steel Dagger (d6)
Stone-tip Spear (d6)
Throwing Knives (d6)
War Bow (d8)
Warhammer (d8)
Woodman’s axe (d8)""".readLines()

    static final List<String> equipment = """Acid
Animal Scent
Antitoxin
Armor
Bear Trap
Bell
Blank Book
Bolt-Cutters
Caltrops
Pliers
Candle
Chain (10 ft.)
Chalk (10)
Copper Wire
Crowbar
Dice
Door Ram
Ether
Falcon
Fiddle
Fire Oil
Fishing Hook
Flashbomb
Glue
Grap. Hook
Grease
Hacksaw
Hammer
Hand Drill
Hog Holder
Hound
Hourglass
Incense
Lantern
Large Sack
Lens
Lock & Key
Lockpicks (5)
Lodestone
Manacles
Marbles
Mule
Net
Pen & Ink
Pickaxe
Poison
Pole (10 ft.)
Potion
Rat
Rum Bottle
Shovel
Smokebomb
Spikes (5)
Spyglass
Stake
Steel Mirror
Tent
Thick Gloves
Trumpet
Whistle""".readLines()


    @Inject
    MazeRatsCharacter(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                """Name: ${ -> pick(forenames)} ${ -> pick(surnames)}
  Personality: ${ -> pick(personalities, 2).join(", ")}
  Appearance:  ${ -> pick(appearances, 2).join(", ")}
  Weapons:     ${ -> pick(weapons, 2).join(", ")}
  Equip:       ${ -> pick(equipment, 3).join(", ")}\n"""
        ]
    }

}
