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

import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class FotfGeneratorTest {
    @Test
    fun spell_english() {
        Assert.assertEquals("All-Seeing Armor", FotfSpellGenerator(GeneratorTest.getShuffler()).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Black Avarice", FotfSpellGenerator(GeneratorTest.getShuffler(5)).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Bolcas", FotfSpellGenerator(GeneratorTest.getShuffler(10)).generate(Locale.ENGLISH))
        Assert.assertEquals("Concealing Armor of Canderol", FotfSpellGenerator(GeneratorTest.getShuffler(12)).generate(Locale.ENGLISH))
        Assert.assertEquals("Armor of Anger", FotfSpellGenerator(GeneratorTest.getShuffler(3)).generate(Locale.ENGLISH))
    }
    @Test
    fun spell_french() {
        Assert.assertEquals("Éther Clairvoyant", FotfSpellGenerator(GeneratorTest.getShuffler()).generate(Locale.FRANCE))
        Assert.assertEquals("Armure de/des/de de l'Air", FotfSpellGenerator(GeneratorTest.getShuffler(2)).generate(Locale.FRANCE))
        Assert.assertEquals("Armure du/de la Aura Grisant", FotfSpellGenerator(GeneratorTest.getShuffler(3)).generate(Locale.FRANCE))
        Assert.assertEquals("Équilibre Flamboyant de Asmoasta", FotfSpellGenerator(GeneratorTest.getShuffler(6)).generate(Locale.FRANCE))
        Assert.assertEquals("Armure de Rouille de Bahabalia", FotfSpellGenerator(GeneratorTest.getShuffler(7)).generate(Locale.FRANCE))
    }
    @Test
    fun exerciser() {
        for(i in 1..50) {
            println(FotfSpellGenerator().generate(Locale.ENGLISH))
        }
        for(i in 1..50) {
            println(FotfSpellGenerator().generate(Locale.FRANCE))
        }
    }

}

