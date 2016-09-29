package org.stevesea.rpgpad.data

import dagger.Module
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.freebooters_on_the_frontier.FotFDataModule
import org.stevesea.rpgpad.data.maze_rats.MazeRatsDataModule
import org.stevesea.rpgpad.data.perilous_wilds.PwDataModule

@CompileStatic
@Module(
        includes = [FotFDataModule.class, MazeRatsDataModule.class, PwDataModule.class],
        library = true,
        complete = false
)
class RpgPadDataModule {
}
