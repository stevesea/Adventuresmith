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

import android.app.Application
import dagger.ObjectGraph
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

import java.util.concurrent.atomic.AtomicBoolean

@CompileStatic
public class RpgPadApp extends Application {
    private ObjectGraph graph;
    static AtomicBoolean isFirstStartup = new AtomicBoolean(true)

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(
                new RpgPadModule(this)
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public AbstractGenerator generatorFactory(DatasetButton btn) {
        return graph.get(btn.clz)
    }
}
