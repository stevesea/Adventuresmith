package org.steavesea.rpg_boy2000

import dagger.Module
import dagger.Provides
import org.steavesea.rpg_boy2000.data.Shuffler

import javax.inject.Singleton

@Module(
        injects = [MainActivity.class, Shuffler.class],
        complete = false
)
public class RpgBoy2000Module {
    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }
}
