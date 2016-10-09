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
package org.stevesea.rpgpad

import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import groovy.transform.CompileStatic

@CompileStatic
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MAX_RESULTS = 50
    private static final String RV_BUTTONS_MGR = MainActivity.class.name + '.rv_buttons_mgr'
    private static final String RV_RESULTS_MGR = MainActivity.class.name + '.rv_results_mgr'
    private static final String RV_RESULTS_ITEMS = MainActivity.class.name + '.rv_results_items'

    private static final String DATASET_CURRENT = MainActivity.class.name + '.dataset_current'
    private static final String BUTTON_CURRENT = MainActivity.class.name + '.button_current'

    RecyclerView recyclerButtons
    RecyclerView recyclerResults

    ArrayList<String> results;
    ArrayList<DatasetButton> buttons;

    Dataset datasetCurrent
    DatasetButton buttonCurrent

    Toolbar toolbar

    DrawerLayout drawer;

    NavigationView navigationView

    ResultsAdapter resultsAdapter
    ButtonsAdapter buttonsAdapter

    FloatingActionButton fab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = R.layout.activity_main

        navigationView = findViewById(R.id.nav_view) as NavigationView
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar

        recyclerResults = findViewById(R.id.recycler_results) as RecyclerView
        recyclerButtons = findViewById(R.id.recycler_buttons) as RecyclerView


        fab = findViewById(R.id.fab_again) as FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                if (buttonCurrent != null) {
                    generateButtonPressed(buttonCurrent)
                }
            }
        })

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        results = new ArrayList<>()
        buttons = new ArrayList<>()

        buttonsAdapter = new ButtonsAdapter(this, buttons)
        recyclerButtons.adapter = buttonsAdapter
        GridLayoutManager buttonGridLayoutMgr = new GridLayoutManager(this, getResources().getInteger(R.integer.buttonCols))

        final int btnSpanShort = getResources().getInteger(R.integer.buttonSpanShort)
        final int btnSpanRegular = getResources().getInteger(R.integer.buttonSpanRegular)
        buttonGridLayoutMgr.spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String btnTxt = getString(buttons.getAt(position).getStringResourceId())
                if (btnTxt.length() <= 6) {
                    return btnSpanShort
                }
                else {
                    return btnSpanRegular
                }
            }
        } as GridLayoutManager.SpanSizeLookup

        recyclerButtons.layoutManager = buttonGridLayoutMgr

        resultsAdapter = new ResultsAdapter(results);
        recyclerResults.adapter = resultsAdapter
        GridLayoutManager resultsGridLayoutMgr = new GridLayoutManager(this, getResources().getInteger(R.integer.resultCols))

        final resultSpanLong = getResources().getInteger(R.integer.resultColsLongtext)
        resultsGridLayoutMgr.spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (results.getAt(position).length() > 48)
                    return resultSpanLong
                else
                    return 1
            }
        } as GridLayoutManager.SpanSizeLookup
        resultsGridLayoutMgr.onSaveInstanceState()

        recyclerResults.layoutManager = resultsGridLayoutMgr


        if (savedInstanceState == null) {
            // first time
            //useDataset(Dataset.None)
            resultsAdd(0, getString(R.string.welcome_msg))
        }

    }

    @Override
    protected void onResume() {
        super.onResume()
        checkFabVisibility()
    }

    void checkFabVisibility() {
        if (fab != null) {
            fab.setVisibility(buttonCurrent == null ? View.INVISIBLE : View.VISIBLE)
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState)


        datasetCurrent = savedInstanceState.getSerializable(DATASET_CURRENT) as Dataset
        useDataset(datasetCurrent != null ? datasetCurrent : Dataset.None)
        buttonCurrent = savedInstanceState.getSerializable(BUTTON_CURRENT) as DatasetButton

        List<String> restoredItems = savedInstanceState.getSerializable(RV_RESULTS_ITEMS) as ArrayList<String>
        resultsRestore(restoredItems)

        recyclerButtons.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RV_BUTTONS_MGR));
        recyclerResults.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RV_RESULTS_MGR));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RV_BUTTONS_MGR, recyclerButtons.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(RV_RESULTS_MGR, recyclerResults.getLayoutManager().onSaveInstanceState());
        outState.putSerializable(RV_RESULTS_ITEMS, results)
        outState.putSerializable(DATASET_CURRENT, datasetCurrent)
        outState.putSerializable(BUTTON_CURRENT, buttonCurrent)

        super.onSaveInstanceState(outState)
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

        if (id == R.id.action_clear) {
            resultsClear()
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static Spanned htmlStrToSpanned(String input) {
        if (Build.VERSION.SDK_INT >= 24 /*Build.VERSION_CODES.N*/) {
            Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(input);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Dataset key = Dataset.lookupDatasetForNavItem(id)
        getSupportActionBar().title = getString(key.stringResourceId)

        useDataset(key)

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void resultsAdd(int position, String item) {
        results.add(position, item);

        int previousSize = results.size()
        int itemsToRemove = previousSize - MAX_RESULTS
        for (int ind = results.size() - 1; ind >= MAX_RESULTS ; ind--) {
            results.remove(ind)
            resultsAdapter.notifyItemRemoved(ind)
        }

        resultsAdapter.notifyItemInserted(position);
        recyclerResults.scrollToPosition(0)
    }

    // called on restore (so don't bother trying to be efficient with notifications)
    public resultsRestore(List<String> items) {
        results.clear()
        results.addAll(items)
        resultsAdapter.notifyDataSetChanged()
        recyclerResults.scrollToPosition(0)
    }

    void resultsClear() {
        results.clear()
        resultsAdapter.notifyDataSetChanged()
    }

    void generateButtonPressed(DatasetButton btn) {
        buttonCurrent = btn
        checkFabVisibility()
        resultsAdd(0, btn.generate())
    }

    public void useDataset(Dataset key) {
        datasetCurrent = key

        buttonCurrent = null
        checkFabVisibility()

        buttons.clear()
        buttons.addAll(DatasetButton.getButtonsForDataset(key))

        results.clear()
        if (key == Dataset.None) {
            resultsAdd(0, getString(R.string.content_attribution))
            resultsAdd(1, getString(R.string.content_thanks))
        } else {
            Answers.getInstance().logCustom(new CustomEvent("Selected Dataset")
                    .putCustomAttribute("Dataset", key.name())
            )
        }
        resultsAdapter.notifyDataSetChanged()
        buttonsAdapter.notifyDataSetChanged()
    }
}
