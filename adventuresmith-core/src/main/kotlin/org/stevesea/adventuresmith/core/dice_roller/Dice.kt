/*
 * Copyright (c) 2017 Steve Christensen
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

package org.stevesea.adventuresmith.core.dice_roller

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.github.salomonbrys.kodein.singleton
import me.sargunvohra.lib.cakeparse.api.and
import me.sargunvohra.lib.cakeparse.api.before
import me.sargunvohra.lib.cakeparse.api.lexer
import me.sargunvohra.lib.cakeparse.api.map
import me.sargunvohra.lib.cakeparse.api.optional
import me.sargunvohra.lib.cakeparse.api.or
import me.sargunvohra.lib.cakeparse.api.parseToEnd
import me.sargunvohra.lib.cakeparse.api.ref
import me.sargunvohra.lib.cakeparse.api.then
import me.sargunvohra.lib.cakeparse.api.token
import me.sargunvohra.lib.cakeparse.exception.LexerException
import me.sargunvohra.lib.cakeparse.parser.Parser
import mu.KLoggable
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorInputDto
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.InputParamDto
import org.stevesea.adventuresmith.core.RangeMap
import org.stevesea.adventuresmith.core.Shuffler
import org.stevesea.adventuresmith.core.getFinalPackageName
import java.text.NumberFormat
import java.util.Locale
import java.util.Random

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
    val GROUP = getFinalPackageName(this.javaClass)

    val regularDice =
            listOf(
            "1d3",
            "1d4",
            "1d6",
            "1d8",
            "1d10",
            "1d12",
            "1d20",
            "1d30",
            "1d100"
    ).map { "$GROUP/$it" to it }.toMap()

    val fudgeDice = mapOf(
            "$GROUP/NdF_1" to "Fudge Dice #1",
            "$GROUP/NdF_2" to "Fudge Dice #2"
    )

    val explodingDice = mapOf(
            "$GROUP/XdY!_1" to "Exploding Dice #1",
            "$GROUP/XdY!_2" to "Exploding Dice #2",
            "$GROUP/XdY!_3" to "Exploding Dice #3"
    )

    // if we just want to roll dice, these could just be in the 'regularDice' above. But, i like
    // showing the individual rolls too
    val multDice = mapOf(
            "$GROUP/2d6" to Pair(2, "1d6"),
            "$GROUP/3d6" to Pair(3, "1d6"),
            "$GROUP/4d4" to Pair(4, "1d4")
    )

    val d20adv = "$GROUP/1d20 advantage"
    val d20disadv = "$GROUP/1d20 disadvantage"

    val customizableDice = mapOf(
            "$GROUP/xdy_z_1" to "Custom Dice #1",
            "$GROUP/xdy_z_2" to "Custom Dice #2",
            "$GROUP/xdy_z_3" to "Custom Dice #3",
            "$GROUP/xdy_z_4" to "Custom Dice #4",
            "$GROUP/xdy_z_5" to "Custom Dice #5",
            "$GROUP/xdy_z_6" to "Custom Dice #6"
    )

    val eoteDice = mapOf(
            "$GROUP/eote_1" to "EotE Dice #1",
            "$GROUP/eote_2" to "EotE Dice #2",
            "$GROUP/eote_3" to "EotE Dice #3"
    )

    val generators = listOf(
        DiceConstants.regularDice.keys,
        DiceConstants.multDice.keys,
        listOf(
            DiceConstants.d20adv,
            DiceConstants.d20disadv
        ),
        DiceConstants.customizableDice.keys,
        DiceConstants.fudgeDice.keys,
        DiceConstants.explodingDice.keys,
        DiceConstants.eoteDice.keys
        ).flatten()
}

val diceModule = Kodein.Module {
    // simple rolls
    DiceConstants.regularDice.forEach {
        bind<Generator>(it.key) with provider {
            object : Generator {
                override fun getId(): String {
                    return it.key
                }

                override fun getMetadata(locale: Locale): GeneratorMetaDto {
                    return GeneratorMetaDto(name = it.value)
                }

                val diceParser: DiceParser = instance()
                override fun generate(locale: Locale, input: Map<String, String>?): String {
                    val nf = NumberFormat.getInstance(locale)
                    return "${it.value}: <strong>${nf.format(diceParser.roll(it.value))}</strong>"
                }
            }
        }
    }
    // mutliple rolls
    DiceConstants.multDice.forEach {
        bind<Generator>(it.key) with provider {
            object : Generator {
                override fun getId(): String {
                    return it.key
                }

                override fun getMetadata(locale: Locale): GeneratorMetaDto {
                    return GeneratorMetaDto(name = it.key.split("/")[1])
                }

                val diceParser: DiceParser = instance()
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
        object : Generator {
            val diceParser: DiceParser = instance()
            override fun getId(): String {
                return DiceConstants.d20adv
            }

            override fun generate(locale: Locale, input: Map<String, String>?): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = diceParser.rollN("1d20", 2)
                val best = rolls.max()
                return "2d20 Advantage: <strong>${nf.format(best)}</strong> <small>${rolls}</small>"
            }

            override fun getMetadata(locale: Locale): GeneratorMetaDto {
                return GeneratorMetaDto(name = "2d20 Advantage")
            }
        }
    }

    bind<Generator>(DiceConstants.d20disadv) with provider {
        object : Generator {
            val diceParser: DiceParser = instance()
            override fun getId(): String {
                return DiceConstants.d20disadv
            }

            override fun generate(locale: Locale, input: Map<String, String>?): String {
                val nf = NumberFormat.getInstance(locale)
                val rolls = diceParser.rollN("1d20", 2)
                val worst = rolls.min()
                return "2d20 Disadvantage: <strong>${nf.format(worst)}</strong> <small>${rolls}</small>"
            }

            override fun getMetadata(locale: Locale): GeneratorMetaDto {
                return GeneratorMetaDto(name = "2d20 Disadvantage")
            }
        }
    }

    DiceConstants.customizableDice.forEach {
        bind<Generator>(it.key) with provider {
            object : CustomizeableDiceGenerator(it.key, it.value, instance()) {}
        }
    }

    DiceConstants.explodingDice.forEach {
        bind<Generator>(it.key) with provider {
            object : ExplodingDiceGenerator(it.key, it.value, instance()) {}
        }
    }

    DiceConstants.fudgeDice.forEach {
        bind<Generator>(it.key) with provider {
            object : FudgeDiceGenerator(it.key, it.value, instance()) {}
        }
    }

    DiceConstants.eoteDice.forEach {
        bind<Generator>(it.key) with provider {
            object : EotEDiceGenerator(it.key, it.value, instance()) {}
        }
    }

    bind<DiceParser>() with singleton {
        DiceParser(kodein)
    }
}

/**
 * custom dice all share the same generator logic and similar metadata. but need to have separate
 * generator IDs so that the user can have multiple custom dice configurations.
 */
