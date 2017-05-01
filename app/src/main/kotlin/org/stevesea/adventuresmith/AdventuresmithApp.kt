/*
 * Copyright (c) 2017 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith

import android.annotation.*
import android.os.*
import android.support.multidex.*
import android.text.*
import com.crashlytics.android.*
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.core.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.base.Stopwatch
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.Iconics
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.squareup.leakcanary.*
import io.fabric.sdk.android.*
import org.jetbrains.anko.*
import org.stevesea.adventuresmith.core.AdventuresmithCore
import java.util.concurrent.TimeUnit

class AdventuresmithApp : MultiDexApplication(), AnkoLogger {

    companion object {
        val watch = Stopwatch.createStarted()

        val objectMapper by lazy {
            ObjectMapper().registerKotlinModule()
        }
        val objectReader by lazy {
            objectMapper.reader()
        }
        val objectWriter by lazy {
            objectMapper.writer()
        }
    }

    override fun onCreate() {
        super.onCreate()
        info("App Started: ${watch}")

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this)

        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        Fabric.with(this, crashlyticsKit)

        Iconics.init(applicationContext)
        Iconics.registerFont(CommunityMaterial())
        Iconics.registerFont(Ionicons())

        info("App onCreate done: ${watch}")

        /*
        doAsync {
            val innerStopwatch = Stopwatch.createStarted()
            AdventuresmithCore.initCaches()
            innerStopwatch.stop()
            info("Init-caches done: $innerStopwatch (since app start: ${AdventuresmithApp.watch})")
            Answers.getInstance().logCustom(CustomEvent("InitCaches")
                    .putCustomAttribute("elapsedMS", innerStopwatch.elapsed(TimeUnit.MILLISECONDS))
                    .putCustomAttribute("fromAppStartMS", AdventuresmithApp.watch.elapsed(TimeUnit.MILLISECONDS))
            )
        }
        */
    }
}


@TargetApi(Build.VERSION_CODES.N)
fun htmlStrToSpanned(input: String): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(input)
    }
}
