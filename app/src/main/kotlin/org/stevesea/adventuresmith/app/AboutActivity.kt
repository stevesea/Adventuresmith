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

import android.content.pm.*
import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.*
import com.mikepenz.fastadapter.commons.adapters.*
import com.mikepenz.materialize.*
import kotlinx.android.synthetic.main.activity_about.*
import org.stevesea.adventuresmith.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        var versionName = ""
        var versionCode = -1
        try {
            val packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            // no-op
        }

        setSupportActionBar(toolbar)

        supportActionBar!!.setTitle(R.string.nav_about)
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

        recycler_about.layoutManager = resultsGridLayoutMgr
        recycler_about.itemAnimator = DefaultItemAnimator()
        recycler_about.adapter = itemAdapter

        itemAdapter.add(ResultItem(getString(R.string.about_help)))
        itemAdapter.add(ResultItem(String.format(getString(R.string.about_app), versionName, versionCode)))
        itemAdapter.add(ResultItem(getString(R.string.about_thirdparty)))
    }
}
