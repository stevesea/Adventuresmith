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

package org.stevesea.adventuresmith

import android.content.pm.PackageInfo
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.mikepenz.materialize.MaterializeBuilder
import groovy.transform.CompileStatic;

@CompileStatic
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.nav_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //style our ui
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(false)
                .withStatusBarPadding(true)
                .build();

        TextView textViewContent = (TextView) findViewById(R.id.about_txt_version);
        textViewContent.setText(ResultAdapterItem.htmlStrToSpanned(
                String.format(getString(R.string.about_version), versionName, versionCode)));

        TextView textViewArt = (TextView) findViewById(R.id.about_txt_app);
        textViewArt.setText(ResultAdapterItem.htmlStrToSpanned(getString(R.string.about_app)));
    }
}
