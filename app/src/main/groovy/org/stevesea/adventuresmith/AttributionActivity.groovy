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
 *
 */

package org.stevesea.adventuresmith

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.mikepenz.materialize.MaterializeBuilder
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.R

@CompileStatic
public class AttributionActivity extends AppCompatActivity {

    static Spanned htmlStrToSpanned(String input) {
        if (Build.VERSION.SDK_INT >= 24 /*Build.VERSION_CODES.N*/) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribution);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.app_name)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)

        //style our ui
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(false)
                .withStatusBarPadding(true)
                .build();

        TextView textViewContent = (TextView) findViewById(R.id.attribution_txt_content)
        textViewContent.setText(htmlStrToSpanned(getString(R.string.content_attribution)))

        TextView textViewArt = (TextView) findViewById(R.id.attribution_txt_artwork)
        textViewArt.setText(htmlStrToSpanned(getString(R.string.content_artwork)))

        TextView textViewThanks = (TextView) findViewById(R.id.attribution_txt_thanks)
        textViewThanks.setText(htmlStrToSpanned(getString(R.string.content_thanks)))
    }
}
