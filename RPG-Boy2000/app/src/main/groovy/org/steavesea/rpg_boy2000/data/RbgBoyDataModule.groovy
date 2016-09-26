package org.steavesea.rpg_boy2000.data

import dagger.Module
import dagger.Provides
import org.steavesea.rpg_boy2000.ButtonsAdapter

import javax.inject.Singleton

@Module(
        injects = [Shuffler.class, ButtonsAdapter.class],
        complete = false,
        library = true
)
public class RbgBoyDataModule {
    static final String DEFAULT = MAZERATS
    static final String PERILOUS_WILDS = 'PerilousWilds'
    static final String FREEBOOTERS = 'Freebooters'
    static final String MAZERATS = 'MazeRats'

    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }

    // not the most extensible... but this is a map of
    @Provides @Singleton
    public Map<String, List<String>> provideDb() {
        def result = [:]
        result[PERILOUS_WILDS] = ["1", "2", "3"]
        result[FREEBOOTERS] = ["4", "5", "6", "7"]
        result[MAZERATS] = ["8", "9"]
        return result
    }

}
