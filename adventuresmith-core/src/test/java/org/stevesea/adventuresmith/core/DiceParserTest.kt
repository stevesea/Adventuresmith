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

import me.sargunvohra.lib.cakeparse.api.*
import me.sargunvohra.lib.cakeparse.parser.*
import org.junit.*
import kotlin.test.*

class DiceParserTest {

    private fun lex(input: String) = DiceParser.allTokens.lexer().lex(input)

    private fun parse(input: String) = lex(input).parseToEnd(DiceParser.Rules.expr).value


    @Test
    fun simple() = assertEquals(2, parse("1+1"))

    @Test
    fun complex() = assertEquals(57, parse("(6*4)+5+7*4"))
    @Test
    fun dice() = assertEquals(3, parse("2d6+1"))

    // 'd' higher precedence than mult
    @Test
    fun dice2() = assertEquals(5, parse("2d6*2+1d4"))
    @Test
    fun dice3() = assertEquals(3, parse("2d(6*2)+1d4"))

}

object DiceParser {
    object Tokens {
        val number = token("number", "[0-9]+")

        val plus = token("plus", "\\+")
        val minus = token("plus", "\\-")
        val times = token("times", "\\*")

        val lPar = token("lPar", "\\(")
        val rPar = token("rPar", "\\)")

        val d = token ("d", "d")

        val space = token("space", "[ \\t\\r]+", ignore = true)
    }

    fun roll(nDice: Int, nSides: Int) : Int {
        var sum = 0
        for (i in 1..nDice)
            sum += 1 // random.nextInt(nSides) + 1
        return sum
    }

    object Rules {
        // convenience references for recursive rules
        val exprRef: Parser<Int> = ref { expr }
        val multExprRef: Parser<Int> = ref { multExpr }
        val dExprRef: Parser<Int> = ref { dExpr }
        val addExprRef: Parser<Int> = ref { addExpr }

        // actual rules
        val parenExpr = Tokens.lPar then exprRef before Tokens.rPar

        val primExpr = Tokens.number map { it.raw.toInt() } or parenExpr

        val dExpr = primExpr and optional(Tokens.d and dExprRef) map { exp ->
            val (left, d) = exp
            d?.let {
                val (op, right) = it
                when (op.type) {
                    Tokens.d -> roll(left, right)
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val multExpr = dExpr and optional(Tokens.times and multExprRef) map { exp ->
            val (left, mult) = exp
            mult?.let {
                val (op, right) = it
                when (op.type) {
                    Tokens.times -> left * right
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val addExpr = multExpr and optional((Tokens.plus or Tokens.minus) and addExprRef) map { exp ->
            val (left, mult) = exp
            mult?.let {
                val (op, right) = it
                when (op.type) {
                    Tokens.plus -> left + right
                    Tokens.minus -> left - right
                    else -> throw IllegalStateException()
                }
            } ?: left
        }

        val expr: Parser<Int> = addExpr

    }
    val allTokens = setOf(
            Tokens.number,
            Tokens.plus,
            Tokens.minus,
            Tokens.times,

            Tokens.lPar,
            Tokens.rPar,

            Tokens.space,
            Tokens.d
    )
}
