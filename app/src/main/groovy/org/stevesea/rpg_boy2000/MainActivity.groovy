/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.stevesea.rpg_boy2000

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
import org.stevesea.rpg_boy2000.data.Dataset

import javax.inject.Inject

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

        ((RpgBoy2000App) getApplication()).inject(this);

        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject this

        // This must be called for saved state restoring
        //SwissKnife.restoreState(this, savedInstanceState);

        // This mus be called for automatic parsing of intent extras
        SwissKnife.loadExtras(this)

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (RpgBoy2000App.isFirstStartup.get()) {
            resultsAdapter.add(getString(R.string.welcome_msg))
            RpgBoy2000App.isFirstStartup.set(false)
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_attribution) {
            resultsAdapter.clear()

            resultsAdapter.add(getString(R.string.content_permission))

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
