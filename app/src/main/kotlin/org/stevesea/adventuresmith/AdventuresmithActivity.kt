/*
 * Copyright (c) 2017 Steve Christensen
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

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.base.Stopwatch
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItemAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.EventHook
import com.mikepenz.fastadapter_extensions.ActionModeHelper
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialize.MaterializeBuilder
import com.mikepenz.materialize.util.UIUtils
import kotlinx.android.synthetic.main.activity_adventuresmith.appbar
import kotlinx.android.synthetic.main.activity_adventuresmith.bkg_image
import kotlinx.android.synthetic.main.activity_adventuresmith.collapsing_toolbar
import kotlinx.android.synthetic.main.activity_adventuresmith.nav_tablet
import kotlinx.android.synthetic.main.activity_adventuresmith.recycler_buttons
import kotlinx.android.synthetic.main.activity_adventuresmith.recycler_results
import kotlinx.android.synthetic.main.activity_adventuresmith.toolbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.debug
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.editText
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.info
import org.jetbrains.anko.padding
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.selector
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.textSizeDimen
import org.jetbrains.anko.textView
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.warn
import org.stevesea.adventuresmith.core.AdventuresmithCore
import org.stevesea.adventuresmith.core.CollectionDto
import org.stevesea.adventuresmith.core.CollectionMetaDto
import org.stevesea.adventuresmith.core.Generator
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Random
import java.util.SortedSet
import java.util.concurrent.TimeUnit

data class CollectionAndGroup(val collectionId: String,
                              val name: String,
                              val groupId: String? = null)

class AdventuresmithActivity : AppCompatActivity(),
        AnkoLogger {

    private val DATE_FORMATTER = SimpleDateFormat.getDateTimeInstance()

    private var currentDrawerItemId: Long? = null
    val drawerIdToGroup : MutableMap<Long, CollectionAndGroup> = mutableMapOf()
    val favoriteIdToName : MutableMap<Long, String> = mutableMapOf()

    val ID_ABOUT = "about".hashCode().toLong()
    val ID_THANKS = "thanks".hashCode().toLong()
    val ID_FAVORITES = "favorites".hashCode().toLong()
    val ID_GENERATE_MANY = "generate many".hashCode().toLong()

    private var drawerHeader: AccountHeader? = null
    private var drawer: Drawer? = null

    val resultAdapter : FastItemAdapter<ResultItem> by lazy {
        val res = FastItemAdapter<ResultItem>()
                .withSelectable(true)
                .withMultiSelect(true)
                .withSelectOnLongClick(true)
                .withPositionBasedStateManagement(true)
                .withOnPreClickListener(object : FastAdapter.OnClickListener<ResultItem> {
                    override fun onClick(v: View?, adapter: IAdapter<ResultItem>?, item: ResultItem?, position: Int): Boolean {
                        //we handle the default onClick behavior for the actionMode. This will return null if it didn't do anything and you can handle a normal onClick
                        val res = actionModeHelper.onClick(item)
                        //info("pre-click: position: $position, helperResult: $res ")
                        return res ?: false
                    }
                })
                .withOnClickListener(object : FastAdapter.OnClickListener<ResultItem> {
                    override fun onClick(v: View?, adapter: IAdapter<ResultItem>?, item: ResultItem?, position: Int): Boolean {
                        //info("on-click: position: $position")
                        return false
                    }
                })
                .withOnPreLongClickListener( object : FastAdapter.OnLongClickListener<ResultItem> {
                    override fun onLongClick(v: View?, adapter: IAdapter<ResultItem>?, item: ResultItem?, position: Int): Boolean {

                        val actionMode = actionModeHelper.onLongClick(this@AdventuresmithActivity, position)

                        if (actionMode != null) {
                            //we want color our CAB
                            findViewById(R.id.action_mode_bar).setBackgroundColor(
                                    UIUtils.getThemeColorFromAttrOrRes(this@AdventuresmithActivity,
                                            R.attr.colorPrimary, R.color.material_drawer_primary))
                        }

                        //if we have no actionMode we do not consume the event
                        return actionMode != null
                    }
                }) as FastItemAdapter<ResultItem>

        res.withFilterPredicate(object : IItemAdapter.Predicate<ResultItem> {
            override fun filter(item: ResultItem?, constraint: CharSequence?): Boolean {
                if (item == null || constraint.isNullOrBlank())
                    return false

                //return true if we should filter it out
                //return false to keep it
                return !item.plainText.toLowerCase().contains(constraint.toString().toLowerCase())
            }
        })
        res
    }

    val buttonAdapter : FastItemAdapter<GeneratorButton> by lazy {
        FastItemAdapter<GeneratorButton>()
                .withSelectable(false)
                .withPositionBasedStateManagement(true)
                .withItemEvent(object : ClickEventHook<GeneratorButton>(), EventHook<GeneratorButton> {
                    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                        if (viewHolder is GeneratorButton.ViewHolder) {
                            return viewHolder.btnSettings
                        }
                        return null
                    }

                    override fun onClick(v: View?, position: Int, fastAdapter: FastAdapter<GeneratorButton>?, item: GeneratorButton?) {
                        if (item != null && item.meta.input != null) {
                            val previousConfig = getGeneratorConfig(item.generator.getId())
                            if (item.meta.input?.useWizard as Boolean) {
                                showGenWizard(item, 0, previousConfig, mutableMapOf())
                            } else {
                                showGenCfg(item, previousConfig)
                            }
                        }
                    }

                    private fun showGenCfg(genBtn: GeneratorButton,
                                           oldState: Map<String, String>) {
                        if (genBtn.meta.input == null)
                            return
                        val genId = genBtn.generator.getId()
                        val params = genBtn.meta.input?.params
                        alert(R.string.generator_config) {
                            customView {
                                verticalLayout {
                                    val edits : MutableMap<String, EditText> = mutableMapOf()
                                    params?.forEach {
                                        val k = it.name
                                        val displayVal = oldState.getOrElse(k) { it.defaultValue }

                                        if (!it.helpText.isNullOrEmpty()) {
                                            textView {
                                                padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                                text = it.helpText
                                            }
                                        }
                                        edits.put(k,
                                                editText {
                                                    padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                                    hint = it.uiName
                                                    maxLines = 1
                                                    singleLine = true
                                                    inputType = if (it.numbersOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
                                                    text = Editable.Factory.getInstance().newEditable(displayVal)
                                                }
                                        )
                                    }
                                    okButton {
                                        val newState : MutableMap<String, String> = mutableMapOf()
                                        edits.forEach {
                                            newState.put(it.key, it.value.text.toString().trim())
                                        }
                                        debug("new state: " + newState)
                                        genBtn.updateInputMap(newState)
                                        setGeneratorConfig(genId, newState)
                                    }
                                }
                            }
                        }.show()
                    }

                    private fun showGenWizard(genBtn: GeneratorButton,
                                              stepInd: Int,
                                              oldState: Map<String, String>,
                                              newState: MutableMap<String, String>) {
                        if (genBtn.meta.input == null)
                            return
                        val genId = genBtn.generator.getId()
                        val params = genBtn.meta.input?.params ?: throw IllegalArgumentException("missing params")
                        val param = params[stepInd]
                        val k = param.name
                        // if entry is in newState, use it. Otherwise fall back to previous config. Otherwise fallback to default value
                        val displayVal = newState.getOrElse(k) { oldState.getOrElse(k) { param.defaultValue } }
                        val isFirstPage = stepInd == 0
                        val isFinalPage = stepInd == params.size - 1
                        if (param.values == null) {
                            alert(R.string.generator_config) {
                                customView {
                                    verticalLayout {
                                        if (!param.helpText.isNullOrEmpty()) {
                                            textView {
                                                padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                                text = param.helpText
                                            }
                                        }
                                        val curEdit = editText {
                                            padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                            hint = param.uiName
                                            maxLines = 1
                                            singleLine = true
                                            inputType = if (param.numbersOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
                                            text = Editable.Factory.getInstance().newEditable(displayVal)
                                        }
                                        if (isFinalPage) {
                                            okButton {
                                                newState.put(k, curEdit.text.toString().trim())
                                                genBtn.updateInputMap(newState)
                                                setGeneratorConfig(genId, newState)
                                            }
                                        } else {
                                            positiveButton("Next") {
                                                newState.put(k, curEdit.text.toString().trim())
                                                showGenWizard(genBtn, stepInd + 1, oldState, newState)
                                            }
                                        }
                                        if (!isFirstPage) {
                                            negativeButton("Prev") {
                                                newState.put(k, curEdit.text.toString().trim())
                                                showGenWizard(genBtn, stepInd - 1, oldState, newState)
                                            }
                                        }
                                    }
                                }
                            }.show()
                        } else {
                            val sellist = param.values.orEmpty()
                            selector(param.helpText, sellist) { i ->
                                val selected = sellist.get(i)
                                newState.put(k, selected)
                                if (isFinalPage) {
                                    genBtn.updateInputMap(newState)
                                    setGeneratorConfig(genId, newState)
                                } else {
                                    showGenWizard(genBtn, stepInd + 1, oldState, newState)
                                }
                            }
                        }
                    }

                })
                .withOnLongClickListener(object : FastAdapter.OnLongClickListener<GeneratorButton> {
                    override fun onLongClick(v: View?, adapter: IAdapter<GeneratorButton>?, item: GeneratorButton?, position: Int): Boolean {
                        if (item == null)
                            return false

                        val generator = item.generator
                        val genid = generator.getId()

                        if (drawerIdToGroup.containsKey(currentDrawerItemId)) {
                            // if the current drawer selection is a regular gen group, then user is adding a favorite
                            val favList = getFavoriteGroups().toList()

                            if (favList.isEmpty()) {
                                // no fav groups, add it to 'Default'
                                addFavoriteGroup(getString(R.string.fav_group_default))
                                addFavoriteToGroup(getString(R.string.fav_group_default), genid)
                                updateFavGroupsInNavDrawer(false)
                                toast(R.string.fav_added)
                            } else if (favList.size == 1) {
                                // if there's only one fav group, don't make user select
                                addFavoriteToGroup(favList.get(0), genid)
                                toast(R.string.fav_added)
                            } else {
                                // if there are multiple fav groups, allow user to select one
                                selector(getString(R.string.fav_add_to_group), favList) { i ->
                                    val favGroup = favList.get(i)
                                    addFavoriteToGroup(favGroup, genid)
                                }
                            }
                            return true
                        } else if (favoriteIdToName.containsKey(currentDrawerItemId)) {
                            // otherwise, they're on a fav-group draweritem.
                            // long-click means remove a favorite from the current nav item
                            val favGroup = favoriteIdToName.get(currentDrawerItemId)
                            favGroup?.let {
                                alert(item.name, getString(R.string.fav_remove)) {
                                    yesButton {
                                        removeFavoriteFromGroup(favGroup, genid)
                                        synchronized(buttonAdapter) {
                                            buttonAdapter.remove(position)
                                        }
                                    }
                                    noButton {}
                                }.show()
                            }
                            return true
                        }
                        return false
                    }
                })
                .withOnClickListener(object : FastAdapter.OnClickListener<GeneratorButton> {
                    override fun onClick(v: View?, adapter: IAdapter<GeneratorButton>?, item: GeneratorButton?, position: Int): Boolean {
                        if (item == null)
                            return false

                        if (!currentFilter.isNullOrBlank()) {
                            toast("Disable filter to generate more results")
                            return false
                        }

                        val num_to_generate = if (settingsGenerateMany) settingsGenerateManyCount else 1
                        val generator = item.generator
                        val currentLocale = getCurrentLocale()

                        val inputConfig = getGeneratorConfig(generator.getId())

                        setProgressDialog(indeterminateProgressDialog(R.string.generating_dlg_title))
                        doAsync {
                            try {
                                val genMeta = generator.getMetadata(getCurrentLocale())

                                val stopwatch = Stopwatch.createStarted()
                                val resultItems: MutableList<String> = mutableListOf()
                                for (i in 1..num_to_generate) {
                                    try {
                                        resultItems.add(generator.generate(currentLocale, inputConfig))
                                    } catch (e: Exception) {
                                        warn(e.toString(), e)
                                        resultItems.add(
                                                "<font color=\"#b71c1c\">" +
                                                        e.message.orEmpty() +
                                                        "</font>"
                                        )
                                    }
                                }
                                stopwatch.stop()

                                Answers.getInstance().logCustom(
                                        CustomEvent("Generate")
                                                .putCustomAttribute("NumGenerated", num_to_generate)
                                                .putCustomAttribute("ElapsedMS", stopwatch.elapsed(TimeUnit.MILLISECONDS))
                                                .putCustomAttribute("GeneratorId", item.generator.getId())
                                )

                                uiThread {
                                    synchronized(resultAdapter) {
                                        resultAdapter.add(0, resultItems.filterNotNull().map { ResultItem(it, genMeta.useIconicsTextView) })

                                        recycler_results.scrollToPosition(0)
                                        debug("Number of items ${resultAdapter.adapterItemCount}")
                                    }
                                }
                            } finally {
                                dismissProgressDialog()
                            }
                        }
                        return true
                    }
                })
                as FastItemAdapter<GeneratorButton>
    }

    val actionModeHelper: ActionModeHelper by lazy {
        ActionModeHelper(resultAdapter, R.menu.result_select_menu, object : ActionMode.Callback {
            private fun hideUsualToolbar() {
                appbar.visibility = View.GONE
                appbar.setExpanded(false, false)
            }
            private fun showUsualToolbar() {
                appbar.visibility = View.VISIBLE
                appbar.setExpanded(true, true)
            }
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                // TODO: http://stackoverflow.com/questions/24737622/how-add-copy-to-clipboard-to-custom-intentchooser
                // TODO: https://gist.github.com/mediavrog/5625602

                if (item != null) {
                    if (item.itemId == R.id.action_delete) {
                        synchronized(resultAdapter) {
                            resultAdapter.deleteAllSelectedItems()
                        }
                        mode?.finish()
                        showUsualToolbar()
                        return true // consume
                    }

                    val ts = DATE_FORMATTER.format(GregorianCalendar.getInstance().time)
                    val subj = "${applicationContext.getString(R.string.app_name)} $ts"

                    synchronized(resultAdapter) {
                        val selectedText = resultAdapter.selectedItems.map { it.spannedText.toString() }.joinToString("\n\n#############\n\n")
                        val selectedTextHtml = resultAdapter.selectedItems.map { it.htmlTxt }.joinToString("\n<hr/>\n")

                        if (item.itemId == R.id.action_share) {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_SUBJECT, subj)
                            intent.putExtra(Intent.EXTRA_TEXT, selectedText)
                            intent.putExtra(Intent.EXTRA_HTML_TEXT, selectedTextHtml)
                            startActivity(Intent.createChooser(intent, null))

                            mode?.finish()
                            showUsualToolbar()
                            resultAdapter.deselect()
                            return true // consume
                        }
                    }
                }
                return false
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                hideUsualToolbar()
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                showUsualToolbar()
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                hideUsualToolbar()
                return false
            }
        })
    }

    var currentFilter : String? = null

    val sharedPreferences: SharedPreferences by lazy {
        applicationContext.defaultSharedPreferences
    }

    val SETTING_GEN_MANY = "GenerateMany"
    val SETTING_GEN_MANY_COUNT = "GenerateMany.Count"
    var settingsGenerateMany : Boolean
        get() {
            return sharedPreferences.getBoolean(SETTING_GEN_MANY, false)
        }
        set(value) {
            sharedPreferences.edit().putBoolean(SETTING_GEN_MANY, value).apply()
        }
    var settingsGenerateManyCount : Int
        get() {
            return sharedPreferences.getInt(SETTING_GEN_MANY_COUNT, 12)
        }
        set(value) {
            sharedPreferences.edit().putInt(SETTING_GEN_MANY_COUNT, value).apply()
        }

    val SETTING_FAVORITE_GROUPS = "FavoriteGroups"
    var settingsFavoriteGroups : Set<String>
        get() {
            return sharedPreferences.getStringSet(SETTING_FAVORITE_GROUPS, setOf(getString(R.string.fav_group_default)))
        }
        set(value) {
            sharedPreferences.edit().putStringSet(SETTING_FAVORITE_GROUPS, value).apply()
        }

    val SETTING_FAVORITE_CONTENTS_PREFIX = "FavoriteGroup."

    fun getFavoriteGroups() : SortedSet<String> {
        return settingsFavoriteGroups.toSortedSet()
    }

    fun addFavoriteGroup(newGroupName: String) {
        val newVals : MutableSet<String> = mutableSetOf()
        newVals.addAll(getFavoriteGroups())
        newVals.add(newGroupName)
        settingsFavoriteGroups = newVals
    }
    fun removeFavoriteGroup(removeGroup: String) {
        val newVals : MutableSet<String> = mutableSetOf()
        newVals.addAll(getFavoriteGroups())
        newVals.remove(removeGroup)

        sharedPreferences.edit()
                .remove(getFavoriteSettingKey(removeGroup))
                .putStringSet(SETTING_FAVORITE_GROUPS, newVals)
                .apply()
    }

    fun renameFavoriteGroup(oldGroupName: String, newGroupName: String) {
        val newGroups : MutableSet<String> = mutableSetOf()
        newGroups.addAll(getFavoriteGroups())
        newGroups.remove(oldGroupName)
        newGroups.add(newGroupName)

        // user might re-name a group to an existing group, if so merge the two together
        val totalVals : MutableSet<String> = mutableSetOf()
        val oldGroupVals = getFavorites(oldGroupName)
        val targetGroupVals = getFavorites(newGroupName)
        totalVals.addAll(oldGroupVals)
        totalVals.addAll(targetGroupVals)

        sharedPreferences.edit()
                .putStringSet(getFavoriteSettingKey(newGroupName), totalVals)
                .remove(getFavoriteSettingKey(oldGroupName))
                .putStringSet(SETTING_FAVORITE_GROUPS, newGroups)
                .apply()
    }

    private fun getFavoriteSettingKey(groupName: String) : String {
        return "${SETTING_FAVORITE_CONTENTS_PREFIX}.${groupName}"
    }

    fun getFavorites(groupName : String) : SortedSet<String> {
        return sharedPreferences.getStringSet(getFavoriteSettingKey(groupName), setOf()).toSortedSet()
    }
    fun getFavoriteGenerators(groupName: String) : List<Generator> {
        return AdventuresmithCore.getGeneratorsByIds(
                getFavorites(groupName)
        )
    }
    fun addFavoriteToGroup(groupName: String, genId: String) {
        info("adding favorite: $groupName - $genId")
        val newVals : MutableSet<String> = mutableSetOf()
        newVals.addAll(getFavorites(groupName))
        newVals.add(genId)
        sharedPreferences.edit()
                .putStringSet(getFavoriteSettingKey(groupName), newVals).apply()
    }
    fun removeFavoriteFromGroup(groupName: String, genId: String) {
        info("removing favorite: $groupName - $genId")
        val curVals : MutableSet<String> = mutableSetOf()
        curVals.addAll(getFavorites(groupName))
        curVals.remove(genId)
        sharedPreferences.edit()
                .putStringSet(getFavoriteSettingKey(groupName), curVals).apply()
    }

    val SETTING_GENERATOR_CONFIG_PREFIX = "GeneratorConfig."
    fun getGeneratorConfig(genId: String) : Map<String, String> {
        val str = sharedPreferences.getString(SETTING_GENERATOR_CONFIG_PREFIX + genId, "")
        if (str.isNullOrEmpty()) {
            return mapOf()
        } else {
            return objectReader.forType(object : TypeReference<Map<String, String>>() {}).readValue(str)
        }
    }
    fun setGeneratorConfig(genId: String, value: Map<String, String>) {
        val str = objectWriter.writeValueAsString(value)
        sharedPreferences.edit()
                .putString(SETTING_GENERATOR_CONFIG_PREFIX + genId, str)
                .apply()
    }

    val random = Random()

    fun <T> pick(items: Collection<T>) : T {
        // use modulo because the randomizer might be a mock that's been setup to do something dumb
        // and returns greater than the # of items in list
        return items.elementAt(random.nextInt(items.size) % items.size)
    }

    val headerImages = listOf(
            R.drawable.header_graphic,
            R.drawable.header_graphic_b
    )
    val bkgImages = listOf(
            R.drawable.alexander_great_bats_rats_1,
            R.drawable.alexander_great_bats_rats_2,
            R.drawable.animal_geometry,
            R.drawable.bear_army,
            R.drawable.cockatrice_crocodile,
            R.drawable.death_king_arthur,
            R.drawable.death_lion,
            R.drawable.demon_glasses,
            R.drawable.elephant_rat,
            R.drawable.fallen_angels,
            R.drawable.fallen_angels_2,
            R.drawable.goat_musician,
            R.drawable.green_demon_1,
            R.drawable.griffin_hog_hug,
            R.drawable.initial_d,
            R.drawable.organ_bunny,
            R.drawable.perseus_and_andromeda,
            R.drawable.rabbit_knight,
            R.drawable.sagittarius,
            R.drawable.siege_rabbits,
            R.drawable.snail_and_turtle
    )

    fun getFavoriteGroupDrawerItem(grpName: String) : SecondaryDrawerItem {
        val id = "favorites/$grpName".hashCode().toLong()
        favoriteIdToName.put(id, grpName)
        return SecondaryDrawerItem()
                .withName(grpName)
                //.withIcon(CommunityMaterial.Icon.cmd_star_outline)
                .withIdentifier(id)
                .withSelectable(true)
                .withLevel(2)
    }

    fun getFavoriteGroupDrawerItems() : List<SecondaryDrawerItem> {
        favoriteIdToName.clear()
        return getFavoriteGroups().map {
            val item = getFavoriteGroupDrawerItem(it)
            favoriteIdToName.put(item.identifier, it)
            item
        }
    }

    fun updateFavGroupsInNavDrawer(selectFavs: Boolean = true) {
        favExpandItem.subItems.clear()
        favExpandItem.subItems.addAll(getFavoriteGroupDrawerItems())
        // TODO: hardcoding favposition... getPos always returned -1
        //val favPosition = drawer!!.getPosition(favExpandItem)
        drawer?.adapter?.notifyAdapterSubItemsChanged(1)
        // notifying dataset changed just messed up rendering... dunno why
        //drawer!!.adapter.notifyAdapterDataSetChanged()

        if (selectFavs) {
            drawer?.adapter?.expand(1)
            drawer?.adapter?.select(1)
        }
    }

    val favExpandItem by lazy {
        ExpandableDrawerItem()
                .withName(R.string.nav_favs)
                .withIdentifier(ID_FAVORITES)
                .withIcon(CommunityMaterial.Icon.cmd_star_outline)
                .withSelectable(false)
                .withIsExpanded(false)
                .withSubItems(getFavoriteGroupDrawerItems())
                .withDescriptionTextColorRes(R.color.textSecondary)
    }

    fun getNavDrawerItems() : List<IDrawerItem<*, *>> {
        //info("Creating navDrawerItems")
        drawerIdToGroup.clear()
        favoriteIdToName.clear()

        val result: MutableList<IDrawerItem<*, *>> = mutableListOf()

        result.add(favExpandItem)

        val collections = getCachedCollections()
        val collectionMetas = getCachedCollectionMetas(getCurrentLocale())

        for ((collId, coll) in collections) {

            val collMeta = collectionMetas[collId]
            if (collMeta == null) {
                error("unknown coll: $collId")
                continue
            }
            debug("collection: $collId")

            if (!collMeta.hasGroups) {
                // no groups, just create item
                val navId = collId.hashCode().toLong()
                drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = collId, name = collMeta.name))
                result.add(PrimaryDrawerItem()
                        .withName(collMeta.name)
                        .withIcon(IconicsDrawable(this, coll.icon).icon)
                        .withIdentifier(navId)
                        .withSelectable(true)
                        .withDescription(collMeta.desc)
                        .withDescriptionTextColorRes(R.color.textSecondary)
                )
            } else {
                // has groups, create expandable item and populate it.

                val collExpandableItem = ExpandableDrawerItem()
                        .withName(collMeta.name)
                        .withIcon(IconicsDrawable(this, coll.icon).icon)
                        .withIdentifier(collId.hashCode().toLong())
                        .withDescription(collMeta.desc)
                        .withDescriptionTextColorRes(R.color.textSecondary)
                        .withSelectable(false)
                        .withIsExpanded(false)

                collMeta.groups?.forEach { grpMeta ->

                    val subItems = grpMeta.groups.map { subGrpMeta ->

                        val navId = "${collId}.${subGrpMeta.name}".hashCode().toLong()
                        drawerIdToGroup.put(navId, CollectionAndGroup(
                                collectionId = collId,
                                name = "${collMeta.name} / ${subGrpMeta.uiName}",
                                groupId = subGrpMeta.name))

                        SecondaryDrawerItem()
                                .withName(subGrpMeta.uiName)
                                .withIcon(IconicsDrawable(this, subGrpMeta.icon).icon)
                                .withIdentifier(navId)
                                .withSelectable(true)
                                .withLevel(3)
                    }

                    // this is just added beneath collection
                    val navId = "${collId}.${grpMeta.name}".hashCode().toLong()

                    if (subItems.isNotEmpty()) {
                        // if there are subitems, we need to put them under another expander
                        val grpGrp = ExpandableDrawerItem()
                                .withName(grpMeta.uiName)
                                .withLevel(2)
                                .withSelectable(false)
                                .withIsExpanded(false)
                                .withIdentifier(navId)
                                .withIcon(IconicsDrawable(this, grpMeta.icon).icon)
                                .withSubItems(subItems)
                        collExpandableItem.withSubItems(grpGrp)
                    } else {
                        drawerIdToGroup.put(navId, CollectionAndGroup(
                                collectionId = collId,
                                name = "${collMeta.name} / ${grpMeta.uiName}",
                                groupId = grpMeta.name))

                        val grpItem = SecondaryDrawerItem()
                                .withName(grpMeta.uiName)
                                .withIcon(IconicsDrawable(this, grpMeta.icon).icon)
                                .withIdentifier(navId)
                                .withSelectable(true)
                                .withLevel(2)
                        collExpandableItem.withSubItems(grpItem)
                    }
                }
                result.add(collExpandableItem)
            }
        }
        result.add(SectionDrawerItem()
                .withName(R.string.section_header_settings))
        result.add(SecondarySwitchDrawerItem()
                .withName(R.string.settings_generate_many)
                .withSelectable(false)
                .withChecked(settingsGenerateMany)
                .withIdentifier(ID_GENERATE_MANY)
                .withIcon(CommunityMaterial.Icon.cmd_stackoverflow)
                .withOnCheckedChangeListener(object : OnCheckedChangeListener {
                    override fun onCheckedChanged(drawerItem: IDrawerItem<*, *>?, buttonView: CompoundButton?, isChecked: Boolean) {
                        settingsGenerateMany = isChecked
                    }
                })
        )
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

    fun useStaticNavbar() : Boolean {
        return resources.getDimension(R.dimen.navigation_menu_width) > 0
    }

    override fun onPause() {
        dismissProgressDialog()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val stopwatch = Stopwatch.createStarted()
        info("Activity onCreate started: $stopwatch (since app start: ${AdventuresmithApp.watch})")

        // seems like this bug https://github.com/mikepenz/Android-Iconics/issues/169
        //LayoutInflaterCompat.setFactory(getLayoutInflater(), IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adventuresmith)

        setSupportActionBar(toolbar)

        MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(false)
                .withStatusBarPadding(true)
                .build()

        collapsing_toolbar.title = ""

        bkg_image.setImageResource(pick(bkgImages))

        drawerHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(useStaticNavbar())
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(pick(headerImages))
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                /*
                .withProfileImagesVisible(false)
                .withCloseDrawerOnProfileListClick(false)
                .withOnAccountHeaderListener( object: AccountHeader.OnAccountHeaderListener {
                    override fun onProfileChanged(view: View?, profile: IProfile<*>?, current: Boolean): Boolean {
                        if (profile == null || current) {
                            return false
                        }
                        when (profile.identifier) {
                            HEADER_ID_ADVENTURESMITH_CORE -> info("core selected")
                            HEADER_ID_FAVORITES -> info("favorites selected")
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false
                    }
                }
                )
                .addProfiles(
                        // TOOD: rather than profiles... if we're always gonna have 2 choices, just stick another settings toggle?
                        ProfileDrawerItem()
                                .withName("Adventuresmith Core")
                                //.withNameShown(true)
                                .withIdentifier(HEADER_ID_ADVENTURESMITH_CORE)
                                .withTextColorRes(R.color.textPrimary)
                                .withSelectedTextColorRes(R.color.colorPrimaryDark)
                                .withSelectedColorRes(R.color.colorPrimaryLightLight)
                                .withIcon(IconicsDrawable(this, CommunityMaterial.Icon.cmd_cube_outline)
                                        .colorRes(R.color.colorPrimaryDark))
                        ,
                        ProfileDrawerItem()
                                .withName("Favorites")
                                .withIdentifier(HEADER_ID_FAVORITES)
                                .withTextColorRes(R.color.textPrimary)
                                .withSelectedTextColorRes(R.color.colorPrimaryDark)
                                .withSelectedColorRes(R.color.colorPrimaryLightLight)
                                .withIcon(IconicsDrawable(this, CommunityMaterial.Icon.cmd_star_outline)
                                        .colorRes(R.color.colorPrimaryDark))
                )
                */
                .build()

        val drawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withToolbar(toolbar)
                .withAccountHeader(drawerHeader ?: AccountHeaderBuilder().build(), false)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withDrawerItems(getNavDrawerItems())
                .withOnDrawerItemLongClickListener(object : Drawer.OnDrawerItemLongClickListener {
                    override fun onItemLongClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
                        if (drawerItem == null)
                            return false

                        val drawerItemId = drawerItem.identifier

                        val favName = favoriteIdToName.get(drawerItemId)
                        if (favName != null) {
                            // the long-clicked drawer is a favorite group, they either want to rename it or remove it
                            val oldVals = getFavoriteGenerators(favName)
                            if (oldVals.isEmpty()) {
                                alert(favName, getString(R.string.fav_group_remove)) {
                                    yesButton {
                                        removeFavoriteGroup(favName)
                                        updateFavGroupsInNavDrawer()
                                    }
                                    noButton {}
                                }.show()
                            } else {
                                alert(R.string.fav_group_rename) {
                                    customView {
                                        verticalLayout {
                                            val groupName = editText {
                                                padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                                hint = getString(R.string.fav_group)
                                                maxLines = 1
                                                singleLine = true
                                                inputType = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_CAP_WORDS
                                                text = Editable.Factory.getInstance().newEditable(favName)
                                            }
                                            positiveButton(getString(R.string.btn_rename)) {
                                                val newGrpName = groupName.text.toString().trim()
                                                if (!newGrpName.isNullOrBlank()) {
                                                    renameFavoriteGroup(favName, newGrpName)
                                                    updateFavGroupsInNavDrawer()
                                                }
                                            }
                                        }
                                    }
                                }.show()
                            }
                            return true
                        } else if (drawerItemId == ID_FAVORITES) {
                            alert (R.string.fav_group_create) {
                                customView {
                                    verticalLayout {
                                        val groupName = editText {
                                            padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                            hint = getString(R.string.fav_group)
                                            maxLines = 1
                                            singleLine = true
                                            inputType = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_CAP_WORDS
                                        }
                                        positiveButton(getString(R.string.btn_create)) {
                                            val newGrpName = groupName.text.toString().trim()
                                            if (!newGrpName.isNullOrBlank()) {
                                                addFavoriteGroup(newGrpName)
                                                updateFavGroupsInNavDrawer()
                                            }
                                        }
                                    }
                                }
                            }.show()
                            return true
                        } else if (drawerItemId == ID_GENERATE_MANY) {
                            val genManyItems = listOf(5, 10, 25, 50, 100)
                            selector(getString(R.string.gen_many_selector), genManyItems.map { it.toString() }) { i ->
                                settingsGenerateManyCount = genManyItems.get(i)
                            }
                        }
                        return false
                    }

                })
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
                        //info("Drawer item selected: $drawerItemId")
                        if (drawerIdToGroup.containsKey(drawerItemId) || favoriteIdToName.containsKey(drawerItemId)) {
                            selectDrawerItem(drawerItemId, null)
                            currentDrawerItemId = drawerItemId
                            return false
                        } else if (drawerItemId == ID_ABOUT) {
                            this@AdventuresmithActivity.startActivity(
                                    android.content.Intent(this@AdventuresmithActivity, AboutActivity::class.java))
                            currentDrawerItemId = null
                        } else if (drawerItemId == ID_THANKS) {
                            this@AdventuresmithActivity.startActivity(
                                    android.content.Intent(this@AdventuresmithActivity, AttributionActivity::class.java))
                            currentDrawerItemId = null
                        }
                        return false
                    }
                })

        if (useStaticNavbar()) {
            drawer = drawerBuilder.buildView()
            nav_tablet.addView(drawer?.slider)
        } else {
            drawer = drawerBuilder.build()
        }

        info("Activity onCreate built drawer: $stopwatch (since app start: ${AdventuresmithApp.watch})")

        val btnSpanShort = resources.getInteger(R.integer.buttonSpanShort)
        val btnSpanRegular = resources.getInteger(R.integer.buttonSpanRegular)
        val btnSpanLong = resources.getInteger(R.integer.buttonSpanLong)
        val buttonGridLayoutMgr = GridLayoutManager(this, resources.getInteger(R.integer.buttonCols))
        buttonGridLayoutMgr.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = buttonAdapter.getAdapterItem(position)
                if (item == null) {
                    return btnSpanRegular
                }
                if (item.meta.input != null) {
                    return btnSpanLong
                }
                val totalLength = item.name.length
                val maxWordLength = item.name.split(" ").map { it.length }.max()
                if (maxWordLength == null) {
                    return btnSpanRegular
                } else if (totalLength > 20) {
                    return btnSpanLong
                } else if (maxWordLength < 6 && item.name.length < 12) {
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

        val resultsGridLayoutMgr = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.resultCols),
                StaggeredGridLayoutManager.VERTICAL)

        recycler_results.layoutManager = resultsGridLayoutMgr
        recycler_results.itemAnimator = DefaultItemAnimator()
        recycler_results.adapter = resultAdapter

        info("Activity onCreate done: $stopwatch (since app start: ${AdventuresmithApp.watch})")
    }

    private fun selectDrawerItem(drawerItemId: Long?, savedInstanceState: Bundle?) {
        debug("selectDraweritem: $drawerItemId")

        if (drawerItemId == null)
            return
        val collGrp = drawerIdToGroup.get(drawerItemId)
        val favName = favoriteIdToName.get(drawerItemId)

        setProgressDialog(indeterminateProgressDialog(R.string.loading_dlg_title))
        doAsync {
            try {
                val getGenSW = Stopwatch.createStarted()
                val generators: List<Generator> =
                        if (collGrp != null) {
                            info("Finding generators... $collGrp ")
                            Answers.getInstance().logCustom(CustomEvent("Selected Dataset")
                                    .putCustomAttribute("Dataset", collGrp.collectionId)
                            )
                            debug("getting generators for: ${collGrp.collectionId} ${collGrp.groupId.orEmpty()}")
                            AdventuresmithCore.getGeneratorsByGroup(
                                    collGrp.collectionId,
                                    collGrp.groupId
                            )
                        } else if (favName != null) {
                            info("Finding favorites... $favName")
                            Answers.getInstance().logCustom(CustomEvent("Selected Favorite"))
                            getFavoriteGenerators(favName)
                        } else {
                            listOf()
                        }
                getGenSW.stop()
                info("Loading generators for drawer selection done ($getGenSW elapsed)")
                Answers.getInstance().logCustom(CustomEvent("GetDrawerGenerators")
                        .putCustomAttribute("elapsedMS", getGenSW.elapsed(TimeUnit.MILLISECONDS))
                )

                val getGenMetaSW = Stopwatch.createStarted()

                generators.forEach { gen ->
                    gen.getMetadata(getCurrentLocale())
                }

                getGenMetaSW.stop()
                info("Loading generator metadata for drawer selection done ($getGenMetaSW elapsed)")
                Answers.getInstance().logCustom(CustomEvent("GetDrawerGeneratorMetas")
                        .putCustomAttribute("elapsedMS", getGenMetaSW.elapsed(TimeUnit.MILLISECONDS))
                )

                uiThread {
                    if (collGrp != null) {
                        toolbar.title = collGrp.name
                    } else if (favName != null) {
                        toolbar.title = getString(R.string.nav_favs) + " / $favName"
                    }
                    synchronized(buttonAdapter) {
                        buttonAdapter.clear()
                        for (g in generators) {
                            buttonAdapter.add(
                                    GeneratorButton(
                                            g,
                                            getGeneratorConfig(g.getId()),
                                            getCurrentLocale())
                            )
                        }
                        buttonAdapter.withSavedInstanceState(savedInstanceState)
                    }
                    appbar.visibility = View.VISIBLE
                    appbar.setExpanded(true, true)
                }
            } finally {
                dismissProgressDialog()
            }
        }
    }

    private val BUNDLE_CURRENT_DRAWER_ITEM = AdventuresmithActivity::class.java.name + ".currentDrawerItem"
    private val BUNDLE_RESULT_ITEMS = AdventuresmithActivity::class.java.name + ".resultItems"

    override fun onSaveInstanceState(outState: Bundle?) {
        val stopwatch = Stopwatch.createStarted()

        synchronized(resultAdapter) {
            resultAdapter.saveInstanceState(outState)

            val results: ArrayList<ResultItem> = ArrayList()
            results.addAll(resultAdapter.adapterItems)
            outState?.putSerializable(BUNDLE_RESULT_ITEMS, results)
        }
        drawerHeader?.saveInstanceState(outState)
        drawer?.saveInstanceState(outState)
        debug("saveInstanceState: $currentDrawerItemId")

        outState?.putSerializable(BUNDLE_CURRENT_DRAWER_ITEM, currentDrawerItemId)

        super.onSaveInstanceState(outState)
        debug("onSaveInstanceState: $stopwatch elapsed")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val stopwatch = Stopwatch.createStarted()
        super.onRestoreInstanceState(savedInstanceState)

        // NOTE: currentDrawerItem _may_ have saved null
        // the following will setup the button adapter
        currentDrawerItemId = savedInstanceState?.getSerializable(BUNDLE_CURRENT_DRAWER_ITEM) as Long?
        debug("restoreInstanceState: $currentDrawerItemId")
        selectDrawerItem(currentDrawerItemId, savedInstanceState)

        // restore previous results
        val restoredResults: ArrayList<ResultItem> = savedInstanceState?.getSerializable(BUNDLE_RESULT_ITEMS) as ArrayList<ResultItem>
        synchronized(resultAdapter) {
            // NOTE: doing deselect after the clear resulted in crash
            resultAdapter.deselect()
            if (actionModeHelper.isActive) {
                actionModeHelper.actionMode.finish()
            }
            resultAdapter.clear()
            resultAdapter.add(restoredResults.filterNotNull())
            resultAdapter.withSavedInstanceState(savedInstanceState)
            resultAdapter.deselect()
        }
        debug("onRestoreInstanceState: $stopwatch elapsed")
    }

    override fun onResume() {
        super.onResume()
        // TODO: between onRestore & onResume, something setting toolbar title to appname.
        //   change it here in onResume to make it consistent.
        if (currentDrawerItemId != null) {
            val collGrp = drawerIdToGroup.get(currentDrawerItemId ?: 0)
            val favName = favoriteIdToName.get(currentDrawerItemId ?: 0)
            if (collGrp != null || favName != null) {
                if (collGrp != null) {
                    toolbar.title = collGrp.name
                } else {
                    toolbar.title = getString(R.string.nav_favs) + " / $favName"
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer != null && drawer?.isDrawerOpen() as Boolean) {
            drawer?.closeDrawer()
        } else {
            alert(R.string.back_button_press) {
                yesButton { super.onBackPressed() }
                noButton {}
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (menu == null)
            return super.onCreateOptionsMenu(menu)

        menu.findItem(R.id.action_clear).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_delete)
                .color(Color.WHITE)
                .actionBar()
        menu.findItem(R.id.action_collection_info).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_information)
                .color(Color.WHITE)
                .actionBar()
        menu.findItem(R.id.search).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_magnify)
                .color(Color.WHITE)
                .actionBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val searchView = menu.findItem(R.id.search).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                fun updateQueryState(newText: String?) {
                    synchronized(resultAdapter) {
                        resultAdapter.filter(newText)
                    }
                    if (newText.isNullOrBlank()) {
                        appbar.setExpanded(true, true)
                        currentFilter = null
                    } else {
                        appbar.setExpanded(false, false)
                        currentFilter = newText
                    }
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    updateQueryState(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    updateQueryState(query)
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
        if (item != null) {
            when (item.itemId) {
                R.id.action_clear -> {
                    if (!currentFilter.isNullOrBlank()) {
                        toast("Disable filter to clear list")
                        return false
                    }
                    // clear results
                    synchronized(resultAdapter) {
                        resultAdapter.clear()
                    }
                    // expand buttons
                    appbar.setExpanded(true, true)
                    return true
                }
                R.id.action_collection_info -> {
                    var str = """
                        See 'Attribution & Thanks' for artwork attribution information.
                        <br/>
                        <br/>
                        Select a set of generators to see their attribution, or go
                        <a href="https://github.com/stevesea/Adventuresmith/blob/master/content_attribution.md">here</a> to see all content attribution.
                        """
                    currentDrawerItemId?.let {
                        val collGrp = drawerIdToGroup.get(currentDrawerItemId!!)
                        if (collGrp != null) {
                            // if currentDrawerItemId isin drawerIdToGroup, that means user has selected
                            // a collection/group and not a favorite
                            val collMeta = getCachedCollectionMetas(getCurrentLocale()).get(collGrp.collectionId) ?: throw IllegalArgumentException("Unable to find metadata for collection ${collGrp.collectionId}")
                            if (collMeta.credit != null)
                                str = collMeta.toHtmlStr()
                        }
                    }
                    alert {
                        customView {
                            verticalLayout {
                                scrollView {
                                    orientation = LinearLayout.VERTICAL
                                    textView {
                                        movementMethod = LinkMovementMethod.getInstance()
                                        setTextIsSelectable(true)
                                        padding = resources.getDimensionPixelSize(R.dimen.alert_text_padding)
                                        text = htmlStrToSpanned(str)
                                        autoLinkMask = Linkify.WEB_URLS
                                        textSizeDimen = R.dimen.resultListFontSize
                                    }
                                }
                                okButton { }
                            }
                        }
                    }.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCurrentLocale(): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getSystemLocale(resources.configuration)
        } else {
            return getSystemLocaleLegacy(resources.configuration)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(cfg: Configuration) : Locale {
        return cfg.locales.get(0)
    }
    @Suppress("DEPRECATION")
    @SuppressWarnings("deprecation")
    fun getSystemLocaleLegacy(cfg: Configuration) : Locale {
        return cfg.locale
    }

    companion object : AnkoLogger {
        val objectMapper by lazy {
            ObjectMapper().registerKotlinModule()
        }
        val objectReader by lazy {
            objectMapper.reader()
        }
        val objectWriter by lazy {
            objectMapper.writer()
        }

        var lastLocale : Locale? = null
        // cache is invalidated if locale changes
        var cachedCollectionMeta : Map<String, CollectionMetaDto> = mapOf()

        private fun getCachedCollectionMetas(l: Locale): Map<String, CollectionMetaDto> {
            synchronized(cachedCollectionMeta) {
                if (l != lastLocale) {
                    val stopwatch = Stopwatch.createStarted()
                    cachedCollectionMeta = AdventuresmithCore.getCollectionMetas(l)
                    stopwatch.stop()
                    info("Loading collection Metas done. took $stopwatch (since app start: ${AdventuresmithApp.watch})")
                    Answers.getInstance().logCustom(CustomEvent("GetCollectionMetas")
                            .putCustomAttribute("elapsedMS", stopwatch.elapsed(TimeUnit.MILLISECONDS))
                            .putCustomAttribute("fromAppStartMS", AdventuresmithApp.watch.elapsed(TimeUnit.MILLISECONDS))
                    )
                    lastLocale = l
                }
                return cachedCollectionMeta
            }
        }
        // this collection is already cached in Core, don't need to create another reference to it.
        // but, it'll be interesting to time it. Also, by having a method to access the data in
        // core, it means we won't get the hang of kodein initialization until _after_ activity is
        // constructed.
        var alreadyInitializedCollections = false
        private fun getCachedCollections() : Map<String, CollectionDto> {
            synchronized(alreadyInitializedCollections) {
                if (!alreadyInitializedCollections) {
                    val stopwatch = Stopwatch.createStarted()
                    try {
                        alreadyInitializedCollections = true
                        return AdventuresmithCore.collections
                    } finally {
                        stopwatch.stop()
                        info("Loading collections done. took $stopwatch (since app start: ${AdventuresmithApp.watch})")
                        Answers.getInstance().logCustom(CustomEvent("GetCollections")
                                .putCustomAttribute("elapsedMS", stopwatch.elapsed(TimeUnit.MILLISECONDS))
                                .putCustomAttribute("fromAppStartMS", AdventuresmithApp.watch.elapsed(TimeUnit.MILLISECONDS))
                        )
                    }
                } else {
                    return AdventuresmithCore.collections
                }
            }
        }

        var progressDlg : ProgressDialog? = null
        @Synchronized fun setProgressDialog(dlg: ProgressDialog) {
            progressDlg = dlg
        }
        @Synchronized fun dismissProgressDialog() {
            if (progressDlg != null && progressDlg?.isShowing ?: false) {
                /*
                val ctxt = (progressDlg?.context as ContextWrapper).baseContext
                if (ctxt is Activity) {
                    val a : Activity = ctxt
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        // isDestroyed only available on 17+
                        if (!a.isFinishing && !a.isDestroyed) {
                            dismissProgressDialogWithCatch()
                        }
                    } else {
                        if (!a.isFinishing) {
                            dismissProgressDialogWithCatch()
                        }
                    }
                } else {
                    dismissProgressDialogWithCatch()
                }
                */
                // the above seems convoluted... just always do the try/catch instead
                dismissProgressDialogWithCatch()
            }
            progressDlg = null
        }
        // fall back to version that swallows exceptions if other checks failed or weren't available
        @Synchronized private fun dismissProgressDialogWithCatch() {
            progressDlg?.let {
                try {
                    progressDlg?.dismiss()
                } catch (ex: Exception) {
                    debug("Ignoring exception thrown when failed to dismiss dialog. ${ex.message}")
                }
            }
        }
    }
}
