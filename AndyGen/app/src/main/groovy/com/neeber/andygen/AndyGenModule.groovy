package com.neeber.andygen

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(
        injects = [MainActivity.class, Shuffler.class],
        complete = false
)
public class AndyGenModule {
    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }
}