abstract class CustomizeableDiceGenerator(
        val myid: String,
        val myname: String,
        val diceParser: DiceParser) : Generator {
    override fun getId(): String {
        return myid
    }
    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

        val die = (inputMapForContext.getOrElse("y") { "6" }).toString().toInt()
        val nDie = (inputMapForContext.getOrElse("x") { "1" }).toString().toInt()
        val add = (inputMapForContext.getOrElse("z") { "0" }).toString().toInt()

        val dropNHighVal = inputMapForContext.getOrElse("dropNHigh") { "" }.toString()
        val dropNHigh = if (dropNHighVal.isNullOrEmpty()) 0 else dropNHighVal.toInt()

        val dropNLowVal = inputMapForContext.getOrElse("dropNLow") { "" }.toString()
        val dropNLow = if (dropNLowVal.isNullOrEmpty()) 0 else dropNLowVal.toInt()

        val tnVal = inputMapForContext.getOrElse("tn") { "" }.toString()
        val tn = if (tnVal.isNullOrEmpty()) 0 else tnVal.toInt()

        val rolls = diceParser.rollN("1d" + die, nDie).sortedDescending()
        val droppedHigh = rolls.take(dropNHigh)
        val afterDroppedHigh = rolls.drop(dropNHigh)
        val droppedLow = afterDroppedHigh.takeLast(dropNLow)
        val afterDroppedHighAndLow = afterDroppedHigh.dropLast(dropNLow)

        val keptDiceSum = afterDroppedHighAndLow.sum()

        val dStr = "${nDie}d${die}"

        val result = keptDiceSum + add
        val nf = NumberFormat.getInstance(locale)

        val sb = StringBuilder()
        sb.append("${dStr}: [")
        if (droppedHigh.isNotEmpty()) {
            val droppedHighStr = droppedHigh.joinToString(", ", prefix = "<strike><small>", postfix = "</small></strike>")
            sb.append(droppedHighStr)
        }
        if (afterDroppedHighAndLow.isNotEmpty()) {
            if (droppedHigh.isNotEmpty()) {
                sb.append(", ")
            }
            sb.append(afterDroppedHighAndLow.joinToString(", ", prefix = "<strong>", postfix = "</strong>"))
        }
        if (droppedLow.isNotEmpty()) {
            if (droppedHigh.isNotEmpty() || afterDroppedHighAndLow.isNotEmpty()) {
                sb.append(", ")
            }
            val droppedLowStr = droppedLow.joinToString(", ", prefix = "<strike><small>", postfix = "</small></strike>")
            sb.append(droppedLowStr)
        }
        sb.append("]<br/><br/>Total: <big><strong>${nf.format(result)}</strong></big> = ${afterDroppedHighAndLow} + $add")
        if (tnVal.isNotEmpty()) {
            val successes = afterDroppedHighAndLow.count { it >= tn }
            sb.append("<br/><br/>Successes: <big><strong>${successes}</strong></big> (>= ${tn})")
        }
        return sb.toString()
    }
    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                input = GeneratorInputDto(
                        displayTemplate = "<big>{{x}}d{{y}} + {{z}}</big>{{#dropNHigh}}<br/>Drop {{dropNHigh}} Highest{{/dropNHigh}}{{#dropNLow}}<br/>Drop {{dropNLow}} Lowest{{/dropNLow}}{{#tn}}<br/>Target number: {{tn}}{{/tn}}",
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = "x", uiName = "X (# of die)", numbersOnly = true, isInt = true, defaultValue = "1",
                                        maxVal = 1000, minVal = 1,
                                        helpText = "Enter dice 'XdY + Z'"),
                                InputParamDto(name = "y", uiName = "Y (# of sides)", numbersOnly = true, isInt = true, defaultValue = "6"),
                                InputParamDto(name = "z", uiName = "Z", numbersOnly = true, isInt = true, defaultValue = "0"),
                                InputParamDto(name = "dropNHigh", uiName = "Drop N Highest", numbersOnly = true, isInt = true,
                                        nullIfZero = true, defaultValue = "",
                                        helpText = "Do you want to drop any rolls?"),
                                InputParamDto(name = "dropNLow", uiName = "Drop M Lowest", numbersOnly = true, isInt = true,
                                        nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = "tn", uiName = "Target Number", numbersOnly = true, isInt = true,
                                        nullIfZero = true, defaultValue = "",
                                        helpText = "Target number? (success if roll >= TN)")
                        )
                )
        )
    }
}

