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
import org.stevesea.adventuresmith.core.RangeMap
import org.stevesea.adventuresmith.core.Shuffler
import org.stevesea.adventuresmith.core.titleCase
import java.util.Locale

class CoriolisDiceGenerator(
        val myid: String,
        val myname: String,
        val shuffler: Shuffler) : Generator, KLoggable {
    override val logger = logger()

    override fun getId(): String {
        return myid
    }

    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

        val resultLines = mutableListOf<String>()

        var totalSuccesses = 0

        val n = inputMapForContext.getOrElse("diceTotal") { "0" }.toString().toInt()
        if (n == 0) {
            return "You must configure your dice pool."
        }
        val rolls = mutableListOf<Int>()

        for (i in 1..n) {
            val roll = shuffler.roll("1d6")
            if (roll == 6) {
                totalSuccesses++
            }
            rolls.add(roll)
        }
        when (totalSuccesses) {
            0 -> resultLines.add("<strong><big>Failure - $totalSuccesses</big></strong>")
            in 1..2 -> resultLines.add("<strong><big>Limited Success - $totalSuccesses</big></strong>")
            else -> resultLines.add("<strong><big>Critical Success - $totalSuccesses</big></strong>")
        }

        val rollStrings = rolls.map { it ->
            when(it) {
                6 -> "<strong><big>6</big></strong>"
                else -> it.toString()
            }
        }
        resultLines.add("Rolls: ${rollStrings.joinToString(", ", prefix = "[", postfix = "]")}")

        return resultLines.joinToString("<br/><br/>")
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                input = GeneratorInputDto(
                        displayTemplate = """
                            <big>
                            {{#diceTotal}}Dice Total: {{diceTotal}}{{/diceTotal}}
                            """,
                        useWizard = true,
                        params = listOf(
                                InputParamDto(name = "diceTotal", uiName = "#Total # of Dice in Pool", numbersOnly = true, isInt = true,
                                        maxVal = 50, minVal = 1, nullIfZero = true, defaultValue = "3",
                                        helpText = "Total # of Dice")
                        )
                ))
    }

}