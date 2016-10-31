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

class DiceTest {

    fun getDiceFactory(): (String) -> Dice {
        val kodein = getKodein(getMockRandom(0))
        val diceFactory :  (String) -> Dice = kodein.factory()

        return diceFactory
    }

    @Test
    fun testStringParsingModifier() {


        val d = getDiceFactory().invoke("2d20+4")
        Assert.assertEquals(2, d.nDice)
        Assert.assertEquals(20, d.nSides)
        Assert.assertEquals(4, d.modifier)

        Assert.assertEquals(6, d.roll())
        Assert.assertEquals("2d20+4", d.toString())
    }

    @Test
    fun testStringParsing() {
        val d = getDiceFactory().invoke("34d12")
        Assert.assertEquals(34, d.nDice)
        Assert.assertEquals(12, d.nSides)
        Assert.assertEquals(0, d.modifier)

        Assert.assertEquals(34, d.roll())
        Assert.assertEquals("34d12", d.toString())

        Assert.assertEquals(listOf(34,34,34), d.rollN(3))
    }

}
