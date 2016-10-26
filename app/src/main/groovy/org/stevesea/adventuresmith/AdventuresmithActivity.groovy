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
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItemAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import groovy.transform.CompileStatic

import java.util.concurrent.atomic.AtomicLong

@CompileStatic
public class AdventuresmithActivity extends AppCompatActivity implements ItemAdapter.ItemFilterListener {
    private static final String BUNDLE_RESULT_ITEMS = AdventuresmithActivity.class.name + '.result_items'
    private static final String BUNDLE_BUTTON_ITEMS = AdventuresmithActivity.class.name + '.button_items'

    private static AtomicLong resultIdGenerator = new AtomicLong(0)

    private AccountHeader headerResult = null;
    private Drawer drawer = null;

    AppBarLayout appBarLayout
    Toolbar toolbar
    RecyclerView recyclerButtons
    RecyclerView recyclerResults

    private FastItemAdapter<ButtonAdapterItem> buttonAdapter;
    private FastItemAdapter<ResultAdapterItem> resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventuresmith);

        toolbar = findViewById(R.id.toolbar) as Toolbar
        appBarLayout = findViewById(R.id.appbar) as AppBarLayout
        recyclerResults = findViewById(R.id.recycler_results) as RecyclerView
        recyclerButtons = findViewById(R.id.recycler_buttons) as RecyclerView

        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        buttonAdapter = new FastItemAdapter<>();
        buttonAdapter.withSelectable(false);
        buttonAdapter.withPositionBasedStateManagement(true);

        buttonAdapter.withOnClickListener(new FastAdapter.OnClickListener<ButtonAdapterItem>() {
            @Override
            public boolean onClick(View v, IAdapter<ButtonAdapterItem> adapter, ButtonAdapterItem item, int position) {
                resultAdapter.add(0, new ResultAdapterItem()
                        .withResult(item.buttonData.generate())
                        .withButtonId(item.buttonData.id)
                        .withIdentifier(resultIdGenerator.incrementAndGet()))
                recyclerResults.scrollToPosition(0)


                def drawerItemData = DrawerItemData.getDrawerItemData(item.buttonData.drawerId)
                if (drawerItemData) {
                    String drawerItem = getString(drawerItemData.nameResourceId)
                    String btnText = getString(item.buttonData.id)
                    Answers.getInstance().logCustom(new CustomEvent("Generated Result")
                            .putCustomAttribute("Dataset", drawerItem)
                            .putCustomAttribute("Button", drawerItem + '.' + btnText))
                    return true;
                } else {
                    return false
                }
            }
        } as FastAdapter.OnClickListener<ButtonAdapterItem>);

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
        resultAdapter.withSelectable(true)
        resultAdapter.withMultiSelect(true);
        resultAdapter.withSelectOnLongClick(true);
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
        } as FastAdapter.OnLongClickListener<ResultAdapterItem>)

        //configure the itemAdapter
        resultAdapter.withFilterPredicate(new IItemAdapter.Predicate<ResultAdapterItem>() {
            @Override
            public boolean filter(ResultAdapterItem item, CharSequence constraint) {
                //return true if we should filter it out
                //return false to keep it
                return !item.spannedText.toString().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        } as IItemAdapter.Predicate<ResultAdapterItem>);

        resultAdapter.getItemAdapter().withItemFilterListener(this);

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

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.header_graphic)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withSavedInstance(savedInstanceState)
                //.withActionBarDrawerToggle(true)
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

                            DrawerItemData diData = DrawerItemData.getDrawerItemData(drawerItem.getIdentifier() as int)
                            if (diData == null)
                                return false

                            if (diData.id.equals(DrawerItemId.Attribution)) {
                                Intent intent = new Intent(AdventuresmithActivity.this, AttributionActivity.class);
                                AdventuresmithActivity.this.startActivity(intent)

                                return false
                            } else if (diData.id.equals(DrawerItemId.About)) {
                                Intent intent = new Intent(AdventuresmithActivity.this, AboutActivity.class);
                                AdventuresmithActivity.this.startActivity(intent)

                                return false
                            }


                            if (diData.selectable) {
                                getSupportActionBar().setTitle(diData.nameResourceId)

                                // when drawer item clicked, expand the collapsing bar.
                                appBarLayout.setExpanded(true, true)

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

        //search icon
        menu.findItem(R.id.action_clear).setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_delete).color(Color.WHITE).actionBar());
        menu.findItem(R.id.search).setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_magnify).color(Color.WHITE).actionBar());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    resultAdapter.filter(s);
                    appBarLayout.setExpanded(false, false)
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    resultAdapter.filter(s);
                    appBarLayout.setExpanded(false, false)
                    return true;
                }
            } as SearchView.OnQueryTextListener);
            /*
            searchView.onSearchClickListener = new View.OnClickListener() {
                @Override
                void onClick(View v) {
                    appBarLayout.setExpanded(false, true)
                }
            }
            searchView.onCloseListener = new SearchView.OnCloseListener() {
                @Override
                boolean onClose() {
                    appBarLayout.setExpanded(true, true)
                    return false
                }
            }
            */
            /*
            menu.findItem(R.id.search).onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
                @Override
                boolean onMenuItemClick(MenuItem item) {
                    appBarLayout.setExpanded(false, false)
                    return false;
                }
            } as MenuItem.OnMenuItemClickListener
            */
        } else {
            menu.findItem(R.id.search).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
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

        ArrayList<ButtonAdapterItem> buttons = new ArrayList<>()
        buttons.addAll(buttonAdapter.getAdapterItems())
        outState.putSerializable(BUNDLE_BUTTON_ITEMS, buttons)

        ArrayList<ResultAdapterItem> results = new ArrayList<>()
        results.addAll(resultAdapter.getAdapterItems())

        outState.putSerializable(BUNDLE_RESULT_ITEMS, results)

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState)

        // TODO: instead of doing this, we could just save current item. also, need to refactor
        //  onclick handler to use method we could use to set the activity state. (rather than
        // setting the drawer current selection, which seems to be problematic on on-click for
        //  expando-items)
        List<ButtonAdapterItem> restoredButtons = savedInstanceState.getSerializable(BUNDLE_BUTTON_ITEMS) as ArrayList<ButtonAdapterItem>
        buttonAdapter.clear()
        buttonAdapter.add(restoredButtons)

        List<ResultAdapterItem> restoredResults = savedInstanceState.getSerializable(BUNDLE_RESULT_ITEMS) as ArrayList<ResultAdapterItem>
        resultAdapter.clear()
        resultAdapter.add(restoredResults)

        buttonAdapter.withSavedInstanceState(savedInstanceState)
        resultAdapter.withSavedInstanceState(savedInstanceState)
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



    @Override
    public void itemsFiltered() {
        //Toast.makeText(this, "filtered items count: " + resultAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
    }
}
