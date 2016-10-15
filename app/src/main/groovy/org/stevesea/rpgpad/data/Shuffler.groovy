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
package org.stevesea.rpgpad.data

import groovy.transform.CompileStatic

@CompileStatic
class Shuffler {
    final Random random;

    Shuffler() {
        this(new Random())
    }

    Shuffler(Random random) {
        this.random = random
    }

    def pick(Collection<?> items) {
        return items.getAt(random.nextInt(items.size()))
    }

    def pick(Dice dice, Collection<?> items) {
        // use mod to ensure our index is within the acceptable range for the collection
        // dice are 1-based, list indexes are 0-based so subtract 1
        return items.getAt(dice.roll() % items.size() - 1)
    }

    Dice dice(String diceStr) {
        return Dice.dice(diceStr, random)
    }

    Dice dice(int numDice, int numSides) {
        return Dice.dice(numDice, numSides, random)
    }

    def pick(String diceStr, Collection<?> items) {
        return pick(Dice.dice(diceStr, random), items)
    }

    Collection<?> pickN(Collection<?> items, int num) {
        def local = items.collect()
        Collections.shuffle(local, random)
        return local.take(num)
    }
}
