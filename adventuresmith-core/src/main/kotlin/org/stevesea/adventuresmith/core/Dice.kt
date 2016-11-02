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

package org.stevesea.adventuresmith.core

import com.github.salomonbrys.kodein.*
import java.util.*


class Dice(val nSides: Int = 1,
           val nDice: Int = 6,
           val modifier: Int = 0,
           override val kodein: Kodein) : KodeinAware {
    val random : Random = instance()

    fun roll(): Int {
        var sum = 0
        for (i in 1..nDice) {
            sum += random.nextInt(nSides) + 1  // plus1 because nextInt is zero-based
        }
        return sum + modifier
    }

    fun rollN(n: Int): List<Int> {
        val result: MutableList<Int> = mutableListOf()
        for (i in 1..n) {
            result.add(roll())
        }
        return result
    }

    override fun toString(): String {
        val modstr = if (modifier == 0) "" else "+${modifier}"
        return "${nDice}d${nSides}${modstr}"
    }
}

data class DiceDef(val nSides: Int = 1,
                   val nDice: Int = 6,
                   val modifier: Int = 0)

fun diceStrToDef(diceStr: String) : DiceDef {
    val trimmed = diceStr.trim()
    val indPlus = trimmed.indexOf('+')
    val indD = trimmed.indexOf('d')

    // TODO: throw? maybe should just log and return a 1d1 or something
    if (indD == -1) throw IllegalArgumentException("invalid dice string: '$trimmed'")

    try {
        return DiceDef(
                nDice = trimmed.substring(0..(indD - 1)).toInt(),
                nSides = trimmed.substring((indD + 1)..(if (indPlus == -1) trimmed.length else indPlus) - 1).toInt(),
                modifier = if (indPlus == -1) 0 else trimmed.substring((indPlus + 1)..(trimmed.length - 1)).toInt()
        )
    } catch (ex: Exception) {
        throw IllegalArgumentException("invalid dice string: '$trimmed' : ${ex.message}")
    }
}

