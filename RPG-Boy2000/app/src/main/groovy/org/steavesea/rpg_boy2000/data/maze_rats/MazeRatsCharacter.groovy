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

import org.steavesea.rpg_boy2000.data.AbstractGenerator
import org.steavesea.rpg_boy2000.data.RpgBoyData
import org.steavesea.rpg_boy2000.data.Shuffler

import javax.inject.Inject

class MazeRatsCharacter extends AbstractGenerator {
    public String getName() {
        return "Character"
    }
    public String getDataset() {
        return RpgBoyData.MAZERATS
    }
    static final List<String> forenames = """\
Adelaide
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
Zora\
""".readLines()

    static final List<String> surnames = """\
Barrow
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
Wilberforce\
""".readLines()

    static final List<String> personalities = """\
Arrogant
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
World-weary\
""".readLines()

    static final List<String> appearances = """\
Acid Scars
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
Wild Hair\
""".readLines()


    static final List<String> weapons = """\
Ancient Spear (d6)
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
Woodman’s axe (d8)\
""".readLines()

    static final List<String> equipment = """\
Acid
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
Whistle\
""".readLines()


    @Inject
    MazeRatsCharacter(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
"""\
<strong><small>Name</small>: <em>${ -> pick(forenames)} ${ -> pick(surnames)}</em></strong>
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small>STR: ${ -> rollDice(3, 6)} DEX: ${ -> rollDice(3, 6)} WIL: ${ -> rollDice(3, 6)} HP: ${ -> rollDice(1, 6)}</small>
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Personality</small></strong>: ${ -> pick(personalities, 2).join(", ")}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Appearance</small></strong>:  ${ -> pick(appearances, 2).join(", ")}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Weapons</small></strong>:     ${ -> pick(weapons, 2).join(", ")}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;<strong><small>Equip</small></strong>:       ${ -> pick(equipment, 3).join(", ")}
"""
        ]
    }

}
