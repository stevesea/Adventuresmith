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
import me.sargunvohra.lib.cakeparse.exception.*
import me.sargunvohra.lib.cakeparse.parser.*
import java.text.*
import java.util.*


// TODO: add support for drop lowest/highest, min/max?
// hey, it parses dice! practically identical to the CakeParse sample for calculator
// just adds a 'd' token as the highest priority
class DiceParser(override val kodein: Kodein) : KodeinAware {
    val random : Random = instance()

    fun roll(diceStr: String) : Int {
        try {
            return parse(diceStr)
        } catch (e: LexerException) {
            throw IllegalArgumentException("cannot parse '$diceStr' as dice. ${e.message}")
        }
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
    val CollectionName = "dice_roller"

    val regularDice = listOf("1d4","1d6","1d8","1d10","1d12","1d20","1d30","1d100")

    // if we just want to roll dice, these could just be in the 'regularDice' above. But, i like
    // showing the individual rolls too
    val multDice : Map<String,Pair<Int,String>> = mapOf(
            "2d6" to Pair(2, "1d6"),
            "3d6" to Pair(3, "1d6"),
            "4d4" to Pair(4, "1d4")
    )

    val d20adv = "1d20 advantage"
    val d20disadv = "1d20 disadvantage"

    val customizableDice: Map<String,String> = mapOf(
            "xdy_z_1" to "Custom Dice #1",
            "xdy_z_2" to "Custom Dice #2",
            "xdy_z_3" to "Custom Dice #3"
    )
}

val diceModule = Kodein.Module {
    // simple rolls
    DiceConstants.regularDice.forEach {
        bind<Generator>(it) with provider {
            object: Generator {
                override fun getId(): String {
                    return it
                }
                override fun getMetadata(locale: Locale): GeneratorMetaDto {
                    return GeneratorMetaDto(name = it,
                            collectionId = DiceConstants.CollectionName,
                            priority = DiceConstants.regularDice.indexOf(it))
                }

                val diceParser : DiceParser = instance()
                override fun generate(locale: Locale, input: Map<String, String>?): String {
                    val nf = NumberFormat.getInstance(locale)
                    return "${it}: <strong>${nf.format(diceParser.roll(it))}</strong>"
                }
            }
        }
    }
    // mutliple rolls
    DiceConstants.multDice.forEach {
        bind<Generator>(it.key) with provider {
            object: Generator {
                override fun getId(): String {
                    return it.key
                }
                override fun getMetadata(locale: Locale): GeneratorMetaDto {
                    return GeneratorMetaDto(name = it.key,
                            collectionId = DiceConstants.CollectionName,
                            priority = 100)
                }

                val diceParser : DiceParser = instance()
                override fun generate(locale: Locale, input: Map<String, String>?): String {
                    val nf = NumberFormat.getInstance(locale)
                    val rolls = diceParser.rollN(it.value.second, it.value.first)
                    val sum = rolls.sum()
                    return "${it.key}: <strong>${nf.format(sum)}</strong> <small>${rolls.map { nf.format(it) }}</small>"
                }
            }
        }
    }

    bind<Generator>(DiceConstants.d20adv) with provider {
        object: Generator {
            val diceParser : DiceParser = instance()
            override fun getId(): String {
                return DiceConstants.d20adv
            }
            override fun generate(locale: Locale, input: Map<String, String>?): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = diceParser.rollN("1d20", 2)
                val best = rolls.max()
                return "${DiceConstants.d20adv}: <strong>${nf.format(best)}</strong> <small>${rolls}</small>"
            }
            override fun getMetadata(locale: Locale): GeneratorMetaDto {
                return GeneratorMetaDto(name = "1d20 (adv)", collectionId = DiceConstants.CollectionName, priority = 2000)
            }
        }
    }

    bind<Generator>(DiceConstants.d20disadv) with provider {
        object: Generator {
            val diceParser : DiceParser = instance()
            override fun getId(): String {
                return DiceConstants.d20disadv
            }
            override fun generate(locale: Locale, input: Map<String, String>?): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = diceParser.rollN("1d20", 2)
                val worst = rolls.min()
                return "${DiceConstants.d20disadv}: <strong>${nf.format(worst)}</strong> <small>${rolls}</small>"
            }
            override fun getMetadata(locale: Locale): GeneratorMetaDto {
                return GeneratorMetaDto(name = "1d20 (disadv)", collectionId = DiceConstants.CollectionName, priority = 2000)
            }
        }
    }

    /**
     * custom dice all share the same generator logic and similar metadata. but need to have separate
     * generator IDs so that the user can have multiple custom dice configurations.
     */
    abstract class CustomizeableDiceGenerator(
            val myid: String,
            val myname: String,
            val diceParser: DiceParser): Generator {
        override fun getId(): String {
            return myid
        }
        override fun generate(locale: Locale, input: Map<String, String>?): String {
            val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)
            val dStr = "${inputMapForContext["x"]}d${inputMapForContext["y"]} + ${inputMapForContext["z"]}"
            val nf = NumberFormat.getInstance(locale)
            return "${dStr}: <strong>${nf.format(diceParser.roll(dStr))}</strong>"
        }
        override fun getMetadata(locale: Locale): GeneratorMetaDto {
            return GeneratorMetaDto(name = myname,
                    collectionId = DiceConstants.CollectionName,
                    priority = 3000,
                    input = GeneratorInputDto(
                        displayTemplate = "{{x}}d{{y}} + {{z}}",
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = "x", uiName = "X", numbersOnly = true, defaultValue = "1",
                                        helpText = "Enter dice 'XdY + Z'"),
                                InputParamDto(name = "y", uiName = "Y", numbersOnly = true, defaultValue = "6"),
                                InputParamDto(name = "z", uiName = "Z", numbersOnly = true, defaultValue = "0")
                        )
                    )
            )
        }
    }

    DiceConstants.customizableDice.forEach {
        bind<Generator>(it.key) with provider {
            object: CustomizeableDiceGenerator(it.key, it.value, instance()) {
            }
        }
    }

    bind<List<String>>(DiceConstants.GROUP) with singleton {
        listOf(
                DiceConstants.regularDice,
                DiceConstants.multDice.keys,
                listOf(
                        DiceConstants.d20adv,
                        DiceConstants.d20disadv
                ),
                DiceConstants.customizableDice.keys
        ).flatten()
    }

    bind<DiceParser>() with singleton {
        DiceParser(kodein)
    }

}
