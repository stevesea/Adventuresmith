/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stevesea.rpg_boy2000.data.maze_rats

import org.junit.Test
import org.steavesea.rpg_boy2000.data.Shuffler
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsAfflictions
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsCharacter
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsItems
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsMagic
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsMonsters
import org.steavesea.rpg_boy2000.data.maze_rats.MazeRatsPotionEffects

class MazeRatsGeneratorTest {

    Shuffler shuffler = new Shuffler(new Random());

    MazeRatsCharacter mrChar = new MazeRatsCharacter(shuffler);
    MazeRatsMagic mrMagic = new MazeRatsMagic(shuffler)
    MazeRatsItems mrItems = new MazeRatsItems(shuffler)
    MazeRatsMonsters mrMonsters = new MazeRatsMonsters(shuffler)
    MazeRatsAfflictions mrAfflictions = new MazeRatsAfflictions(shuffler)
    MazeRatsPotionEffects mrPotionEffects = new MazeRatsPotionEffects(shuffler)

    @Test
    public void test() {
        printf mrMagic.generate(4).toString() + "\n"
        printf mrItems.generate(4).toString() + "\n"
        printf mrMonsters.generate(4).toString() + "\n"
        printf mrAfflictions.generate(4).toString() + "\n"
        printf mrPotionEffects.generate(4).toString() + "\n"
        printf mrChar.generate(5).join("\n")
    }
}
