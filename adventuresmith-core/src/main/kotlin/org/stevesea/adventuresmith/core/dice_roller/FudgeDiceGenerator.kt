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
import org.stevesea.adventuresmith.core.RangeMap
import org.stevesea.adventuresmith.core.Shuffler
import java.text.NumberFormat
import java.util.Locale

class FudgeDiceGenerator(
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

        return "${n}dF: $rolls<br/><br/><big><strong>${nf.format(sum)}</strong></big>"
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