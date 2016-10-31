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

package org.stevesea.adventuresmith.core.fotf

import com.fasterxml.jackson.databind.*
import com.github.salomonbrys.kodein.*
import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.security.*
import java.util.*


data class RangeMapVal(val range: IntRange, val value: String)
data class PlaybooksDto( val playbooks: List<RangeMapVal>,
                         val playbooks2: List<List<String>>,
                         val playbooks3: RangeMap)

class FotfGeneratorTest {
    @Test
    fun yaml_parser_test() {

        val kodein = getKodein(SecureRandom())
        val reader : ObjectReader = kodein.instance()

        val input = """
---
playbooks:
- range:
    start: 1
    end: 3
  value: asdfasdf1
- range: {start: 4, end: 6}
  value: asdfasdf2
- {range: {start: 7, end: 10} , value: asdfasdf3}
playbooks2:
- [1..3, Human]
- [4..6, asdfasdf]
- [7..8, asdfasdfasdf]
playbooks3:
- 1..3, Human
- 1..3, halfling
- 4..6, asdfasdf
- "4..6, asdfasdf : string with colon"
        """

        val dto : PlaybooksDto = reader.forType(PlaybooksDto::class.java).readValue(input)

        Assert.assertEquals(3, dto.playbooks.size)
        Assert.assertEquals(1..3, dto.playbooks.get(0).range)
        Assert.assertEquals(4..6, dto.playbooks.get(1).range)
        Assert.assertEquals(7..10, dto.playbooks.get(2).range)

    }
    @Test
    fun spell_english() {
        Assert.assertEquals("All-Seeing Armor", getGenerator(FotfConstants.SPELLS,1).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Black Avarice", getGenerator(FotfConstants.SPELLS,5).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Bolcas", getGenerator(FotfConstants.SPELLS,10).generate(Locale.ENGLISH))
        Assert.assertEquals("Concealing Armor of Canderol", getGenerator(FotfConstants.SPELLS,12).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Anger", getGenerator(FotfConstants.SPELLS,3).generate(Locale.ENGLISH))
    }
    @Test
    fun spell_french() {
        Assert.assertEquals("Éther Clairvoyant", getGenerator(FotfConstants.SPELLS,1).generate(Locale.FRANCE))
        Assert.assertEquals("Armure de/des/de de l'Air", getGenerator(FotfConstants.SPELLS,2).generate(Locale.FRANCE))
        Assert.assertEquals("Armure du/de la Aura Grisant", getGenerator(FotfConstants.SPELLS,3).generate(Locale.FRANCE))
        Assert.assertEquals("Équilibre Flamboyant de Asmoasta", getGenerator(FotfConstants.SPELLS,6).generate(Locale.FRANCE))
        Assert.assertEquals("Armure de Rouille de Bahabalia", getGenerator(FotfConstants.SPELLS,7).generate(Locale.FRANCE))
    }
    @Test
    fun exerciser() {
        for(i in 1..50) {
            println(getGenerator(FotfConstants.SPELLS).generate(Locale.ENGLISH))
        }
        for(i in 1..50) {
            println(getGenerator(FotfConstants.SPELLS).generate(Locale.FRANCE))
        }
    }

}

