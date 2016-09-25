package com.neeber.andygen.data.maze_rats

import com.neeber.andygen.data.Shuffler
import com.neeber.andygen.data.AbstractGenerator

import javax.inject.Inject;

class MazeRatsItems extends AbstractGenerator {
    static final List<String> items = """Amulet
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
Wine""".readLines()

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
}
