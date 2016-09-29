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
package org.stevesea.rpgpad.data.maze_rats

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.Shuffler

import javax.inject.Inject

@CompileStatic
class MazeRatsMagic extends AbstractGenerator {
    static final List<String> effects = """\
Absorbing
Accelerating
Attracting
Awakening
Bewildering
Binding
Blazing
Blinding
Blossoming
Cacophonous
Concealing
Condemning
Consuming
Creeping
Crushing
Deflecting
Devastating
Dicing
Diminishing
Disguising
Dispelling
Duplicating
Empowering
Enchanting
Enlightening
Enraging
Ensorcelling
Entangling
Enveloping
Excruciating
Expanding
Fearsome
Flaming
Floating
Freezing
Grasping
Gyrating
Haunting
Helpful
Hindering
Hovering
Illusory
Imprisoning
Instantaneous
Inverting
Invigorating
Invisible
Liquefying
Luminous
Maddening
Mesmerizing
Nullifying
Obscuring
Oozing
Opening
Perceptive
Pestilential
Petrifying
Phasing
Piercing
Planar
Poisonous
Polymorphing
Pursuing
Rearranging
Rebounding
Reflecting
Rejuvenating
Repeating
Repelling
Restorative
Restraining
Revealing
Reversing
Revolting
Revolving
Screaming
Scrying
Sealing
Shielding
Silent
Slashing
Sleeping
Smoking
Soothing
Subtle
Summoning
Sweeping
Terrifying
Thirsty
Thundering
Transmuting
Transporting
Transposing
Untiring
Vaporizing
Vengeful
Voracious
Warding
Withering\
""".readLines()

    static final List<String> forms = """\
Acid
Aether
Air
Alabaster
Amber
Ash
Bat
Battle
Beetle
Bile
Blight
Blood
Bone
Brimstone
Brine
Bronze
Chaos
Clay
Copper
Crow
Crystal
Night
Death
Doom
Dream
Dust
Earth
Echo
Energy
Fire
Flame
Flesh
Fog
Fungus
Ghost
Glass
Gold
Heat
Honey
Ice
Ichor
Insect
Iron
Ivory
Jade
Lava
Light
Lightning
Loam
Marmalade
Miasma
Milk
Mist
Moss
Mud
Mutation
Nectar
Nightmare
Obsidian
Oil
Plague
Poison
Power
Psyche
Quicksilver
Rain
Rat
Rose
Rot
Rust
Salt
Sand
Sap
Serpent
Shadow
Silver
Skin
Slime
Smoke
Snow
Souls
Spirit
Star
Steam
Stench
Stone
Sun
Tar
Thorn
Thunder
Treasure
Venom
Vine
Void
Water
Wind
Wine
Winter
Wood
Worm\
""".readLines()

    static final List<String> elements = """\
Arc
Assassin
Aura
Bastion
Beacon
Beam
Beast
Blade
Blast
Blob
Bolt
Bubble
Burst
Call
Cascade
Circle
Cloud
Coil
Colossus
Column
Cone
Crystal
Cube
Disk
Elemental
Emanation
Enclosure
Explosion
Eye
Face
Field
Fist
Fountain
Gate
Gaze
Golem
Grip
Gush
Halo
Hand
Heart
Helix
Image
Laugh
Lock
Loop
Maze
Moment
Monolith
Mouth
Nexus
Oracle
Path
Pattern
Plane
Portal
Prism
Pulse
Pyramid
Ray
Rift
Road
Scream
Seal
Sentinel
Servant
Shard
Shield
Shroud
Sigil
Song
Sphere
Spiral
Splinter
Spray
Steed
Storm
Stream
Strike
Swarm
Tendril
Tentacle
Throne
Tongue
Torrent
Touch
Tower
Trap
Tree
Tunnel
Veil
Voice
Vortex
Wall
Ward
Wave
Web
Whisper
Word
Zone\
""".readLines()

    @Inject
    MazeRatsMagic(Shuffler shuffler) {
        super(shuffler)
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(elements)} ${ -> pick(forms)}",
                "${ -> pick(effects)} ${ -> pick(forms)}",
                "${ -> pick(effects)} ${ -> pick(elements)}",
                "${ -> pick(effects)} ${ -> pick(elements)} ${ -> pick(forms)}",
                "${ -> pick(forms)} of ${ -> pick(elements)}",
                "${ -> pick(forms)} of ${ -> pick(effects)} ${ -> pick(elements)}",
        ]
    }
    String generate() {
        return pick(getFormatters())
    }
}
