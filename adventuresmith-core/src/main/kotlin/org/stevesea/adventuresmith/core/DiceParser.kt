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

import com.github.salomonbrys.kodein.*
import me.sargunvohra.lib.cakeparse.api.*
import me.sargunvohra.lib.cakeparse.parser.*
import java.util.*

object DiceParserTokens {
    val number = token("number", "[0-9]+")

    val plus = token("plus", "\\+")
    val minus = token("plus", "\\-")
    val times = token("times", "\\*")

    val lPar = token("lPar", "\\(")
    val rPar = token("rPar", "\\)")

    val d = token ("d", "d")

    val space = token("space", "[ \\t\\r]+", ignore = true)
}

class DiceParser(override val kodein: Kodein) : KodeinAware {
    val random : Random = instance()


    fun roll(nDice: Int, nSides: Int) : Int {
        var sum = 0
        for (i in 1..nDice)
            sum += random.nextInt(nSides) + 1
        return sum
    }

    class Rules {
        // convenience references for recursive rules
        val exprRef: Parser<Int> = ref { expr }
        val multExprRef: Parser<Int> = ref { multExpr }
        val dExprRef: Parser<Int> = ref { dExpr }
        val addExprRef: Parser<Int> = ref { addExpr }

        // actual rules
        val parenExpr = DiceParserTokens.lPar then exprRef before DiceParserTokens.rPar

        val primExpr = DiceParserTokens.number map { it.raw.toInt() } or parenExpr

        val dExpr = primExpr and optional(DiceParserTokens.d and dExprRef) map { exp ->
            val (left, d) = exp
            d?.let {
                val (op, right) = it
                when (op.type) {
                    DiceParserTokens.d -> roll(left, right)
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val multExpr = dExpr and optional(DiceParserTokens.times and multExprRef) map { exp ->
            val (left, mult) = exp
            mult?.let {
                val (op, right) = it
                when (op.type) {
                    DiceParserTokens.times -> left * right
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val addExpr = multExpr and optional((DiceParserTokens.plus or DiceParserTokens.minus) and addExprRef) map { exp ->
            val (left, mult) = exp
            mult?.let {
                val (op, right) = it
                when (op.type) {
                    DiceParserTokens.plus -> left + right
                    DiceParserTokens.minus -> left - right
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val expr: Parser<Int> = addExpr

    }
    val allTokens = setOf(
            DiceParserTokens.number,
            DiceParserTokens.plus,
            DiceParserTokens.minus,
            DiceParserTokens.times,

            DiceParserTokens.lPar,
            DiceParserTokens.rPar,

            DiceParserTokens.space,
            DiceParserTokens.d
    )
}
