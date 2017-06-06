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

class MYZDiceGenerator(
        val myid: String,
        val myname: String,
        val shuffler: Shuffler) : Generator, KLoggable {
    override val logger = logger()

    override fun getId(): String {
        return myid
    }

    companion object {
        val BASE = "base"
        val SKILL = "skill"
        val GEAR = "gear"

        val RADIOACTIVE = "{cmd_radioactive}"
        val BIOHAZARD = "{cmd_biohazard}"
        val GEAR_BREAK = "{cmd_flash_outline}" // cmd-fire, cmd-flash-outline?

        val MYZ_DIE_MAP = linkedMapOf (
                BASE to RangeMap()
                    .with(1, BIOHAZARD)
                    .with(2, "{cmd_dice_2}")
                    .with(3, "{cmd_dice_3}")
                    .with(4, "{cmd_dice_4}")
                    .with(5, "{cmd_dice_5}")
                    .with(6, RADIOACTIVE),
                SKILL to RangeMap()
                    .with(1, "{cmd_dice_1}")
                    .with(2, "{cmd_dice_2}")
                    .with(3, "{cmd_dice_3}")
                    .with(4, "{cmd_dice_4}")
                    .with(5, "{cmd_dice_5}")
                    .with(6, RADIOACTIVE),
                GEAR to RangeMap()
                    .with(1, GEAR_BREAK)
                    .with(2, "{cmd_dice_2}")
                    .with(3, "{cmd_dice_3}")
                    .with(4, "{cmd_dice_4}")
                    .with(5, "{cmd_dice_5}")
                    .with(6, RADIOACTIVE)
        )
        val MYZ_DIE_COLOR = linkedMapOf(
                BASE to "ff8f00",
                SKILL to "2e7d32",
                GEAR to "424242"
        )
    }

    override fun generate(locale: Locale, input: Map<String, String>?): String {
        val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

        val resultLines = mutableListOf<String>()

        var totalRadioactive = 0
        var totalBiohazard = 0
        var totalGear = 0

        // iterate over each type of die
        MYZ_DIE_MAP.forEach {
            val n = inputMapForContext.getOrElse(it.key) { "0" }.toString().toInt()
            if (n > 0) {
                val curDice = "$n "

                val sb = StringBuilder()

                // TODO: font doesn't work in IconicsTextView. How to have both icons and color?!?!?
                sb.append("<font color=\"#${MYZ_DIE_COLOR[it.key]}\">")
                sb.append("${titleCase(it.key)}: ")

                for (i in 1..n) {
                    val roll = shuffler.pick(it.value)
                    if (RADIOACTIVE == roll) {
                        totalRadioactive++
                    }
                    if (BIOHAZARD == roll) {
                        totalBiohazard++
                    }
                    if (GEAR_BREAK == roll) {
                        totalGear++
                    }
                    sb.append(roll)
                }
                sb.append("</font>")
                resultLines.add(sb.toString())
            }
        }
        if (resultLines.isEmpty()) {
            return "You must configure your dice pool."
        }
        if (totalRadioactive > 0 || totalBiohazard > 0 || totalGear > 0) {
            val sb = StringBuilder()
            sb.append("Totals:")
            if (totalRadioactive > 0) {
                sb.append(" $RADIOACTIVE : $totalRadioactive")
            }
            if (totalBiohazard > 0) {
                sb.append(" $BIOHAZARD : $totalBiohazard")
            }
            if (totalGear > 0) {
                sb.append(" $GEAR_BREAK : $totalGear")
            }
            resultLines.add(sb.toString())
        }

        return resultLines.joinToString("<br/><br/>")
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return GeneratorMetaDto(name = myname,
                useIconicsTextView = true,
                input = GeneratorInputDto(
                        displayTemplate = """
                            <big>
                            {{#$BASE}}{{$BASE}}base{{/$BASE}}{{#$SKILL}} {{$SKILL}}skill{{/$SKILL}}{{#$GEAR}} {{$GEAR}}gear{{/$GEAR}}
                            """,
                        useWizard = false,
                        params = listOf(
                                InputParamDto(name = BASE, uiName = "# of $BASE die (yellow)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = "",
                                        helpText = "$BASE (yellow), $SKILL (green), $GEAR (black):"),
                                InputParamDto(name = SKILL, uiName = "# of $SKILL die (green)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = ""),
                                InputParamDto(name = GEAR, uiName = "# of $GEAR die (black)", numbersOnly = true, isInt = true,
                                        maxVal = 10, nullIfZero = true, defaultValue = "")
                        )
                ))
    }

}