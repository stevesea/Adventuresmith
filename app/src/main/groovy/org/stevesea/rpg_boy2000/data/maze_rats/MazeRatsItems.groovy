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
class MazeRatsItems extends AbstractGenerator {
    public String getName() {
        return "Items"
    }
    public String getDataset() {
        return RpgBoyData.MAZERATS
    }
    static final List<String> items = """\
Amulet
Arrow
Arrowhead
Axe
Bell
Belt
Boots
Bottle
Bow
Bowl
Box
Bracelet
Breastplate
Brooch
Candle
Card
Censer
Circlet
Claw
Cloak
Coin
Comb
Compass
Conch
Crown
Cup
Doll
Egg
Eye
Eyepatch
Fang
Feather
Figurine
Finger
Flute
Gauntlet
Gem
Glove
Hammer
Handkerchief
Hat
Helm
Horn
Hourglass
Jar
Key
Knife
Lamp
Lens
Locket
Lute
Lyre
Mace
Machine
Mask
Mirror
Necklace
Needle
Net
Orb
Painting
Pearl
Pen
Phial
Pillow
Pipe
Pipes
Purse
Puzzle Box
Pyramid
Razor
Ring
Rod
Rook
Rope
Salve
Scarf
Scepter
Scissors
Scroll
Shield
Shoe
Signet Ring
Skull
Slippers
Snuffbox
Spear
Staff
Strand
Sword
Thread
Tome
Tooth
Torch
Tuning Fork
Turnip
Wand
Whetstone
Whistle
Wine\
""".readLines()

    @Inject
    MazeRatsItems(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                "${ -> pick(MazeRatsMagic.elements)} ${ -> pick(items)}",
                "${ -> pick(MazeRatsMagic.effects)} ${ -> pick(items)}",
                "${ -> pick(MazeRatsMagic.effects)} ${ -> pick(MazeRatsMagic.elements)} ${ -> pick(items)}",
                "${ -> pick(items)} of ${ -> pick(MazeRatsMagic.elements)}",
                "${ -> pick(items)} of ${ -> pick(MazeRatsMagic.effects)} ${ -> pick(MazeRatsMagic.elements)}",
                "${ -> pick(MazeRatsMagic.forms)} ${ -> pick(items)}",
        ]
    }

    String generate() {
        return pick(getFormatters())
    }
}
