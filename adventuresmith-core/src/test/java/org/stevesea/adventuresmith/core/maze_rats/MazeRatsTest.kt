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

package org.stevesea.adventuresmith.core.maze_rats;

import org.junit.*
import org.stevesea.adventuresmith.core.*


class MazeRatsTest : BaseGeneratorTest() {
    @Before
    fun setup() {
        init()
    }

    @Test
    fun affliction() {
        Assert.assertEquals("Ages backwards", MrAfflictionsGenerator(lockedShuffler!!).generate())
    }
    @Test
    fun potion_effects() {
        Assert.assertEquals("1-hour vampirism", MrPotionEffectsGenerator(lockedShuffler!!).generate())
    }
    @Test
    fun magic() {
        Assert.assertEquals("Arc Acid", MrMagicGenerator(lockedShuffler!!).generate())
    }
    @Test
    fun item() {
        Assert.assertEquals("asdf", MrItemGenerator(lockedShuffler!!).generate())
    }
    @Test
    fun creature() {
        Assert.assertEquals("asdf", MrCreatureGenerator(lockedShuffler!!).generate())
    }
    @Test
    fun char() {
        Assert.assertEquals("asdf", MrCharacterGenerator(lockedShuffler!!).generate())
    }

}
