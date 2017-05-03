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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.materialize.MaterializeBuilder
import kotlinx.android.synthetic.main.activity_attribution.recycler_attribution
import kotlinx.android.synthetic.main.activity_attribution.toolbar

class AttributionActivity : AppCompatActivity() {

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

        itemAdapter.add(ResultItem(getString(R.string.content_thanks)))
        itemAdapter.add(ResultItem(getString(R.string.content_attribution)))
        itemAdapter.add(ResultItem(getString(R.string.artwork_advsmith)))
        itemAdapter.add(ResultItem(getString(R.string.artwork_icons)))
        itemAdapter.add(ResultItem(getString(R.string.artwork_public_domain)))
    }
}
