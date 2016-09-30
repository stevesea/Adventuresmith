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

// TODO: use http://groovy-lang.org/templating.html instead? -- needs more groovy libs than is typically in grooid
@CompileStatic
public abstract class AbstractGenerator {

    protected final Shuffler shuffler;

    AbstractGenerator(Shuffler shuffler) {
        this.shuffler = shuffler
    }

    def pick(List<?> items) {
        return shuffler.pick(items)
    }

    def pick(Dice dice, List<?> items) {
        return shuffler.pick(dice, items)
    }
    def pick(String diceStr, List<?> items) {
        return pick(Dice.dice(diceStr), items)
    }

    List<?> pickN(List<?> items, int num) {
        return shuffler.pickN(items, num)
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

    String strong(String input) {
        return "<strong>${input}</strong>"
    }
    String small(String input) {
        return "<small>${input}</small>"
    }
    String ss(String input) {
        return strong(small(input))
    }
}
