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

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

@CompileStatic
class MazeRatsMonsters extends AbstractGenerator {
    static final List<String> creatures = '''\
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
'''.readLines()

   List<String> templates = [
                '{{element}} {{creature}}',
                '{{effect}} {{element}} {{creature}}',
                '{{effect}} {{creature}}',
                '{{form}} {{creature}}',
                '{{creature2}}',
                '{{effect}} {{creature2}}',
   ]

   String generate() {
       Mustache.compiler().compile(pick(templates) as String).execute(
                [
                        element  : pick(MazeRatsMagic.elements),
                        effect   : pick(MazeRatsMagic.effects),
                        form     : pick(MazeRatsMagic.forms),
                        creature : pick(creatures),
                        creature2: pickN(creatures, 2).join(' ')
                ]
       )
   }
}
