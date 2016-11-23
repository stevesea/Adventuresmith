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

import android.annotation.*
import android.content.*
import android.content.res.*
import android.graphics.*
import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.*
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.*
import com.crashlytics.android.answers.*
import com.mikepenz.community_material_typeface_library.*
import com.mikepenz.fastadapter.*
import com.mikepenz.fastadapter.adapters.*
import com.mikepenz.iconics.*
import com.mikepenz.iconics.typeface.*
import com.mikepenz.ionicons_typeface_library.*
import com.mikepenz.materialdrawer.*
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import kotlinx.android.synthetic.main.activity_adventuresmith.*
import org.jetbrains.anko.*
import org.stevesea.adventuresmith.R
import org.stevesea.adventuresmith.core.*
import org.stevesea.adventuresmith.core.freebooters_on_the_frontier.*
import org.stevesea.adventuresmith.core.stars_without_number.*
import java.util.*

data class CollectionAndGroup(val collectionId: String,
                              val name: String,
                              val groupId: String? = null)

class AdventuresmithActivity : AppCompatActivity(),
        AnkoLogger {

    private var currentDrawerItemId: Long? = null
    val drawerIdToGroup : MutableMap<Long, CollectionAndGroup> = mutableMapOf()
    val ID_ABOUT = com.google.common.base.Objects.hashCode("about").toLong()
    val ID_THANKS = com.google.common.base.Objects.hashCode("thanks").toLong()

    private var drawerHeader: AccountHeader? = null
    private var drawer: Drawer? = null
    var resultAdapter : FastItemAdapter<ResultItem>? = null
    var buttonAdapter : FastItemAdapter<GeneratorButton>? = null


    fun getNavDrawerItems(locale: Locale) : List<IDrawerItem<*,*>> {
        info("Creating navDrawerItems")
        drawerIdToGroup.clear()

        val generatorCollections = AdventuresmithCore.getCollections(locale)

        val result: MutableList<IDrawerItem<*, *>> = mutableListOf()

        //info("Generators: $generatorCollections")

        var previousWasExpandable = false
        for (coll in generatorCollections) {
            info("collection: ${coll}")

            if (coll.groups != null && coll.groups!!.isNotEmpty()) {
                // has groups, create header & children
                val expandableItem = ExpandableDrawerItem()
                        .withName(coll.name)
                        .withIcon(getCollectionIcon(coll.id))
                        .withIdentifier(com.google.common.base.Objects.hashCode(coll.id).toLong())
                        .withSelectable(false)
                        .withIsExpanded(false)
                for (grp in coll.groups!!.entries) {
                    val navId = com.google.common.base.Objects.hashCode(coll.id, grp.key).toLong()
                    drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = coll.id, name = "${coll.name} / ${grp.value}" , groupId = grp.key))
                    val childItem = SecondaryDrawerItem()
                            .withName(grp.value)
                            .withIcon(getCollectionIcon(coll.id, grp.key))
                            .withIdentifier(navId)
                            .withSelectable(true)
                            .withLevel(2)
                    expandableItem.withSubItems(childItem)
                }
                result.add(expandableItem)
                previousWasExpandable = true
            } else {
                if (previousWasExpandable) {
                    //result.add(DividerDrawerItem())
                }
                // no groups, just create item
                val navId = com.google.common.base.Objects.hashCode(coll.id).toLong()
                drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = coll.id, name = coll.name))
                result.add(PrimaryDrawerItem()
                        .withName(coll.name)
                        .withIcon(getCollectionIcon(coll.id))
                        .withIdentifier(navId)
                        .withSelectable(true)
                        .withDescription(coll.desc)
                )
                previousWasExpandable = false
            }
        }
        result.add(DividerDrawerItem())
        // final items are for attribution & about
        result.add(SecondaryDrawerItem()
                .withName(R.string.nav_thanks)
                .withLevel(1)
                .withIdentifier(ID_THANKS)
                .withIcon(CommunityMaterial.Icon.cmd_information_outline)
        )
        result.add(SecondaryDrawerItem()
                .withName(R.string.nav_about)
                .withLevel(1)
                .withIdentifier(ID_ABOUT)
                .withIcon(CommunityMaterial.Icon.cmd_help)
        )

        return result
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adventuresmith)
        info("onCreate")

        setSupportActionBar(toolbar)

        info("width: ${resources.configuration.screenWidthDp}")
        info("height: ${resources.configuration.screenHeightDp}")

        collapsing_toolbar.title = ""

        resultAdapter = FastItemAdapter<ResultItem>()
                .withSelectable(true)
                .withMultiSelect(true)
                .withSelectOnLongClick(true)
                .withPositionBasedStateManagement(true)
                .withOnLongClickListener( object : FastAdapter.OnLongClickListener<ResultItem> {
                    override fun onLongClick(v: View?, adapter: IAdapter<ResultItem>?, item: ResultItem?, position: Int): Boolean {
                        if (v == null || item == null)
                            return false

                        // TODO: http://stackoverflow.com/questions/24737622/how-add-copy-to-clipboard-to-custom-intentchooser
                        // TODO: https://gist.github.com/mediavrog/5625602
                        /*
                        ClipboardManager clipboard = v.getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                        ClipData clipData = ClipData.newHtmlText(v.getContext().getString(R.string.app_name), plainTxt, htmlTxt)
                        clipboard.setPrimaryClip(clipData)
                        */
                        //Snackbar.make(v, "Shared...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                        val intent = Intent()
                        intent.setAction(Intent.ACTION_SEND)
                        intent.setType("text/html")
                        intent.putExtra(Intent.EXTRA_TEXT, item.spannedText.toString())
                        intent.putExtra(Intent.EXTRA_HTML_TEXT, item.htmlTxt)

                        v.context.startActivity(Intent.createChooser(intent,
                                v.context.getString(R.string.action_share)))

                        return true
                    }
                }) as FastItemAdapter<ResultItem>

        resultAdapter!!.withFilterPredicate(object : IItemAdapter.Predicate<ResultItem> {
            override fun filter(item: ResultItem?, constraint: CharSequence?): Boolean {
                if (item == null || constraint == null)
                    return false

                //return true if we should filter it out
                //return false to keep it
                return !item.spannedText.toString().toLowerCase().contains(constraint.toString().toLowerCase())
            }
        })
        buttonAdapter = FastItemAdapter<GeneratorButton>()
                .withSelectable(false)
                .withPositionBasedStateManagement(true)
                .withOnClickListener(object : FastAdapter.OnClickListener<GeneratorButton> {
                    override fun onClick(v: View?, adapter: IAdapter<GeneratorButton>?, item: GeneratorButton?, position: Int): Boolean {
                        if (item == null)
                            return false
                        info("Pressed button: ${item.name} (${item.meta.collectionId} ${Objects.toString(item.meta.groupId, "")})")

                        val result = item.generator.generate(getCurrentLocale(resources))

                        resultAdapter!!.add(0, ResultItem(result))

                        recycler_results.scrollToPosition(0)

                        Answers.getInstance().logCustom(
                                CustomEvent("Generated Result")
                                        .putCustomAttribute("CollectionId", item.meta.collectionId)
                                        .putCustomAttribute("GroupId", Objects.toString(item.meta.groupId, ""))
                                        .putCustomAttribute("Name", item.meta.name)
                        )
                        return true
                    }
                })
                as FastItemAdapter<GeneratorButton>

        drawerHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.header_graphic)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.FIT_XY)
                .build()

        val drawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withToolbar(toolbar)
                .withAccountHeader(drawerHeader!!)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withDrawerItems(getNavDrawerItems(getCurrentLocale(resources)))
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        if (drawerItem == null)
                            return false

                        val drawerItemId = drawerItem.identifier
                        if (drawerIdToGroup.containsKey(drawerItemId)) {
                            selectDrawerItem(drawerItemId)
                            return false
                        } else if (drawerItemId == ID_ABOUT) {
                            this@AdventuresmithActivity.startActivity(
                                    Intent(this@AdventuresmithActivity, AboutActivity::class.java))
                        } else if (drawerItemId == ID_THANKS) {
                            this@AdventuresmithActivity.startActivity(
                                    Intent(this@AdventuresmithActivity, AttributionActivity::class.java))
                        }
                        currentDrawerItemId = null
                        return false
                    }
                })

        if (resources.getDimension(R.dimen.navigation_menu_width) > 0) {
            drawer = drawerBuilder.buildView()
            nav_tablet.addView(drawer!!.slider)
        } else {
            drawer = drawerBuilder.build()
        }

        val btnSpanShort = resources.getInteger(R.integer.buttonSpanShort)
        val btnSpanRegular = resources.getInteger(R.integer.buttonSpanRegular)
        val btnSpanLong = resources.getInteger(R.integer.buttonSpanLong)
        val buttonGridLayoutMgr = GridLayoutManager(this, resources.getInteger(R.integer.buttonCols))
        buttonGridLayoutMgr.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = buttonAdapter!!.getAdapterItem(position)
                if (item == null) {
                    return btnSpanRegular
                }
                val maxWordLength = item.name.split(" ").map { it.length }.max()
                if (maxWordLength == null) {
                    return btnSpanRegular
                } else if (maxWordLength < 6) {
                    return btnSpanShort
                } else if (maxWordLength >= 10) {
                    return btnSpanLong
                } else {
                    return btnSpanRegular
                }
            }
        }

        recycler_buttons.layoutManager = buttonGridLayoutMgr
        recycler_buttons.itemAnimator = DefaultItemAnimator()
        recycler_buttons.adapter = buttonAdapter



        //val resultsGridLayoutMgr = GridLayoutManager(this, resources.getInteger(R.integer.resultCols))
        val resultsGridLayoutMgr = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.resultCols),
                StaggeredGridLayoutManager.VERTICAL)

        recycler_results.layoutManager = resultsGridLayoutMgr
        recycler_results.itemAnimator = DefaultItemAnimator()
        recycler_results.adapter = resultAdapter
    }

    private fun selectDrawerItem(drawerItemId: Long?) {
        if (drawerItemId == null)
            return
        val collGrp = drawerIdToGroup.get(drawerItemId)
        if (collGrp == null)
            return

        info("selected: ${collGrp.name}")

        toolbar.title = collGrp.name

        currentDrawerItemId = drawerItemId

        buttonAdapter!!.clear()
        val generators = AdventuresmithCore.getGeneratorsByGroup(getCurrentLocale(resources), collGrp.collectionId, collGrp.groupId)
        for (g in generators) {
            buttonAdapter!!.add(GeneratorButton(g.value, getCurrentLocale(resources), g.key))
        }
        resultAdapter!!.clear()
        resultAdapter!!.notifyAdapterDataSetChanged()
        appbar.setExpanded(true, true)

        Answers.getInstance().logCustom(CustomEvent("Selected Dataset")
                .putCustomAttribute("Dataset", collGrp.collectionId)
        )
    }

    private val BUNDLE_CURRENT_DRAWER_ITEM = AdventuresmithActivity::class.java.name + ".currentDrawerItem"
    private val BUNDLE_RESULT_ITEMS = AdventuresmithActivity::class.java.name + ".resultItems"

    override fun onSaveInstanceState(outState: Bundle?) {
        resultAdapter!!.saveInstanceState(outState)

        drawerHeader!!.saveInstanceState(outState)
        drawer!!.saveInstanceState(outState)

        outState!!.putSerializable(BUNDLE_CURRENT_DRAWER_ITEM, currentDrawerItemId)

        val results : ArrayList<ResultItem> = ArrayList()
        results.addAll(resultAdapter!!.adapterItems)
        outState!!.putSerializable(BUNDLE_RESULT_ITEMS, results)

        super.onSaveInstanceState(outState)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        // NOTE: currentDrawerItem _may_ have saved null
        selectDrawerItem(savedInstanceState!!.getSerializable(BUNDLE_CURRENT_DRAWER_ITEM) as Long?)
        val restoredResults: ArrayList<ResultItem> = savedInstanceState.getSerializable(BUNDLE_RESULT_ITEMS) as ArrayList<ResultItem>
        resultAdapter!!.clear()
        resultAdapter!!.add(restoredResults)

        buttonAdapter!!.withSavedInstanceState(savedInstanceState)
        resultAdapter!!.withSavedInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (drawer != null && drawer!!.isDrawerOpen())
            drawer!!.closeDrawer()
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (menu == null)
            return super.onCreateOptionsMenu(menu)

        menu.findItem(R.id.action_clear).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_delete)
                .color(Color.WHITE)
                .actionBar()
        menu.findItem(R.id.search).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_magnify)
                .color(Color.WHITE)
                .actionBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val searchView = menu.findItem(R.id.search).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    resultAdapter!!.filter(newText)
                    appbar.setExpanded(false,false)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    resultAdapter!!.filter(query)
                    appbar.setExpanded(false,false)
                    return true
                }
            })
        } else {
            menu.findItem(R.id.search).setVisible(false)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item != null && item.itemId == R.id.action_clear) {
            // clear results
            resultAdapter!!.clear()
            resultAdapter!!.notifyAdapterDataSetChanged()
            // expand buttons
            appbar.setExpanded(true,true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @TargetApi(Build.VERSION_CODES.N)
        fun getCurrentLocale(r: Resources): Locale {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return r.configuration.locales.get(0)
            } else {
                return r.configuration.locale
            }
        }
        fun getCollectionIcon(id: String, grpId : String? = null): IIcon {

            return when(id) {
                DiceConstants.CollectionName -> CommunityMaterial.Icon.cmd_dice_6
                AdventuresmithCore.MazeRats -> CommunityMaterial.Icon.cmd_castle
                FotfConstants.GROUP -> Ionicons.Icon.ion_map
                AdventuresmithCore.FourthPage -> CommunityMaterial.Icon.cmd_numeric_4_box_outline
                AdventuresmithCore.PerilousWilds -> {
                    when (grpId) {
                        "grp1" -> CommunityMaterial.Icon.cmd_image_filter_hdr // dangers & discov
                        "grp2" -> CommunityMaterial.Icon.cmd_book_open_page_variant // create & name
                        "grp3" -> Ionicons.Icon.ion_ios_paw // creature
                        "grp4" -> CommunityMaterial.Icon.cmd_account_multiple // npcs
                        "grp5" -> CommunityMaterial.Icon.cmd_white_balance_irradescent // treasure
                        else -> CommunityMaterial.Icon.cmd_folder_multiple_image
                    }
                }
                SwnConstantsCustom.GROUP ->  {
                    when (grpId) {
                        "grp1" -> Ionicons.Icon.ion_planet // aliens, animals, worlds
                        "grp2" -> Ionicons.Icon.ion_person_stalker // npcs, corps, religions
                        "grp3" -> Ionicons.Icon.ion_edit // names
                        "grp4" -> CommunityMaterial.Icon.cmd_factory // tech, architecture
                        else -> CommunityMaterial.Icon.cmd_rocket
                    }
                }
                AdventuresmithCore.Stonetop -> CommunityMaterial.Icon.cmd_barley//CommunityMaterial.Icon.cmd_pine_tree
                else -> CommunityMaterial.Icon.cmd_help_circle
            }
        }
    }
}
