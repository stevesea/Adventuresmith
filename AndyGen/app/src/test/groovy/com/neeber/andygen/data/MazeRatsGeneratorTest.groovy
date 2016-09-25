package com.neeber.andygen.data

import com.neeber.andygen.data.maze_rats.*
import org.junit.Test

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
        printf mrChar.generate(2).toString() + "\n"
    }
}
