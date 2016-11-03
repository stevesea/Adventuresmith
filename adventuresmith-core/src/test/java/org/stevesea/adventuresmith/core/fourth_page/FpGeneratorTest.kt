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

package org.stevesea.adventuresmith.core.fourth_page

import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class FpGeneratorTest {

    @Test
    fun testFpArtifact() {
        Assert.assertEquals("""
<html>
  <body>
    <h4>
      Artifact
    </h4>
    <h5>
      Origin
    </h5>
    <p>
      <strong>
        <small>
          Magic
        </small>
      </strong>
      - It's powered by a trapped soul.
    </p>
    <h5>
      Power
    </h5>
    <p>
      <strong>
        <small>
          Curse
        </small>
      </strong>
      - You've been polymorphed.
    </p>
  </body>
</html>
""".trim(), getGenerator(FpConstants.ARTIFACT, 1).generate(Locale.ENGLISH))
    }

    @Test
    fun testFpDungeon() {
        Assert.assertEquals("""
<html>
  <body>
    <h4>
      Dungeon
    </h4>
    <h5>
      History
    </h5>
    <p>
      <strong>
        <small>
          Downfall
        </small>
      </strong>
      - The war left the land in ruins.
    </p>
    <h5>
      Denizen
    </h5>
    <p>
      <strong>
        <small>
          Creature
        </small>
      </strong>
      - A dragon has made this its lair.
    </p>
    <h5>
      Trial
    </h5>
    <p>
      <strong>
        <small>
          Hazard
        </small>
      </strong>
      - The air is filled with poison spores.
    </p>
    <h5>
      Secret
    </h5>
    <p>
      <strong>
        <small>
          Lore
        </small>
      </strong>
      - There's something strange about this map.
    </p>
  </body>
</html>
""".trim(), getGenerator(FpConstants.DUNGEON, 1).generate(Locale.ENGLISH))
    }

    @Test
    fun testFpCity() {
        Assert.assertEquals("""
<html>
  <body>
    <h4>
      City
    </h4>
    <h5>
      Feature
    </h5>
    <p>
      <strong>
        <small>
          Resource
        </small>
      </strong>
      - There's good hunting here.
    </p>
    <h5>
      Population
    </h5>
    <p>
      <strong>
        <small>
          Caste
        </small>
      </strong>
      - You never know what the mages are up to.
    </p>
    <h5>
      Society
    </h5>
    <p>
      <strong>
        <small>
          Guild
        </small>
      </strong>
      - There's no honour amongst these thieves.
    </p>
    <h5>
      Trouble
    </h5>
    <p>
      <strong>
        <small>
          Threat
        </small>
      </strong>
      - The sick have been quarantined.
    </p>
  </body>
</html>
""".trim(),  getGenerator(FpConstants.CITY, 1).generate(Locale.ENGLISH))

    }

    @Test
    fun testFpMonster() {
        Assert.assertEquals("""
<html>
  <body>
    <h4>
      Monster
    </h4>
    <h5>
      Nature
    </h5>
    <p>
      <strong>
        <small>
          Construct
        </small>
      </strong>
      - They repair their own injuries.
    </p>
    <h5>
      Role
    </h5>
    <p>
      <strong>
        <small>
          Brute
        </small>
      </strong>
      - It gets in your way.
    </p>
  </body>
</html>
""".trim(),  getGenerator(FpConstants.MONSTER, 1).generate(Locale.ENGLISH))

    }

}

