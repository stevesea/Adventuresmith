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
import java.text.*
import java.util.*


class Dice(val nSides: Int = 1,
           val nDice: Int = 6,
           val modifier: Int = 0,
           override val kodein: Kodein) : KodeinAware {
    val random : Random = instance()

    constructor(dd : DiceDef, kodein: Kodein) : this(dd.nSides, dd.nDice, dd.modifier, kodein)

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


object DiceConstants {
    val GROUP = "dice"

    val regularDice = listOf("1d4","1d6","1d8","1d10","1d12","1d20","1d30","1d100")
    val sums = listOf("2d6", "3d6","4d4")

    val d20adv = "1d20adv"
    val d20disadv = "1d20disadv"

}

val diceModule = Kodein.Module {
    // simple rolls
    for (d in DiceConstants.regularDice) {
        bind<Generator>(d) with provider {
            object: Generator {
                val dice = Dice(diceStrToDef(d), kodein)
                override fun generate(locale: Locale): String {
                    val nf = NumberFormat.getInstance(locale)
                    return "${d}: ${nf.format(dice.roll())}"
                }
            }
        }
    }
    // multiple dice, and i want to show individual dice rolls
    for (d in DiceConstants.sums) {
        bind<Generator>(d) with provider {
            object: Generator {
                val dd = diceStrToDef(d)
                val dice = Dice(dd.nSides, 1, 0, kodein)
                override fun generate(locale: Locale): String {
                    val nf = NumberFormat.getInstance(locale)
                    val rolls = dice.rollN(dd.nDice)
                    val sum = rolls.sum()
                    return "${d}: ${nf.format(sum)} <small>${rolls}</small>"
                }
            }
        }
    }

    bind<Generator>(DiceConstants.d20adv) with provider {
        object: Generator {
            val dice = Dice(diceStrToDef("1d20"), kodein)
            override fun generate(locale: Locale): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = dice.rollN(2)
                val best = rolls.max()
                return "${DiceConstants.d20adv}: ${nf.format(best)} <small>${rolls}</small>"
            }
        }
    }

    bind<Generator>(DiceConstants.d20disadv) with provider {
        object: Generator {
            val dice = Dice(diceStrToDef("1d20"), kodein)
            override fun generate(locale: Locale): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = dice.rollN(2)
                val worst = rolls.min()
                return "${DiceConstants.d20disadv}: ${nf.format(worst)} <small>${rolls}</small>"
            }
        }
    }

    bind<List<String>>(DiceConstants.GROUP) with singleton {
        listOf(
                DiceConstants.regularDice,
                DiceConstants.sums,
                listOf(
                        DiceConstants.d20adv,
                        DiceConstants.d20disadv
                )
        ).flatten()
    }

}
