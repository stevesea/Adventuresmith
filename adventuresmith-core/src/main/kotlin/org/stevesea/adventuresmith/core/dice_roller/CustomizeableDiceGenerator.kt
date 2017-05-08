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

import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorInputDto
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.InputParamDto
import java.text.NumberFormat
import java.util.Locale

/**
 * custom dice all share the same generator logic and similar metadata. but need to have separate
 * generator IDs so that the user can have multiple custom dice configurations.
 */
class CustomizeableDiceGenerator(
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

        val dStr = "${nDie}d$die"

        val result = keptDiceSum + add
        val nf = NumberFormat.getInstance(locale)

        val sb = StringBuilder()
        sb.append("$dStr: [")
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
        sb.append("]<br/><br/>Total: <big><strong>${nf.format(result)}</strong></big> = $afterDroppedHighAndLow + $add")
        if (tnVal.isNotEmpty()) {
            val successes = afterDroppedHighAndLow.count { it >= tn }
            sb.append("<br/><br/>Successes: <big><strong>$successes</strong></big> (>= $tn)")
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