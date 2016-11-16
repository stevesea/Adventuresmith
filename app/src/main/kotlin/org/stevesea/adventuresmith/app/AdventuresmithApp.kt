/*
 * Copyright (c) 2016 Steve Christensen
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

package org.stevesea.adventuresmith.app

import android.content.*
import android.os.*
import android.support.multidex.*
import android.text.*
import com.crashlytics.android.*
import com.crashlytics.android.core.*
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.*
import com.squareup.leakcanary.*
import io.fabric.sdk.android.*
import org.jetbrains.anko.*
import org.stevesea.adventuresmith.BuildConfig
import org.stevesea.adventuresmith.core.*

class AdventuresmithApp : MultiDexApplication(), KodeinAware, AnkoLogger {

    override val kodein by Kodein.lazy {
        info("starting kodein bindings")

        import(adventureSmithModule)

        import(androidModule)

        val gennames = instance<Set<String>>(AdventureSmithConstants.GENERATORS)

        // map of generator id -> generator
        val genMap : MutableMap<String, Generator> = mutableMapOf()
        // set of generator-collections
        val colls : MutableSet<CollectionMetaDto> = mutableSetOf()

        val collMetaLoader = instance<CollectionMetaLoader>()

        val context : Context = instance() // TODO: no idea if this works
        val locale = context.resources.configuration.locales.get(0)

        info("instantiating generators")
        for (g in gennames) {
            val generator_instance = instance<Generator>(g)

            val genmeta = generator_instance.getMetadata(locale)

            colls.add(collMetaLoader.load(genmeta.collectionId, locale))

            genMap.put(g, generator_instance)
        }

        bind<Set<CollectionMetaDto>>(AdventureSmithConstants.GENERATORS) with instance(colls)
        bind<Map<String, Generator>>(AdventureSmithConstants.GENERATORS) with instance(genMap)

        info("done with bindings")
        // TODO: create drawer items here?
    }

    override fun onCreate() {
        super.onCreate()
        info("creating app")
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
    }
}



fun htmlStrToSpanned(input: String): Spanned {
    if (Build.VERSION.SDK_INT >= 24 /*Build.VERSION_CODES.N*/) {
        return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(input)
    }
}
