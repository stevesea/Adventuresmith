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

import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.mikepenz.iconics.context.IconicsLayoutInflater
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.R

@CompileStatic
public class Adventuresmith extends AppCompatActivity {
    private AccountHeader headerResult = null;
    private Drawer drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventuresmith);

        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));

        // Handle Toolbar
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.pheonix)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                //.withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withDrawerItems(DrawerItemData.createDrawerItems())
                .build();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
