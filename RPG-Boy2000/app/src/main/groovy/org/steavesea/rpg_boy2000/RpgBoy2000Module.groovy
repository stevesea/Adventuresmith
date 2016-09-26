package org.steavesea.rpg_boy2000

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(
        injects = [MainActivity.class],
        library = true,
        complete = false
)
public class RpgBoy2000Module {
    @Provides @Singleton
    public ResultsAdapter provideResultsAdapter() {
        new ResultsAdapter(new ArrayList<String>())
    }

    @Provides @Singleton
    public ButtonsAdapter provideButtonsAdapter() {
        return new ButtonsAdapter()
    }
}
