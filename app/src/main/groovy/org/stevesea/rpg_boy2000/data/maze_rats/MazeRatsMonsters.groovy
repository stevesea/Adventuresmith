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
package org.stevesea.rpg_boy2000.data.maze_rats

import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.AbstractGenerator
import org.stevesea.rpg_boy2000.data.RpgBoyData
import org.stevesea.rpg_boy2000.data.Shuffler

import javax.inject.Inject

@CompileStatic
class MazeRatsMonsters extends AbstractGenerator {
    public String getName() {
        return "Monsters"
    }
    public String getDataset() {
        return RpgBoyData.MAZERATS
    }
    static final List<String> creatures = """\
Ant
Ape
Badger
Bat
Bear
Beaver
Bee
Beetle
Boar
Bulldog
Butterfly
Camel
Cat
Centipede
Chameleon
Cobra
Cockroach
Constrictor
Cougar
Cow
Coyote
Crab
Crane
Cricket
Crocodile
Crow
Cuckoo
Donkey
Dragonfly
Duck
Eagle
Eel
Elephant
Elk
Falcon
Ferret
Firefly
Fox
Frog
Goat
Goose
Hare
Hart
Hawk
Hedgehog
Hornet
Horse
Hound
Hummingbird
Jackal
Jellyfish
Leech
Lion
Locust
Lynx
Mantis
Mastodon
Mockingbird
Mole
Monkey
Moose
Moth
Mouse
Mule
Octopus
Otter
Owl
Ox
Panther
Pig
Pony
Porcupine
Possum
Rabbit
Raccoon
Rat
Reindeer
Rooster
Salamander
Scorpion
Seal
Shark
Sheep
Slug
Snail
Sparrow
Spider
Squid
Squirrel
Tiger
Toad
Turtle
Viper
Vulture
Walrus
Weasel
Whale
Wolf
Wolverine
Worm\
""".readLines()

    @Inject
    MazeRatsMonsters(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(MazeRatsMagic.elements)} ${ -> pick(creatures)}",
                "${ -> pick(MazeRatsMagic.effects)} ${ -> pick(MazeRatsMagic.elements)} ${ -> pick(creatures)}",
                "${ -> pick(MazeRatsMagic.effects)} ${ -> pick(creatures)}",
                "${ -> pick(MazeRatsMagic.forms)} ${ -> pick(creatures)}",
                "${ -> pick(creatures)} ${ -> pick(creatures)}",
                "${ -> pick(MazeRatsMagic.effects)} ${ -> pick(creatures)} ${ -> pick(creatures)}",
        ]
    }

    String generate() {
        return pick(getFormatters())
    }
}