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
        Assert.assertEquals("All-Seeing Arrow", getGenerator(FotfConstants.SPELLS,1).generate(Locale.ENGLISH))
        Assert.assertEquals("Blade of Black Avarice", getGenerator(FotfConstants.SPELLS,5).generate(Locale.ENGLISH))
        Assert.assertEquals("Bolt of Bolcas", getGenerator(FotfConstants.SPELLS,10).generate(Locale.ENGLISH))
        Assert.assertEquals("Concealing Boon of Canderol", getGenerator(FotfConstants.SPELLS,12).generate(Locale.ENGLISH))
        Assert.assertEquals("Bane of Anger", getGenerator(FotfConstants.SPELLS,3).generate(Locale.ENGLISH))
    }

    @Test
    fun spell_french() {
        Assert.assertEquals("Éther Clairvoyant", getGenerator(FotfConstants.SPELLS,1).generate(Locale.FRANCE))
        Assert.assertEquals("Aura de/des/de de l'Air", getGenerator(FotfConstants.SPELLS,2).generate(Locale.FRANCE))
        Assert.assertEquals("Fléau du/de la Fléau Grisant", getGenerator(FotfConstants.SPELLS,3).generate(Locale.FRANCE))
        Assert.assertEquals("Équilibre Flamboyant de Asmoasta", getGenerator(FotfConstants.SPELLS,6).generate(Locale.FRANCE))
        Assert.assertEquals("Bénédiction de Rouille de Bahabalia", getGenerator(FotfConstants.SPELLS,7).generate(Locale.FRANCE))
    }

    @Test
    fun char_english() {
        Assert.assertEquals("""
<html>
  <body>
    <h4>
      Playbook - Fighter
    </h4>
    <h5>
      Name & Heritage
    </h5>
    <p>
      Alodia
      <em>
        Female
      </em>
      <br>
      </br>
      Human Evil
    </p>
    <h5>
      Abilities
    </h5>
    <p>
      Strength: 6&nbsp;&nbsp;Dexterity: 6
      <br>
      </br>
      Charisma: 6&nbsp;&nbsp;Constitution: 6
      <br>
      </br>
      Wisdom: 6&nbsp;&nbsp;Intelligence: 6
      <br>
      </br>
      Luck: 6
    </p>
    <h5>
      Virtues & Vices
    </h5>
    <p>
      Addict<br/>Alcoholic<br/>Antagonistic
    </p>
    <h5>
      Gear
    </h5>
    <p>
      Chainmail<br/>Shield
    </p>
  </body>
</html>""".trim(), getGenerator(FotfConstants.CHARS,1).generate(Locale.ENGLISH))

    }




}

