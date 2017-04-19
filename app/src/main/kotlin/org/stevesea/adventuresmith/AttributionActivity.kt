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

import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.*
import com.mikepenz.fastadapter.commons.adapters.*
import com.mikepenz.materialize.*
import kotlinx.android.synthetic.main.activity_attribution.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class AttributionActivity : AppCompatActivity() {

    fun getAttributions(adapter: FastItemAdapter<ResultItem>, locale: Locale) {
        val colls = AdventuresmithCore.getCollections(locale)

        for (coll in colls) {
            if (coll.credit == null)
                continue
            val str = coll.toHtmlStr()
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

        val itemAdapter : FastItemAdapter<ResultItem> = FastItemAdapter<ResultItem>()
                .withSelectable(false)
                .withMultiSelect(false)
                .withPositionBasedStateManagement(false)
                as FastItemAdapter<ResultItem>


        val resultsGridLayoutMgr = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.resultCols),
                StaggeredGridLayoutManager.VERTICAL)

        recycler_attribution.layoutManager = resultsGridLayoutMgr
        recycler_attribution.itemAnimator = DefaultItemAnimator()
        recycler_attribution.adapter = itemAdapter

        itemAdapter.add(ResultItem(getString(R.string.artwork_advsmith)))
        itemAdapter.add(ResultItem(getString(R.string.content_thanks)))
        getAttributions(itemAdapter, AdventuresmithActivity.getCurrentLocale(resources))
        itemAdapter.add(ResultItem(getString(R.string.artwork_icons)))
        itemAdapter.add(ResultItem(getString(R.string.artwork_public_domain)))

    }
}
