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

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.R

@CompileStatic
public class Adventuresmith extends AppCompatActivity {

    // TODO: enable result filtering

    private AccountHeader headerResult = null;
    private Drawer drawer = null;

    @InjectView(R.id.toolbar)
    Toolbar toolbar

    @InjectView(R.id.recycler_buttons)
    RecyclerView recyclerButtons
    @InjectView(R.id.recycler_results)
    RecyclerView recyclerResults


    private FastItemAdapter<ButtonAdapterItem> buttonAdapter;
    private FastItemAdapter<ResultAdapterItem> resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventuresmith);

        SwissKnife.inject(this);

        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        //create our FastAdapter which will manage everything
        buttonAdapter = new FastItemAdapter<>();
        buttonAdapter.withSelectable(false);
        buttonAdapter.withPositionBasedStateManagement(true);

        buttonAdapter.withOnClickListener(new FastAdapter.OnClickListener<ButtonAdapterItem>() {
            @Override
            public boolean onClick(View v, IAdapter<ButtonAdapterItem> adapter, ButtonAdapterItem item, int position) {
                resultAdapter.add(0, new ResultAdapterItem().withResult(item.buttonData.generate()))
                recyclerResults.scrollToPosition(0)

                String drawerItem = getString(DrawerItemData.getDrawerItemData(item.buttonData.drawerId).nameResourceId)
                String btnText = getString(item.buttonData.id)
                Answers.getInstance().logCustom(new CustomEvent("Generated Result")
                        .putCustomAttribute("Dataset", drawerItem)
                        .putCustomAttribute("Button", drawerItem + '.' + btnText))
                return true;
            }
        });

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
        recyclerButtons.itemAnimator = new DefaultItemAnimator();
        recyclerButtons.adapter = buttonAdapter;

        resultAdapter = new FastItemAdapter<>()
        resultAdapter.withSelectable(false)
        resultAdapter.withPositionBasedStateManagement(true)
        resultAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener<ResultAdapterItem>() {
            @Override
            boolean onLongClick(View v, IAdapter<ResultAdapterItem> adapter, ResultAdapterItem item, int position) {

                // TODO: http://stackoverflow.com/questions/24737622/how-add-copy-to-clipboard-to-custom-intentchooser
                // TODO: https://gist.github.com/mediavrog/5625602
                /*
                ClipboardManager clipboard = v.getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                ClipData clipData = ClipData.newHtmlText(v.getContext().getString(R.string.app_name), plainTxt, htmlTxt)
                clipboard.setPrimaryClip(clipData)
                */
                //Snackbar.make(v, "Shared...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                // TODO: this isn't workin'

                Intent sIntent = new Intent()
                sIntent.setAction(Intent.ACTION_SEND)
                sIntent.setType("text/html")
                //sIntent.putExtra(Intent.EXTRA_SUBJECT, "Look at what I generated with ${v.getContext().getString(R.string.app_name)}")
                sIntent.putExtra(Intent.EXTRA_TEXT, item.spannedText.toString())
                sIntent.putExtra(Intent.EXTRA_HTML_TEXT, item.htmlTxt)
                //sIntent.setClipData(clipData)
                v.getContext().startActivity(Intent.createChooser(sIntent,
                        v.getContext().getString(R.string.action_share)))
            }
        })

        GridLayoutManager resultsGridLayoutMgr = new GridLayoutManager(this, getResources().getInteger(R.integer.resultCols))
        final resultSpanLong = getResources().getInteger(R.integer.resultColsLongtext)
        resultsGridLayoutMgr.spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                ResultAdapterItem item = resultAdapter.getAdapterItem(position)
                if (item.htmlTxt.length() > 48)
                    return resultSpanLong
                else
                    return 1
            }
        } as GridLayoutManager.SpanSizeLookup
        recyclerResults.layoutManager = resultsGridLayoutMgr
        recyclerResults.itemAnimator = new DefaultItemAnimator()
        recyclerResults.adapter = resultAdapter

        // TODO: restore not working -- no items restored
        //restore selections (this has to be done after the items were added
        buttonAdapter.withSavedInstanceState(savedInstanceState);
        resultAdapter.withSavedInstanceState(savedInstanceState);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withTranslucentStatusBar(false)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.pheonix)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withSavedInstance(savedInstanceState)
                //.withActionBarDrawerToggleAnimated(true)
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
                            // TODO: handle attribution drawer item

                            //resultsAdd(0, getString(R.string.content_attribution))
                            //resultsAdd(1, getString(R.string.content_artwork))
                            //resultsAdd(2, getString(R.string.content_thanks))
                            DrawerItemData diData = DrawerItemData.getDrawerItemData(drawerItem.getIdentifier() as int)

                            if (diData.selectable) {
                                resultAdapter.clear()
                                buttonAdapter.clear()
                                for (ButtonData bd : ButtonData.getButtonsForDrawerItem(diData.id)) {
                                    buttonAdapter.add(new ButtonAdapterItem().withButton(bd))
                                }

                                Answers.getInstance().logCustom(new CustomEvent("Selected Dataset")
                                        .putCustomAttribute("Dataset", getString(diData.nameResourceId))
                                )
                            }
                        }
                        return false
                    }
                } as Drawer.OnDrawerItemClickListener)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            resultAdapter.clear()
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        outState = buttonAdapter.saveInstanceState(outState)
        outState = resultAdapter.saveInstanceState(outState);
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
