/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.stevesea.rpg_boy2000

import android.content.Context
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.RpgBoyDataModule
import org.stevesea.rpg_boy2000.data.Shuffler

import javax.inject.Singleton

@CompileStatic
@Module(
        includes = RpgBoyDataModule.class,
        injects = [MainActivity.class, ResultsAdapter.class, ButtonsAdapter.class, Shuffler.class],
        library = true,
        complete = false
)
public class RpgBoy2000Module {
    private final RpgBoy2000App application;

    public RpgBoy2000Module(RpgBoy2000App application) {
        this.application = application;
    }

    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link org.stevesea.rpg_boy2000.ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton RpgBoy2000App provideApp() {
        return application
    }
}
