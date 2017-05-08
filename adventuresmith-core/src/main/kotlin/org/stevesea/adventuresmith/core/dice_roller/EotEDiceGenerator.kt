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
import java.util.Locale

class EotEDiceGenerator(
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
                    all_rolls[curDice]?.add(roll)

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