package org.steavesea.rpg_boy2000

import android.content.Context
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(library = true)
public class AndroidModule {
    private final RpgBoy2000App application;

    public AndroidModule(RpgBoy2000App application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication Context provideApplicationContext() {
        return application;
    }
}
