package org.steavesea.rpg_boy2000.data

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(
        injects = [Shuffler.class],
        complete = false
)
public class RbgBoyDataModule {
    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }
}
