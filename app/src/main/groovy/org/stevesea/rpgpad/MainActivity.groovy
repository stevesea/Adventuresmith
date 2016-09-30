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
 */
package org.stevesea.rpgpad

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic

import javax.inject.Inject

// TODO: select result items (actions: favorite, share, copy to clipboard)
// TODO: long-click result item (action: copy to clipboard)
// TODO: swipe support?
@CompileStatic
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @InjectView(R.id.recycler_buttons)
    RecyclerView recyclerButtons
    @InjectView(R.id.recycler_results)
    RecyclerView recyclerResults

    @InjectView(R.id.toolbar)
    Toolbar toolbar
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;
    @InjectView(R.id.nav_view)
    NavigationView navigationView

    @Inject
    ResultsAdapter resultsAdapter
    @Inject
    ButtonsAdapter buttonsAdapter

    @OnClick(R.id.clear_results)
    public void onClickFloater(View v) {
        resultsAdapter.clear()
        Snackbar.make(v, "Cleared results", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = R.layout.activity_main

        ((RpgPadApp) getApplication()).inject(this);

        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject this

        // This must be called for saved state restoring
        //SwissKnife.restoreState(this, savedInstanceState);

        // This mus be called for automatic parsing of intent extras
        SwissKnife.loadExtras(this)

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (RpgPadApp.isFirstStartup.get()) {
            resultsAdapter.addAll( [
                    getString(R.string.welcome_msg) + getString(R.string.content_attribution),
                    getString(R.string.content_thanks)
                    ]
                    )
            RpgPadApp.isFirstStartup.set(false)
        }


        navigationView.setNavigationItemSelectedListener(this);

        recyclerButtons.adapter = buttonsAdapter
        recyclerResults.adapter = resultsAdapter

        recyclerButtons.layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.buttonCols))

        recyclerResults.layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.resultCols))
        final int longTextSpan = getResources().getInteger(R.integer.resultColsLongtext)
        ((GridLayoutManager)recyclerResults.layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (resultsAdapter.getTextLength(position) > 48)
                    return longTextSpan
                else
                    return 1
            }
        })
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // TODO: implement settings
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_attribution) {
            resultsAdapter.clear()

            resultsAdapter.addAll([getString(R.string.content_attribution), getString(R.string.content_thanks)])

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Dataset key = Dataset.MazeRats
        if (id == R.id.nav_fotf) {
            key = Dataset.FreebootersOnTheFrontier
        } else if (id == R.id.nav_mr) {
            key = Dataset.MazeRats
        } else if (id == R.id.nav_pw) {
            key = Dataset.ThePerilousWilds
        }
        getSupportActionBar().setTitle(getString(key.stringResourceId))
        buttonsAdapter.useDb(key)
        resultsAdapter.clear()

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
