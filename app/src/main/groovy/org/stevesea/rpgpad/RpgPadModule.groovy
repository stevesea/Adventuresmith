/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.stevesea.rpgpad

import android.content.Context
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic

import javax.inject.Singleton

@CompileStatic
@Module
public class RpgPadModule {
    private final RpgPadApp application;

    public RpgPadModule(RpgPadApp application) {
        this.application = application;
    }

    @Provides @Singleton
    Random provideRandom() {
        return new Random();
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton RpgPadApp provideApp() {
        return application
    }
}
