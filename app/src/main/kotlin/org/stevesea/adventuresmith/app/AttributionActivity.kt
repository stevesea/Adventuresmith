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
import com.mikepenz.materialize.*
import kotlinx.android.synthetic.main.activity_attribution.*
import org.stevesea.adventuresmith.R
import org.stevesea.adventuresmith.core.*
import java.util.*

class AttributionActivity : AppCompatActivity() {

    fun getAttributions(locale: Locale) : String {
        val colls = AdventuresmithCore.getCollections(locale)

        return html {
            body {
                h1 { + getString(R.string.attribution_header) }
                p {
                    + getString(R.string.attribution_desc)
                }
                for (coll in colls) {
                    if (coll.credit == null)
                        continue

                    h4 { + "${coll.credit.orEmpty()} - ${coll.name}" }
                    if (coll.desc != null) {
                        p { + coll.desc!! }
                    }
                    if (coll.attribution != null) {
                        p { + coll.attribution!! }
                    }
                    if (coll.url != null)  {
                        p { + coll.url!! }
                    }
                }
            }

        }.toString()
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

        // TODO: read these from YaML
        attribution_txt_content.text = htmlStrToSpanned(getAttributions(AdventuresmithActivity.getCurrentLocale(resources)))
        attribution_txt_artwork.text = htmlStrToSpanned(getString(R.string.content_artwork))
        attribution_txt_thanks.text = htmlStrToSpanned(getString(R.string.content_thanks))
    }
}
