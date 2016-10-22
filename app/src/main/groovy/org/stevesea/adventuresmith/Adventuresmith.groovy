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

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.R

@CompileStatic
public class Adventuresmith extends AppCompatActivity {
    private AccountHeader headerResult = null;
    private Drawer drawer = null;

    @InjectView(R.id.toolbar)
    Toolbar toolbar

    @InjectView(R.id.recycler_buttons)
    RecyclerView recyclerButtons
    @InjectView(R.id.recycler_results)
    RecyclerView recyclerResults


    private FastItemAdapter<ButtonAdapterItem> buttonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventuresmith);

        SwissKnife.inject(this);

        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("wooo!");

        //create our FastAdapter which will manage everything
        buttonAdapter = new FastItemAdapter<>();
        buttonAdapter.withSelectable(false);
        buttonAdapter.withPositionBasedStateManagement(false);

        buttonAdapter.withOnClickListener(new FastAdapter.OnClickListener<ButtonAdapterItem>() {
            @Override
            public boolean onClick(View v, IAdapter<ButtonAdapterItem> adapter, ButtonAdapterItem item, int position) {
                // run generator
                //item.generator
                //Toast.makeText(v.getContext(), (item).btnText.getText(v.getContext()), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        recyclerButtons.setLayoutManager(new LinearLayoutManager(this));
        recyclerButtons.setItemAnimator(new DefaultItemAnimator());
        recyclerButtons.setAdapter(buttonAdapter);

        GridLayoutManager buttonGridLayoutMgr = new GridLayoutManager(this, getResources().getInteger(R.integer.buttonCols))

        final int btnSpanShort = getResources().getInteger(R.integer.buttonSpanShort)
        final int btnSpanRegular = getResources().getInteger(R.integer.buttonSpanRegular)
        final int btnSpanLong = getResources().getInteger(R.integer.buttonSpanLong)
        buttonGridLayoutMgr.spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                ButtonAdapterItem item = buttonAdapter.getAdapterItem(position)
                String btnTxt = getString(item.buttonData.id)
                List<Integer> words = btnTxt.tokenize().collect{it.length()}
                Integer longest = words.max()

                if (longest <= 6) {
                    return btnSpanShort
                } else if (longest >= 11) {
                    return btnSpanLong
                }
                else {
                    return btnSpanRegular
                }
            }
        } as GridLayoutManager.SpanSizeLookup

        recyclerButtons.layoutManager = buttonGridLayoutMgr

        // create items

        //restore selections (this has to be done after the items were added
        buttonAdapter.withSavedInstanceState(savedInstanceState);


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
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
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem) {
                            DrawerItemData diData = DrawerItemData.getDrawerItemData(drawerItem.getIdentifier() as int)
                            buttonAdapter.clear()
                            for (ButtonData bd : ButtonData.getButtonsForDrawerItem(diData.id)) {
                                buttonAdapter.add(new ButtonAdapterItem().withButton(bd))
                            }
                        }
                        return false
                    }
                } as Drawer.OnDrawerItemClickListener)
                .build();

        fillFab()
    }

    private void fillFab() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_plus_circle).actionBar().color(Color.WHITE));
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
