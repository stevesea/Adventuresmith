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

import mu.KLoggable
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorInputDto
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.InputParamDto
import java.text.NumberFormat
import java.util.Locale

class ExplodingDiceGenerator( val myid: String,
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

            logger.debug("Rolled: $rolls. matches: $matched_rolls ($numToRoll)")
            if (numIters > 100) {
                logger.info("Too many explodes. abandon ship!")
                collected_rolls.add(listOf(0))
                break
            }
        } while (matched_rolls.isNotEmpty())

        logger.debug(" ... done. Collected rolls: $collected_rolls")

        val dStrSb = StringBuilder("${nDie}d$die!")

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
        sb.append("$dStr: <big><strong>${nf.format(sum)}</strong></big><br/><br/>")
        sb.append(collected_rolls.map {
            it -> it.map {
            it2 -> if ((eGreater > 0 && it2 >= eGreater) || (eEqual > 0 && it2 == eEqual)) "<strong>$it2</strong>" else "$it2"
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