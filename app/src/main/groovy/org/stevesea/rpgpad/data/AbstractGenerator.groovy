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
public abstract class AbstractGenerator {
    Shuffler shuffler;

    AbstractGenerator() {
        this.shuffler = new Shuffler()
    }

    AbstractGenerator withShuffler(Shuffler shuff) {
        this.shuffler = shuff
        this
    }

    Shuffler getShuffler() {
        return shuffler;
    }

    def pick(Collection<?> items) {
        return shuffler.pick(items)
    }
    def pick(RangeMap map) {
        return shuffler.pick(map.values())
    }

    def pick(String diceStr, RangeMap map) {
        return map.pick(Dice.dice(diceStr, shuffler.random))
    }

    def pick(String diceStr, Collection<?> items) {
        return shuffler.pick(Dice.dice(diceStr, shuffler.random), items)
    }

    Dice dice(String diceStr) {
        return shuffler.dice(diceStr)
    }

    Collection<?> pickN(Collection<?> items, int num) {
        return shuffler.pickN(items, num)
    }

    Collection<?> pickN(RangeMap map, int num) {
        return shuffler.pickN(map.values(), num)
    }

    int roll(int numDice, int nSides, int modifier=0) {
        return Dice.roll(numDice, nSides, modifier, shuffler.random)
    }

    int roll(String diceStr) {
        return Dice.roll(diceStr, shuffler.random)
    }

    abstract String generate()

    String[] generate(int num) {
        def strings = []
        num.times {
            strings << generate()
        }
        return strings as String[]
    }

    String strong(Object input) {
        return "<strong>${input}</strong>"
    }
    String small(Object input) {
        return "<small>${input}</small>"
    }
    String em(Object input) {
        return "<em>${input}</em>"
    }
    String ss(Object input) {
        return strong(small(input))
    }
    String ssem(Object input) {
        return strong(small(em(input)))
    }
}
