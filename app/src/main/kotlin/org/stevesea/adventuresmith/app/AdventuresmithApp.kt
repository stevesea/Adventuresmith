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
import android.support.multidex.*
import com.crashlytics.android.*
import com.crashlytics.android.core.*
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.*
import com.squareup.leakcanary.*
import io.fabric.sdk.android.*
import org.stevesea.adventuresmith.BuildConfig
import org.stevesea.adventuresmith.core.*

object AdventuresmithAppConstants {
}
class AdventuresmithApp : MultiDexApplication(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(adventureSmithModule)

        import(androidModule)

        val gennames = instance<Set<String>>(AdventureSmithConstants.GENERATORS)

        // map of generator name -> generator
        val genMap : MutableMap<String, Generator> = mutableMapOf()
        // map of coll name -> coll meta data
        val collMap : MutableMap<String, CollectionMetaDto> = mutableMapOf()

        val collgrpToGen : MutableMap<String, MutableList<Generator>> = mutableMapOf()

        val collMetaLoader = instance<CollectionMetaLoader>()

        val context : Context = instance()

        for (g in gennames) {
            val generator_instance = instance<Generator>(g)

            val genmeta = generator_instance.getMetadata()

            collMap.put(genmeta.collectionId,
                    collMetaLoader.load(genmeta.collectionId,
                            context.resources.configuration.locales.get(0))
            )
            genMap.put(g, generator_instance)
        }
    }

    override fun onCreate() {
        super.onCreate()
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
