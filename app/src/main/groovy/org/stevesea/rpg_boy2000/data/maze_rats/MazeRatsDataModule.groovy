package org.stevesea.rpg_boy2000.data.maze_rats

import dagger.Module
import groovy.transform.CompileStatic

@CompileStatic
@Module(
        injects = [
                MazeRatsAfflictions.class,
                MazeRatsCharacter.class,
                MazeRatsItems.class,
                MazeRatsMagic.class,
                MazeRatsMonsters.class,
                MazeRatsPotionEffects.class,
        ],
        library = true,
        complete = false
)
class MazeRatsDataModule {
}
