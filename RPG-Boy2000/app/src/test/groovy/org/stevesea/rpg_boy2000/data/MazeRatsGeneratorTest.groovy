package org.stevesea.rpg_boy2000.data

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
