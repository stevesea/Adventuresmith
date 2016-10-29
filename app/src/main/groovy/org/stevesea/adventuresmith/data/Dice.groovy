/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.data

import groovy.transform.CompileStatic

@CompileStatic
class Dice {
    Random random = new Random();
    Integer nDice = 1
    Integer nSides = 6
    Integer mod = 0

    /**
     *
     * @param diceStr like "3d6" or something
     * @param r
     * @return
     */
    static Dice dice(String diceStr, Random r = new Random()) {
        int indPlus = diceStr.indexOf("+")
        int indD = diceStr.indexOf("d")

        Dice d = new Dice(
                nDice: diceStr.substring(0, indD).toInteger(),
                nSides: diceStr.substring(indD+1, indPlus == -1 ? diceStr.length() : indPlus).toInteger(),
                mod: indPlus == -1 ? 0 : diceStr.substring(indPlus+1, diceStr.length()).toInteger(),
                random: r
        )
        return d
    }

    Integer roll() {
        int sum = 0
        nDice.times {
            sum += random.nextInt(nSides) + 1
        }
        return sum + mod
    }

    static Dice dice(Integer numDice, Integer numSides, Integer modifier, Random r = new Random()) {
        return new Dice(nDice: numDice, nSides: numSides, mod: modifier, random: r)
    }

    static Dice dice(Integer numDice, Integer numSides, Random r = new Random()) {
        return dice(numDice, numSides, 0, r)
    }

    static Integer roll(Integer numDice, Integer numSides, Integer modifier, Random r = new Random()) {
        return dice(numDice, numSides, modifier, r).roll()
    }

    static Integer roll(Integer nDice, Integer sides, Random r = new Random()) {
        return roll(nDice, sides, 0, r)
    }

    static Integer roll(String diceStr, Random r = new Random()) {
        return dice(diceStr, r).roll()
    }

    @Override
    public String toString() {
        return "${nDice}d${nSides}${mod != 0 ? "+${mod}" : ""}"
    }
}
