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

package org.stevesea.adventuresmith.data_k

import java.security.*
import java.util.*

class Dice(val nSides: Int = 1,
           val nDice: Int = 6,
           val modifier: Int = 0,
           private val random: Random = SecureRandom()) {

    fun roll(): Int {
        var sum = 0
        for (i in 1..nDice) {
            sum += random.nextInt(nSides) + 1  // plus1 because nextInt is zero-based
        }
        return sum + modifier
    }

    fun rollN(n: Int): List<Int> {
        var result: MutableList<Int> = mutableListOf()
        for (i in 1..n) {
            result.add(roll())
        }
        return result
    }

    override fun toString(): String {
        return "${nDice}d${nSides}+${modifier}"
    }

    companion object Factory {
        fun create(diceStr: String, random: Random = SecureRandom()): Dice {
            val indPlus = diceStr.indexOf('+')
            val indD = diceStr.indexOf('d')

            return Dice(
                    nDice = diceStr.substring(0..indD).toInt(),
                    nSides = diceStr.substring((indD+1)..(if (indPlus == -1) diceStr.length else indPlus)).toInt(),
                    modifier = if (indPlus == -1) 0 else diceStr.substring((indPlus+1)..(diceStr.length)).toInt(),
                    random = random
            )
        }

        fun roll(diceStr: String, random: Random = SecureRandom()) : Int {
            return create(diceStr, random).roll()
        }

        fun rollN(n: Int, diceStr: String, random: Random = SecureRandom()) : List<Int> {
            return create(diceStr, random).rollN(n)
        }
    }
}
