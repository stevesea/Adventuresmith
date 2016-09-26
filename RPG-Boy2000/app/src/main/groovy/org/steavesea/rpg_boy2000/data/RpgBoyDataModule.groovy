package org.steavesea.rpg_boy2000.data

import dagger.Module
import dagger.Provides
import org.steavesea.rpg_boy2000.data.freebooters_on_the_frontier.FotFSpells
import org.steavesea.rpg_boy2000.data.freebooters_on_the_frontier.FotFTraits
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwPlace
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwRegion

import javax.inject.Singleton

@Module(
        injects = [Shuffler.class, RpgBoyData.class],
        complete = false,
        library = true
)
public class RpgBoyDataModule {

    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }

    // this seems dumb... but i don't know how else to do it right now
    @Provides
    List<AbstractGenerator> provideGenerators(PwPlace pwPlace, PwRegion pwRegion, FotFSpells fotFSpells, FotFTraits fotFTraits) {
        def result = []
        result.add(pwRegion)
        result.add(pwPlace)
        result.add(fotFTraits)
        result.add(fotFSpells)
        return result
    }
}
