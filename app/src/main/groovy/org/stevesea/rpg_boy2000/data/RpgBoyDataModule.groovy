package org.stevesea.rpg_boy2000.data

import dagger.Module
import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.freebooters_on_the_frontier.FotFDataModule
import org.stevesea.rpg_boy2000.data.maze_rats.MazeRatsDataModule
import org.stevesea.rpg_boy2000.data.perilous_wilds.PwDataModule

@CompileStatic
@Module(
        includes = [FotFDataModule.class, MazeRatsDataModule.class, PwDataModule.class],
        library = true,
        complete = false
)
class RpgBoyDataModule {
}
