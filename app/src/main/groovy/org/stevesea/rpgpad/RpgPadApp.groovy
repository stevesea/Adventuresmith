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
import android.support.v4.view.LayoutInflaterCompat
import com.arasthel.swissknife.annotations.OnBackground
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.mikepenz.iconics.context.IconicsLayoutInflater
import com.squareup.leakcanary.LeakCanary
import groovy.transform.CompileStatic
import io.fabric.sdk.android.Fabric

@CompileStatic
public class RpgPadApp extends Application {
    DatasetButton[] btns;
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //Stetho.initializeWithDefaults(this);
        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()/*.disabled(BuildConfig.DEBUG)*/.build())
                .build();
        Fabric.with(this, crashlyticsKit)

        initButtons()
    }

    @OnBackground
    public void initButtons() {
        // initializing all the groovy closure-heavy classes seems to take a long time. do that here
        // in a background thread spawned during app startup. Otherwise, the first time the user
        // picks an item in the nav drawer, there's seconds of unresponsiveness.
        btns = DatasetButton.values()
    }
}