abstract class ExplodingDiceGenerator( val myid: String,
                                       val myname: String,
                                       val diceParser: DiceParser) : Generator, KLoggable {
    override val logger = logger()
    override fun getId(): String {
        return myid
    }

    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

        val die = (inputMapForContext.getOrElse("y") { "6" }).toString().toInt()
        val nDie = (inputMapForContext.getOrElse("x") { "1" }).toString().toInt()

        val eGreaterVal = inputMapForContext.getOrElse("eGreater") { "" }.toString()
        val eGreater = if (eGreaterVal.isNullOrEmpty()) 0 else eGreaterVal.toInt()

        val eEqualVal = inputMapForContext.getOrElse("eEqual") { "" }.toString()
        var eEqual = if (eEqualVal.isNullOrEmpty()) 0 else eEqualVal.toInt()

        if (eGreater == 0 && eEqual == 0) {
            // if neither has been set, use the die #
            eEqual = die
        }
        if (eGreater > 0) {
            // if eGreater has been set, only use eGreater (as is displayed by template)
            eEqual = 0
        }

        val collected_rolls : MutableList<List<Int>> = mutableListOf()

        var numIters = 0
        var numToRoll = nDie
        do {
            numIters++
            val rolls = diceParser.rollN("1d" + die, numToRoll)
            collected_rolls.add(rolls)

            val matched_rolls = rolls.filter {
                (eGreater > 0 && it >= eGreater) || (eEqual > 0 && it == eEqual)
            }

            numToRoll = matched_rolls.size

            logger.debug("Rolled: ${rolls}. matches: ${matched_rolls} ($numToRoll)")
            if (numIters > 100) {
                logger.info("Too many explodes. abandon ship!")
                collected_rolls.add(listOf(0))
                break
            }
        } while (matched_rolls.isNotEmpty())

        logger.debug(" ... done. Collected rolls: ${collected_rolls}")

        val dStrSb = StringBuilder("${nDie}d${die}!")

        if (eGreater > 0)
            dStrSb.append(">" + eGreater)
        else if (eEqual > 0)
            dStrSb.append(eEqual)
        else if (eGreater == 0 && eEqual == 0)
            dStrSb.append(die)

        val dStr = dStrSb.toString()

        val sum = collected_rolls.map { it -> it.sum() }.sum()

        val nf = NumberFormat.getInstance(locale)

        val sb = StringBuilder()
        sb.append("${dStr}: <big><strong>${nf.format(sum)}</strong></big><br/><br/>")
        sb.append(collected_rolls.map {
            it -> it.map {
            it -> if ((eGreater > 0 && it >= eGreater) || (eEqual > 0 && it == eEqual)) "<strong>$it</strong>" else "$it"
        }.joinToString(", ", "[", "]")
        }.joinToString(" + <br/>"))

        return sb.toString()
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                input = GeneratorInputDto(
                        displayTemplate = "<big>{{x}}d{{y}}!{{#eGreater}}>{{eGreater}}{{/eGreater}}{{^eGreater}}{{#eEqual}}{{eEqual}}{{/eEqual}}{{/eGreater}}{{^eGreater}}{{^eEqual}}{{y}}{{/eEqual}}{{/eGreater}}</big>",
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = "x", uiName = "X (# of die)", numbersOnly = true, isInt = true, defaultValue = "1",
                                        maxVal = 100, minVal = 1,
                                        helpText = "Enter dice 'XdY'"),
                                InputParamDto(name = "y", uiName = "Y (# of sides)", numbersOnly = true, isInt = true, defaultValue = "6"),
                                InputParamDto(name = "eGreater", uiName = ">E", numbersOnly = true, isInt = true, minVal = 2,
                                        nullIfZero = true, defaultValue = "",
                                        helpText = "Explode if greater than or equal to:"),
                                InputParamDto(name = "eEqual", uiName = "=E", numbersOnly = true, isInt = true,
                                        nullIfZero = true, defaultValue = "",
                                        helpText = "Explode if equal to:")
                        )
                )
        )
    }

}

