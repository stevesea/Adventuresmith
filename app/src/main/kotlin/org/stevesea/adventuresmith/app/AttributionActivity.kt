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

import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.*
import com.mikepenz.fastadapter.*
import com.mikepenz.fastadapter.adapters.*
import com.mikepenz.materialize.*
import kotlinx.android.synthetic.main.activity_attribution.*
import org.stevesea.adventuresmith.R
import org.stevesea.adventuresmith.core.*
import java.util.*

class AttributionActivity : AppCompatActivity() {

    fun getAttributions(adapter: ItemAdapter<ResultItem>, locale: Locale) {
        val colls = AdventuresmithCore.getCollections(locale)

        for (coll in colls) {
            if (coll.credit == null)
                continue
            val str = html {
                body {
                    h1 { + "${getString(R.string.attribution_content)} - ${coll.name}"}
                    h4 { + coll.credit.orEmpty() }
                    if (coll.desc != null) {
                        p { +coll.desc!! }
                    }
                    if (coll.attribution != null) {
                        p { +coll.attribution!! }
                    }
                    if (coll.url != null) {
                        p { +coll.url!! }
                    }
                }
            }.toString()
            adapter.add(ResultItem(str))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_attribution)

        setSupportActionBar(toolbar)

        supportActionBar!!.setTitle(R.string.nav_thanks)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(false)
                .withStatusBarPadding(true)
                .build()

        val fastAdapter : FastAdapter<ResultItem> = FastAdapter<ResultItem>()
                .withSelectable(false)
                .withMultiSelect(false)
                .withPositionBasedStateManagement(false)
        val itemAdapter = ItemAdapter<ResultItem>()


        val resultsGridLayoutMgr = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.resultCols),
                StaggeredGridLayoutManager.VERTICAL)

        recycler_attribution.layoutManager = resultsGridLayoutMgr
        recycler_attribution.itemAnimator = DefaultItemAnimator()
        recycler_attribution.adapter = itemAdapter.wrap(fastAdapter)

        itemAdapter.add(ResultItem(getString(R.string.artwork_advsmith)))
        itemAdapter.add(ResultItem(getString(R.string.content_thanks)))
        getAttributions(itemAdapter, AdventuresmithActivity.getCurrentLocale(resources))
        itemAdapter.add(ResultItem(getString(R.string.artwork_icons)))
        itemAdapter.add(ResultItem(getString(R.string.artwork_public_domain)))

    }
}
