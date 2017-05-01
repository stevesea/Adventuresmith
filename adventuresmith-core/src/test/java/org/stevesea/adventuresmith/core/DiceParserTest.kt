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
import org.junit.*
import org.stevesea.adventuresmith.core.dice_roller.DiceParser
import kotlin.test.*

class DiceParserTest {

    private fun parse(input: String) : Int {
        val kodein = getKodein(getMockRandom(0))
        val diceParser : DiceParser = kodein.instance()
        return diceParser.roll(input)
    }

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

