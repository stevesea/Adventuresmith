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
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.github.salomonbrys.kodein.singleton
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import org.stevesea.adventuresmith.core.getFinalPackageName
import java.text.NumberFormat
import java.util.Locale

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
    val myzDice = mapOf(
            "$GROUP/myz_1" to "MYZ Dice #1",
            "$GROUP/myz_2" to "MYZ Dice #2",
            "$GROUP/myz_3" to "MYZ Dice #3"
    )
    val coriolisDice = mapOf(
            "$GROUP/coriolis_1" to "Coriolis Dice #1",
            "$GROUP/coriolis_2" to "Coriolis Dice #2",
            "$GROUP/coriolis_3" to "Coriolis Dice #3"
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
        DiceConstants.eoteDice.keys,
        DiceConstants.myzDice.keys,
        DiceConstants.coriolisDice.keys
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
                    return "${it.key.split("/")[1]}: <strong>${nf.format(sum)}</strong> <small>${rolls.map { nf.format(it) }}</small>"
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
                return "2d20 Advantage: <strong>${nf.format(best)}</strong> <small>$rolls</small>"
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
                return "2d20 Disadvantage: <strong>${nf.format(worst)}</strong> <small>$rolls</small>"
            }

            override fun getMetadata(locale: Locale): GeneratorMetaDto {
                return GeneratorMetaDto(name = "2d20 Disadvantage")
            }
        }
    }

    DiceConstants.customizableDice.forEach {
        bind<Generator>(it.key) with provider {
            CustomizeableDiceGenerator(it.key, it.value, instance())
        }
    }

    DiceConstants.explodingDice.forEach {
        bind<Generator>(it.key) with provider {
            ExplodingDiceGenerator(it.key, it.value, instance())
        }
    }

    DiceConstants.fudgeDice.forEach {
        bind<Generator>(it.key) with provider {
            FudgeDiceGenerator(it.key, it.value, instance())
        }
    }

    DiceConstants.eoteDice.forEach {
        bind<Generator>(it.key) with provider {
            EotEDiceGenerator(it.key, it.value, instance())
        }
    }
    DiceConstants.myzDice.forEach {
        bind<Generator>(it.key) with provider {
            MYZDiceGenerator(it.key, it.value, instance())
        }
    }
    DiceConstants.coriolisDice.forEach {
        bind<Generator>(it.key) with provider {
            CoriolisDiceGenerator(it.key, it.value, instance())
        }
    }

    bind<DiceParser>() with singleton {
        DiceParser(kodein)
    }
}

