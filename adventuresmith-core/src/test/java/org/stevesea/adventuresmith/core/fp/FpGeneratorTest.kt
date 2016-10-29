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

package org.stevesea.adventuresmith.core.fp

import org.junit.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class FpGeneratorTest : BaseGeneratorTest() {
    @Before
    fun setup() {
        init()
    }

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
          Holy
        </small>
      </strong>
      - It's been lost for centuries.
    </p>
    <h5>
      Power
    </h5>
    <p>
      <strong>
        <small>
          Blessing
        </small>
      </strong>
      - You see visions of the future.
    </p>
  </body>
</html>
""".trim(), FourthPageArtifactGenerator(lockedShuffler!!).generate(Locale.ENGLISH))
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
          Builder
        </small>
      </strong>
      - The walls show signs of dwarven craftsmanship.
    </p>
    <h5>
      Denizen
    </h5>
    <p>
      <strong>
        <small>
          Community
        </small>
      </strong>
      - It's a perfect hideout for the outlaws.
    </p>
    <h5>
      Trial
    </h5>
    <p>
      <strong>
        <small>
          Dilemma
        </small>
      </strong>
      - She's just a child.
    </p>
    <h5>
      Secret
    </h5>
    <p>
      <strong>
        <small>
          Cache
        </small>
      </strong>
      - The library is full of ancient texts.
    </p>
  </body>
</html>
""".trim(), FourthPageDungeonGenerator(lockedShuffler!!).generate(Locale.ENGLISH))
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
          District
        </small>
      </strong>
      - The garrison is a well-oiled machine.
    </p>
    <h5>
      Population
    </h5>
    <p>
      <strong>
        <small>
          Attitude
        </small>
      </strong>
      - They want you gone as soon as possible.
    </p>
    <h5>
      Society
    </h5>
    <p>
      <strong>
        <small>
          Faith
        </small>
      </strong>
      - Your heathen ways will not be tolerated.
    </p>
    <h5>
      Trouble
    </h5>
    <p>
      <strong>
        <small>
          Rumour
        </small>
      </strong>
      - There's treasure in those caves!
    </p>
  </body>
</html>
""".trim(),  FourthPageCityGenerator(lockedShuffler!!).generate(Locale.ENGLISH))

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
          Animal and Plant
        </small>
      </strong>
      - Its barbs are filled with poison.
    </p>
    <h5>
      Role
    </h5>
    <p>
      <strong>
        <small>
          Artillery
        </small>
      </strong>
      - They shoot at you from cover.
    </p>
  </body>
</html>
""".trim(),  FourthPageMonsterGenerator(lockedShuffler!!).generate(Locale.ENGLISH))

    }
}

