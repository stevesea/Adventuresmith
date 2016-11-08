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
import me.sargunvohra.lib.cakeparse.api.*
import me.sargunvohra.lib.cakeparse.parser.*
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

    // TODO: throws NumberFormatException if not an int
    if (indD == -1 && indPlus == -1) {
        return DiceDef(nDice = 0,
                nSides = 0,
                modifier = diceStr.toInt())
    }

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

class DiceParser(override val kodein: Kodein) : KodeinAware {
    val random : Random = instance()

    fun roll(diceStr: String) : Int {
        return parse(diceStr)
    }
    fun rollN(diceStr: String, n: Int) : List<Int> {
        val result: MutableList<Int> = mutableListOf()
        for (i in 1..n) {
            result.add(roll(diceStr))
        }
        return result
    }


    private fun roll(nDice: Int, nSides: Int) : Int {
        var sum = 0
        for (i in 1..nDice)
            sum += random.nextInt(nSides) + 1
        return sum
    }

    private fun lex(input: String) = allTokens.lexer().lex(input)
    private fun parse(input: String) = lex(input).parseToEnd(expr).value

    companion object {
        val number = token("number", "[0-9]+")

        val plus = token("plus", "\\+")
        val minus = token("plus", "\\-")
        val times = token("times", "\\*")

        val lPar = token("lPar", "\\(")
        val rPar = token("rPar", "\\)")

        val d = token ("d", "d")

        val space = token("space", "[ \\t\\r]+", ignore = true)

        val allTokens = setOf(
                number,
                plus,
                minus,
                times,

                lPar,
                rPar,

                space,
                d
        )
    }

    // convenience references for recursive rules
    val exprRef: Parser<Int> = ref { expr }
    val multExprRef: Parser<Int> = ref { multExpr }
    val dExprRef: Parser<Int> = ref { dExpr }
    val addExprRef: Parser<Int> = ref { addExpr }

    // actual rules
    val parenExpr = lPar then exprRef before rPar

    val primExpr = number map { it.raw.toInt() } or parenExpr

    val dExpr = primExpr and optional(d and dExprRef) map { exp ->
        val (left, dRight) = exp
        dRight?.let {
            val (op, right) = it
            when (op.type) {
                d -> roll(left, right)
                else -> throw IllegalStateException()
            }
        } ?: left
    }

    val multExpr = dExpr and optional(times and multExprRef) map { exp ->
        val (left, mult) = exp
        mult?.let {
            val (op, right) = it
            when (op.type) {
                times -> left * right
                else -> throw IllegalStateException()
            }
        } ?: left
    }

    val addExpr = multExpr and optional((plus or minus) and addExprRef) map { exp ->
        val (left, mult) = exp
        mult?.let {
            val (op, right) = it
            when (op.type) {
                plus -> left + right
                minus -> left - right
                else -> throw IllegalStateException()
            }
        } ?: left
    }

    val expr: Parser<Int> = addExpr
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

    bind<DiceParser>() with singleton {
        DiceParser(kodein)
    }

}