abstract class FudgeDiceGenerator(
        val myid: String,
        val myname: String,
        val shuffler: Shuffler) : Generator {

    companion object {
        val fudgeMap = RangeMap()
                .with(1, "-")
                .with(2, "-")
                .with(3, " ")
                .with(4, " ")
                .with(5, "+")
                .with(6, "+")
    }

    override fun getId(): String {
        return myid
    }

    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)
        val nf = NumberFormat.getInstance(locale)

        val n = inputMapForContext.getOrElse("n") { "1" }.toString().toInt()
        val rolls: MutableList<String> = mutableListOf()
        var sum = 0
        for (i in 1..n) {
            val roll = shuffler.pick(fudgeMap)
            if (roll == "-")
                sum--
            if (roll == "+")
                sum++
            rolls.add(roll)
        }

        return "${n}dF: ${rolls}<br/><br/><big><strong>${nf.format(sum)}</strong></big>"
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                input = GeneratorInputDto(
                        displayTemplate = "<big>{{n}}dF</big>",
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = "n", uiName = "N (# of die)", numbersOnly = true, isInt = true, defaultValue = "4", minVal = 1,
                                        helpText = "How many dF to roll?")
                        )
                ))
    }

}

abstract class EotEDiceGenerator(
        val myid: String,
        val myname: String,
        val shuffler: Shuffler) : Generator, KLoggable {
    override val logger = logger()

    override fun getId(): String {
        return myid
    }

    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

        val diceStrSb = StringBuffer()

        var successFailureSum = 0
        var advantageThreatSum = 0
        var triumphCount = 0
        var despairCount = 0
        var darkSum = 0
        var lightSum = 0

        val all_rolls : MutableMap<String, MutableList<String>> = linkedMapOf()

        // iterate over each type of die
        EOTE_DIE_MAP.forEach {
            val n = inputMapForContext.getOrElse(it.key) { "0" }.toString().toInt()
            if (n > 0) {
                val curDice = "$n ${it.key}"
                diceStrSb.append("$n${it.key.first()}") // first char for dice-notation display

                all_rolls.put(curDice, mutableListOf())

                for (i in 1..n) {
                    val roll = shuffler.pick(it.value)
                    if (roll.isNullOrBlank())
                        continue
                    logger.debug("${it.key}: $roll")
                    val rolls = roll.split("+").map { it.trim() }
                    all_rolls.get(curDice)?.add(roll)

                    rolls.forEach {
                        when (it) {
                            SUCCESS -> {
                                successFailureSum++
                            }
                            FAILURE -> {
                                successFailureSum--
                            }

                            ADVANTAGE -> {
                                advantageThreatSum++
                            }
                            THREAT -> {
                                advantageThreatSum--
                            }
                            TRIUMPH -> {
                                successFailureSum++
                                triumphCount++
                            }
                            DESPAIR -> {
                                successFailureSum--
                                despairCount++
                            }
                            DARK -> {
                                darkSum ++
                            }
                            LIGHT -> {
                                lightSum ++
                            }
                        }
                    }
                }
            }
        }
        val diceStr = diceStrSb.toString()

        if (diceStr.isNullOrBlank()) {
            return "You must configure your dice pool."
        }

        val resultLines : MutableList<String> = mutableListOf()
        if (successFailureSum > 0) {
            resultLines.add("Success: <strong>$successFailureSum</strong>")
        } else if (successFailureSum < 0) {
            resultLines.add("Failure: <strong>$successFailureSum</strong>")
        }

        if (advantageThreatSum > 0) {
            resultLines.add("Advantage: <strong>$advantageThreatSum</strong>")
        } else if (advantageThreatSum < 0) {
            resultLines.add("Threat: <strong>$advantageThreatSum</strong>")
        }

        if (darkSum > 0 || lightSum > 0) {
            resultLines.add("Light: <strong>$lightSum</strong> &nbsp;&nbsp;&nbsp;Dark: <strong>$darkSum</strong>")
        }

        if (triumphCount > 0) {
            if (resultLines.isNotEmpty())
                resultLines.add("")
            resultLines.add("Triumph: <strong>$triumphCount</strong>")
        }
        if (despairCount > 0) {
            if (resultLines.isNotEmpty())
                resultLines.add("")
            resultLines.add("Despair: <strong>$despairCount</strong>")
        }
        if (resultLines.isNotEmpty())
            resultLines.add("")
        resultLines.add("<small>$diceStrSb</small>")
        resultLines.add("<small>" + all_rolls.map { "&nbsp;&nbsp;${it.key}: ${it.value.joinToString(", ")}" }.joinToString("<br/>") + "</small>")

        return resultLines.joinToString("<br/>")
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                input = GeneratorInputDto(
                        displayTemplate = """
                            <big>
                            {{#$ABILITY}}{{$ABILITY}}a{{/$ABILITY}}{{#$PROFICIENCY}}{{$PROFICIENCY}}p{{/$PROFICIENCY}}{{#$BOOST}}{{$BOOST}}b{{/$BOOST}}
                            {{#$DIFFICULTY}}{{$DIFFICULTY}}d{{/$DIFFICULTY}}{{#$CHALLENGE}}{{$CHALLENGE}}c{{/$CHALLENGE}}{{#$SETBACK}}{{$SETBACK}}s{{/$SETBACK}}
                            {{#$FORCE}}{{$FORCE}}f{{/$FORCE}}
                            """,
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = ABILITY, uiName = "# of $ABILITY die (green)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = "",
                                        helpText = "$ABILITY (green), $PROFICIENCY (yellow), $BOOST (blue):"),
                                InputParamDto(name = PROFICIENCY, uiName = "# of $PROFICIENCY die (yellow)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = BOOST, uiName = "# of $BOOST die (blue)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = DIFFICULTY, uiName = "# of $DIFFICULTY die (purple)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = "",
                                        helpText = "$DIFFICULTY (purple), $CHALLENGE (red), $SETBACK (black):"),
                                InputParamDto(name = CHALLENGE, uiName = "# of $CHALLENGE die (red)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = SETBACK, uiName = "# of $SETBACK die (black)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = FORCE, uiName = "# of $FORCE die (white)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = "")
                        )
                ))
    }
    companion object {
        val BLANK = "-"
        val SUCCESS = "Success"
        val FAILURE = "Failure"
        val TRIUMPH = "Triumph"
        val DESPAIR = "Despair"
        val ADVANTAGE = "Advantage"
        val THREAT = "Threat"
        val DARK = "Dark"
        val LIGHT = "Light"

        val FORCE = "force"
        val BOOST = "boost"
        val SETBACK = "setback"
        val ABILITY = "ability"
        val DIFFICULTY = "difficulty"
        val PROFICIENCY = "proficiency"
        val CHALLENGE = "challenge"

        val EOTE_DIE_MAP = linkedMapOf(
                ABILITY to RangeMap()
                        .with(1, BLANK)
                        .with(2, SUCCESS)
                        .with(3, SUCCESS)
                        .with(4, SUCCESS + " + " + SUCCESS)
                        .with(5, ADVANTAGE)
                        .with(6, ADVANTAGE)
                        .with(7, SUCCESS + " + " + ADVANTAGE)
                        .with(8, ADVANTAGE + " + " + ADVANTAGE),
                PROFICIENCY to RangeMap()
                        .with(1, BLANK)
                        .with(2, SUCCESS)
                        .with(3, SUCCESS)
                        .with(4, SUCCESS + " + " + SUCCESS)
                        .with(5, SUCCESS + " + " + SUCCESS)
                        .with(6, ADVANTAGE)
                        .with(7, SUCCESS + " + " + ADVANTAGE)
                        .with(8, SUCCESS + " + " + ADVANTAGE)
                        .with(9, SUCCESS + " + " + ADVANTAGE)
                        .with(10, ADVANTAGE + " + " + ADVANTAGE)
                        .with(11, ADVANTAGE + " + " + ADVANTAGE)
                        .with(12, TRIUMPH),
                BOOST to RangeMap()
                        .with(1, BLANK)
                        .with(2, BLANK)
                        .with(3, SUCCESS)
                        .with(4, SUCCESS + " + " + ADVANTAGE)
                        .with(5, ADVANTAGE + " + " + ADVANTAGE)
                        .with(6, ADVANTAGE),
                DIFFICULTY to RangeMap()
                        .with(1, BLANK)
                        .with(2, FAILURE)
                        .with(3, FAILURE + " + " + FAILURE)
                        .with(4, THREAT)
                        .with(5, THREAT)
                        .with(6, THREAT)
                        .with(7, THREAT + " + " + THREAT)
                        .with(8, FAILURE + " + " + THREAT),
                CHALLENGE to RangeMap()
                        .with(1, BLANK)
                        .with(2, FAILURE)
                        .with(3, FAILURE)
                        .with(4, FAILURE + " + " + FAILURE)
                        .with(5, FAILURE + " + " + FAILURE)
                        .with(6, THREAT)
                        .with(7, THREAT)
                        .with(8, FAILURE + " + " + THREAT)
                        .with(9, FAILURE + " + " + THREAT)
                        .with(10, THREAT + " + " + THREAT)
                        .with(11, THREAT + " + " + THREAT)
                        .with(12, DESPAIR),
                SETBACK to RangeMap()
                        .with(1, BLANK)
                        .with(2, BLANK)
                        .with(3, FAILURE)
                        .with(4, FAILURE)
                        .with(5, THREAT)
                        .with(6, THREAT),
                FORCE to RangeMap()
                        .with(1, DARK)
                        .with(2, DARK)
                        .with(3, DARK)
                        .with(4, DARK)
                        .with(5, DARK)
                        .with(6, DARK)
                        .with(7, DARK + " + " + DARK)
                        .with(8, LIGHT)
                        .with(9, LIGHT)
                        .with(10, LIGHT + " + " + LIGHT)
                        .with(11, LIGHT + " + " + LIGHT)
                        .with(12, LIGHT + " + " + LIGHT)
        )

    }

}
