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

import android.graphics.*
import android.os.*
import android.support.v7.app.*
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.*
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.*
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
import org.stevesea.adventuresmith.R
import org.stevesea.adventuresmith.core.*
import java.util.*

class AdventuresmithActivity : AppCompatActivity(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)
    val generatorCollections : Set<CollectionMetaDto> by kodein.instance(AdventureSmithConstants.GENERATORS)
    val generatorMap : Map<String, Generator> by kodein.instance(AdventureSmithConstants.GENERATORS)

    var drawerIdToGenerators : Map<Int, Generator>? = null

    private var drawerHeader: AccountHeader? = null
    private var drawer: Drawer? = null

    val navDrawerItems : List<IDrawerItem<*,*>> by lazy {
        drawerIdToGenerators = mutableMapOf()

        val result: MutableList<IDrawerItem<*,*>> = mutableListOf()

        var previousWasExpandable = false
        for (coll in generatorCollections) {

            if (coll.groups != null && coll.groups!!.isNotEmpty()) {
                // has groups, create header & children
                val expandableItem = ExpandableDrawerItem()
                        .withName(coll.name)
                        .withIcon(getCollectionIcon(coll.id))
                        .withIdentifier(Objects.hash(coll.id).toLong())
                        .withSelectable(false)
                        .withIsExpanded(false)
                for (grp in coll.groups!!.entries) {
                    val childItem = SecondaryDrawerItem()
                            .withName(grp.value)
                            .withIcon(getCollectionIcon(coll.id, grp.key))
                            .withIdentifier(Objects.hash(coll.id, grp.key).toLong())
                            .withSelectable(true)
                            .withLevel(2)
                    expandableItem.withSubItems(childItem)
                }
                result.add(expandableItem)
                previousWasExpandable = true
            } else {
                if (previousWasExpandable) {
                    result.add(DividerDrawerItem())
                }
                // no groups, just create item
                result.add(PrimaryDrawerItem()
                        .withName(coll.name)
                        .withIcon(getCollectionIcon(coll.id))
                        .withIdentifier(coll.hashCode().toLong())
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
                .withIdentifier(Objects.hash("thanks").toLong())
                .withIcon(CommunityMaterial.Icon.cmd_information_outline)
        )
        result.add(SecondaryDrawerItem()
                .withName(R.string.nav_about)
                .withLevel(1)
                .withIdentifier(Objects.hash("about").toLong())
                .withIcon(CommunityMaterial.Icon.cmd_help)
        )

        result
    }

    val resultAdapter by lazy {
        FastItemAdapter<ResultItem>()
                .withSelectable(true)
                .withMultiSelect(true)
                .withSelectOnLongClick(true)
                .withPositionBasedStateManagement(true)
                .withOnLongClickListener( object : FastAdapter.OnLongClickListener<ResultItem> {
                    override fun onLongClick(v: View?, adapter: IAdapter<ResultItem>?, item: ResultItem?, position: Int): Boolean {
                        // TODO: clipboard send
                        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    val buttonAdapter by lazy {
        FastItemAdapter<GeneratorButton>()
                .withSelectable(false)
                .withPositionBasedStateManagement(true)
                .withOnClickListener ( object : FastAdapter.OnClickListener<GeneratorButton> {
                    override fun onClick(v: View?, adapter: IAdapter<GeneratorButton>?, item: GeneratorButton?, position: Int): Boolean {
                        if (item == null)
                            return false

                        val result = item.generator.generate(
                                this@AdventuresmithActivity.resources.configuration.locales.get(0))

                        resultAdapter.add(0, ResultItem(result))

                        // asldkfjalsdkjfalskjdf
                        return true
                    }
                })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adventuresmith)

        setSupportActionBar(toolbar)

        collapsing_toolbar.title = ""

        drawerHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.header_graphic)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build()

        drawer = DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withToolbar(toolbar)
                .withAccountHeader(drawerHeader!!)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withDrawerItems(navDrawerItems)
                .build()
    }




    override fun onSaveInstanceState(outState: Bundle?) {
        resultAdapter.saveInstanceState(outState)
        // TODO: deliberate skipping button adapter -- it should be re-created on each
        //buttonAdapter.saveInstanceState(outState)

        drawerHeader!!.saveInstanceState(outState)
        drawer!!.saveInstanceState(outState)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        //buttonAdapter.withSavedInstanceState(savedInstanceState)
        resultAdapter.withSavedInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        menu!!.findItem(R.id.action_clear).icon = IconicsDrawable(this,
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
                    // resultAdapter.filter(s)
                    appbar.setExpanded(false,false)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    // resultAdapter.filter(s)
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
        if (item!!.itemId == R.id.action_clear) {
            // clear results
            // resultAdapter.clear
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun getCollectionIcon(id: String, grpId : String? = null): IIcon {

            return when(id) {
                "dice_roller" -> CommunityMaterial.Icon.cmd_dice_6
                "maze_rats" -> CommunityMaterial.Icon.cmd_castle
                "freebooters_on_the_frontier" -> Ionicons.Icon.ion_map
                "fourth_page" -> CommunityMaterial.Icon.cmd_numeric_4_box_outline
                "perilous_wilds" -> {
                    when (grpId) {
                        "grp1" -> Ionicons.Icon.ion_ios_paw // creature
                        "grp2" -> CommunityMaterial.Icon.cmd_image_filter_hdr // dangers & discov
                        "grp3" -> CommunityMaterial.Icon.cmd_white_balance_irradescent // create & name
                        "grp4" -> CommunityMaterial.Icon.cmd_account_multiple // npcs
                        "grp5" -> CommunityMaterial.Icon.cmd_book_open_page_variant // treasure
                        else -> CommunityMaterial.Icon.cmd_folder_multiple_image
                    }
                }
                "stars_without_number" ->  {
                    when (grpId) {
                        "grp1" -> Ionicons.Icon.ion_planet // aliens, animals, worlds
                        "grp2" -> Ionicons.Icon.ion_person_stalker // npcs, corps, religions
                        "grp3" -> Ionicons.Icon.ion_edit // names
                        else -> CommunityMaterial.Icon.cmd_rocket
                    }
                }
                else -> CommunityMaterial.Icon.cmd_help_circle
            }
        }
    }
}
