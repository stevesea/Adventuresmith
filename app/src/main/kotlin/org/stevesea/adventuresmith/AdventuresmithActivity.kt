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

import android.annotation.*
import android.content.*
import android.content.res.*
import android.graphics.*
import android.os.*
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.*
import android.support.v7.view.ActionMode
import android.support.v7.widget.*
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.InputType
import android.text.InputType.*
import android.view.*
import android.widget.*
import com.crashlytics.android.answers.*
import com.fasterxml.jackson.core.type.TypeReference
import com.google.common.base.Stopwatch
import com.google.common.base.Strings
import com.mikepenz.community_material_typeface_library.*
import com.mikepenz.fastadapter.*
import com.mikepenz.fastadapter.commons.adapters.*
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.EventHook
import com.mikepenz.fastadapter_extensions.*
import com.mikepenz.iconics.*
import com.mikepenz.iconics.context.IconicsLayoutInflater
import com.mikepenz.iconics.typeface.*
import com.mikepenz.materialdrawer.*
import com.mikepenz.materialdrawer.interfaces.*
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialize.MaterializeBuilder
import com.mikepenz.materialize.util.*
import kotlinx.android.synthetic.main.activity_adventuresmith.*
import org.jetbrains.anko.*
import org.stevesea.adventuresmith.core.*
import java.text.*
import java.util.*
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
                            findViewById(R.id.action_mode_bar).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this@AdventuresmithActivity,
                                    R.attr.colorPrimary, R.color.material_drawer_primary))
                        }

                        //if we have no actionMode we do not consume the event
                        return actionMode != null
                    }
                }) as FastItemAdapter<ResultItem>

        res.withFilterPredicate(object : IItemAdapter.Predicate<ResultItem> {
            override fun filter(item: ResultItem?, constraint: CharSequence?): Boolean {
                if (item == null || constraint == null)
                    return false

                //return true if we should filter it out
                //return false to keep it
                return !item.spannedText.toString().toLowerCase().contains(constraint.toString().toLowerCase())
            }
        })
        res
    }


    val buttonAdapter : FastItemAdapter<GeneratorButton> by lazy {
        FastItemAdapter<GeneratorButton>()
                .withSelectable(false)
                .withPositionBasedStateManagement(true)
                .withItemEvent(object: ClickEventHook<GeneratorButton>(), EventHook {
                    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                        if (viewHolder is GeneratorButton.ViewHolder) {
                            return viewHolder.btnSettings
                        }
                        return null
                    }

                    override fun onClick(v: View?, position: Int, fastAdapter: FastAdapter<GeneratorButton>?, item: GeneratorButton?) {
                        if (item != null && item.meta.inputParams.isNotEmpty()) {
                            val previousConfig = getGeneratorConfig(item.generator.getId())
                            if (item.meta.inputParamsUseWizard) {
                                showGenWizard(item.generator.getId(), 0, item.meta.inputParams, previousConfig, mutableMapOf())
                            } else {
                                showGenCfg(item.generator.getId(), item.meta.inputParams, previousConfig)
                            }
                        }
                    }

                    private fun showGenCfg(genId: String,
                                           items: List<InputParamDto>,
                                           oldState: Map<String,String>) {
                        alert(R.string.generator_config) {
                            customView {
                                verticalLayout {
                                    val edits : MutableMap<String, EditText> = mutableMapOf()
                                    items.forEach {
                                        val k = it.name
                                        val displayVal = oldState.getOrElse(k) {it.defaultValue}

                                        if (!it.helpText.isNullOrEmpty()) {
                                            textView {
                                                text = it.helpText
                                            }
                                        }
                                        edits.put(k,
                                                editText {
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
                                        setGeneratorConfig(genId, newState)
                                    }
                                }
                            }
                        }.show()
                    }

                    private fun showGenWizard(genId: String,
                                              stepInd: Int,
                                              items: List<InputParamDto>,
                                              oldState: Map<String,String>,
                                              newState: MutableMap<String, String>) {
                        val item = items.get(stepInd)
                        val k = item.name
                        // if entry is in newState, use it. Otherwise fall back to previous config. Otherwise fallback to default value
                        val displayVal = newState.getOrElse(k) { oldState.getOrElse(k) {item.defaultValue}}
                        val isFirstPage = stepInd == 0
                        val isFinalPage = stepInd == items.size - 1
                        alert(R.string.generator_config) {
                            customView {
                                verticalLayout {
                                    if (!item.helpText.isNullOrEmpty()) {
                                        textView {
                                            text = item.helpText
                                        }
                                    }
                                    val curEdit = editText {
                                        hint = item.uiName
                                        maxLines = 1
                                        singleLine = true
                                        inputType = if (item.numbersOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
                                        text = Editable.Factory.getInstance().newEditable(displayVal)
                                    }
                                    positiveButton(if (isFinalPage) "OK" else "Next") {
                                        newState.put(k, curEdit.text.toString().trim())
                                        debug("new state: " + newState)
                                        if (isFinalPage) {
                                            setGeneratorConfig(genId, newState)
                                        } else {
                                            showGenWizard(genId, stepInd + 1, items, oldState, newState)
                                        }
                                    }
                                    if (!isFirstPage) {
                                        negativeButton("Prev") {
                                            newState.put(k, curEdit.text.toString().trim())
                                            showGenWizard(genId, stepInd - 1, items, oldState, newState)
                                        }
                                    }
                                }
                            }
                        }.show()
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

                            if (favList.size == 1) {
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
                            alert(item.name, getString(R.string.fav_remove)) {
                                yesButton {
                                    removeFavoriteFromGroup(favGroup!!, genid)
                                    synchronized(buttonAdapter) {
                                        buttonAdapter.remove(position)
                                    }
                                }
                                noButton {}
                            }.show()

                            return true
                        }
                        return false
                    }
                })
                .withOnClickListener(object : FastAdapter.OnClickListener<GeneratorButton> {
                    override fun onClick(v: View?, adapter: IAdapter<GeneratorButton>?, item: GeneratorButton?, position: Int): Boolean {
                        if (item == null)
                            return false

                        val num_to_generate = if (settingsGenerateMany) settingsGenerateManyCount else 1
                        val generator = item.generator
                        val currentLocale = getCurrentLocale(resources)

                        val inputConfig = getGeneratorConfig(generator.getId())

                        doAsync {
                            val stopwatch = Stopwatch.createStarted()
                            val resultItems : MutableList<String> = mutableListOf()
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
                                    // disable filter before adding any results
                                    if (currentFilter != null) {
                                        resultAdapter.filter(null)
                                    }

                                    resultAdapter.add(0, resultItems.filterNotNull().map { ResultItem(it) })

                                    recycler_results.scrollToPosition(0)

                                    // re-apply the filter if there is one
                                    if (currentFilter != null) {
                                        debug("Applying filter '$currentFilter'")
                                        resultAdapter.filter(currentFilter)
                                    }
                                    debug("Number of items ${resultAdapter.adapterItemCount}")
                                }
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

                if (item != null && item.itemId == R.id.action_share) {
                    val ts = DATE_FORMATTER.format(GregorianCalendar.getInstance().time)
                    val subj = "${applicationContext.getString(R.string.app_name)} $ts"
                    debug("subject: $subj")

                    synchronized(resultAdapter) {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_SUBJECT, subj)
                        intent.putExtra(Intent.EXTRA_TEXT, resultAdapter.selectedItems.map { it.spannedText.toString() }.joinToString("\n#############\n"))
                        intent.putExtra(Intent.EXTRA_HTML_TEXT, resultAdapter.selectedItems.map { it.htmlTxt }.joinToString("\n<hr/>\n"))
                        startActivity(Intent.createChooser(intent, null))

                        mode!!.finish()
                        showUsualToolbar()
                        resultAdapter.deselect()
                    }
                    return true // consume
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
        if (Strings.isNullOrEmpty(str)) {
            return mapOf()
        } else {
            return AdventuresmithApp.objectReader.forType(object: TypeReference<Map<String, String>>(){}).readValue(str)
        }
    }
    fun setGeneratorConfig(genId: String, value: Map<String, String>) {
        val str = AdventuresmithApp.objectWriter.writeValueAsString(value)
        sharedPreferences.edit()
                .putString(SETTING_GENERATOR_CONFIG_PREFIX + genId, str)
                .apply()
    }



    val shuffler = AdventuresmithCore.shuffler

    val headerImages = listOf(
            R.drawable.header_graphic,
            R.drawable.header_graphic_b
            //R.drawable.dragon,
            //R.drawable.elephant_dragon,
            //R.drawable.pheonix,
            //R.drawable.werewolf
    )
    val bkgImages = listOf(
            R.drawable.armored_angels,
            R.drawable.bards_are_monsters,
            R.drawable.bats,
            R.drawable.bear_army,
            R.drawable.beast_dragon_false_prophet,
            R.drawable.bonnacon,
            R.drawable.book_lovers,
            R.drawable.death_king_arthur,
            R.drawable.death_lion,
            R.drawable.demon_glasses,
            R.drawable.devil_and_god,
            R.drawable.dragons_eyes,
            R.drawable.fall_of_rebel_angels,
            R.drawable.fallen_angels,
            R.drawable.falling_demons,
            R.drawable.fool_riding_goat,
            R.drawable.griffin_hog_hug,
            R.drawable.hell,
            R.drawable.hellmouth,
            R.drawable.hellmouth2,
            R.drawable.horsemen_of_apocalypse,
            R.drawable.infernus,
            R.drawable.initial_d,
            R.drawable.lion_fight,
            R.drawable.mmanticore,
            R.drawable.perseus_and_andromeda,
            R.drawable.seven_headed_dragon,
            R.drawable.siege_rabbits,
            R.drawable.snailfight,
            R.drawable.snake_eater,
            R.drawable.superlion,
            R.drawable.werewolf,
            R.drawable.vain_skeleton
            //R.drawable.alexander_great_chariot,
            //R.drawable.batman,
            //R.drawable.pheonix,
            //R.drawable.mmanticore,
            //R.drawable.falling_demons,
            //R.drawable.feed_beast,
            //R.drawable.infernus,
            //R.drawable.sawtooth,
            //R.drawable.scenes_in_hell,
            //R.drawable.st_anthony_lobster_devil,
            //R.drawable.st_george_and_dragon,
            //R.drawable.sword_scythe,
            //R.drawable.wound_man
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

    fun updateFavGroupsInNavDrawer() {
        favExpandItem.subItems.clear()
        favExpandItem.subItems.addAll(getFavoriteGroupDrawerItems())
        // TODO: hardcoding favposition... getPos always returned -1
        //val favPosition = drawer!!.getPosition(favExpandItem)
        drawer!!.adapter.notifyAdapterSubItemsChanged(1)
        // notifying dataset changed just messed up rendering... dunno why
        //drawer!!.adapter.notifyAdapterDataSetChanged()

        drawer!!.adapter.expand(1)
        drawer!!.adapter.select(1)
    }

    val favExpandItem by lazy {
        ExpandableDrawerItem()
                .withName(R.string.nav_favs)
                .withIdentifier(ID_FAVORITES)
                .withIcon(CommunityMaterial.Icon.cmd_star_outline)
                .withSelectable(false)
                .withIsExpanded(false)
                .withSubItems(getFavoriteGroupDrawerItems())
    }

    fun getNavDrawerItemIcon(id: String, grpId : String? = null): IIcon {
        val collMeta = AdventuresmithCore.getCollectionMetaData(id, getCurrentLocale(resources))

        val collIcon = collMeta.icon
        val iconicsDrawable : IconicsDrawable =
        if (!Strings.isNullOrEmpty(grpId) && collMeta.groupIcons != null) {
            val grpIcon = collMeta.groupIcons!!.getOrElse(grpId!!) {collIcon}
            IconicsDrawable(this, grpIcon)
        } else {
            IconicsDrawable(this, collIcon)
        }

        if (iconicsDrawable.icon == null) {
            error("Couldn't find icon $id.$grpId")
            return CommunityMaterial.Icon.cmd_help
        } else {
            return iconicsDrawable.icon
        }
    }

    fun getNavDrawerItems(locale: Locale) : List<IDrawerItem<*, *>> {
        //info("Creating navDrawerItems")
        drawerIdToGroup.clear()
        favoriteIdToName.clear()

        val stopwatch = Stopwatch.createStarted()
        val generatorCollections = AdventuresmithCore.getCollections(locale)
        stopwatch.stop()
        debug("Loading nav drawer done. took ${stopwatch} (since app start: ${AdventuresmithApp.watch})")

        val result: MutableList<IDrawerItem<*, *>> = mutableListOf()

        result.add(favExpandItem)

        for (coll in generatorCollections) {
            debug("collection: ${coll}")

            if (coll.groups != null && coll.groups!!.isNotEmpty()) {

                // has groups, create header & children
                if (coll.hasGroupHierarchy) {

                    val rootExpandableItem = ExpandableDrawerItem()
                            .withName(coll.name)
                            .withIcon(getNavDrawerItemIcon(coll.id))
                            .withIdentifier(coll.id.hashCode().toLong())
                            .withDescription(coll.desc)
                            .withDescriptionTextColorRes(R.color.textSecondary)
                            .withSelectable(false)
                            .withIsExpanded(false)

                    val groupGroupMap : MutableMap<String, ExpandableDrawerItem> = mutableMapOf()
                    for (grp in coll.groups!!.entries) {
                        val navId = "${coll.id}.${grp.key}".hashCode().toLong()
                        drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = coll.id, name = "${coll.name} / ${grp.value}", groupId = grp.key))

                        val words = grp.value.split("/", limit = 2).map { it.trim() }
                        if (words.size != 2) {
                            error("Invalid group name: $grp")
                        }
                        val grpGrp = words[0]
                        val subGrpName = words[1]

                        val subGrpItem = SecondaryDrawerItem()
                                .withName(subGrpName)
                                .withIcon(getNavDrawerItemIcon(coll.id, grp.key))
                                .withIdentifier(navId)
                                .withSelectable(true)
                                .withLevel(3)

                        var groupGroupItem = groupGroupMap.get(grpGrp)
                        if (groupGroupItem == null) {
                            groupGroupItem = ExpandableDrawerItem()
                                    .withName(grpGrp)
                                    .withLevel(2)
                                    .withSelectable(false)
                                    .withIsExpanded(false)
                                    .withIdentifier(grpGrp.hashCode().toLong())
                                    .withIcon(getNavDrawerItemIcon(coll.id, grpGrp))
                                    .withSubItems(subGrpItem)
                            groupGroupMap.put(grpGrp, groupGroupItem)
                        } else {
                            groupGroupItem.withSubItems(subGrpItem)
                        }
                    }
                    groupGroupMap.values.forEach {
                        rootExpandableItem.withSubItems(it)
                    }

                    result.add(rootExpandableItem)

                } else {
                    val expandableItem = ExpandableDrawerItem()
                            .withName(coll.name)
                            .withIcon(getNavDrawerItemIcon(coll.id))
                            .withIdentifier(coll.id.hashCode().toLong())
                            .withDescription(coll.desc)
                            .withDescriptionTextColorRes(R.color.textSecondary)
                            .withSelectable(false)
                            .withIsExpanded(false)
                    for (grp in coll.groups!!.entries) {
                        val navId = "${coll.id}.${grp.key}".hashCode().toLong()
                        drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = coll.id, name = "${coll.name} / ${grp.value}", groupId = grp.key))
                        val childItem = SecondaryDrawerItem()
                                .withName(grp.value)
                                .withIcon(getNavDrawerItemIcon(coll.id, grp.key))
                                .withIdentifier(navId)
                                .withSelectable(true)
                                .withLevel(2)
                        expandableItem.withSubItems(childItem)
                    }
                    result.add(expandableItem)
                }
            } else {
                // no groups, just create item
                val navId = coll.id.hashCode().toLong()
                drawerIdToGroup.put(navId, CollectionAndGroup(collectionId = coll.id, name = coll.name))
                result.add(PrimaryDrawerItem()
                        .withName(coll.name)
                        .withIcon(getNavDrawerItemIcon(coll.id))
                        .withIdentifier(navId)
                        .withSelectable(true)
                        .withDescription(coll.desc)
                        .withDescriptionTextColorRes(R.color.textSecondary)
                )
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

    fun useStaticNavbar() : Boolean  {
        return resources.getDimension(R.dimen.navigation_menu_width) > 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        LayoutInflaterCompat.setFactory(getLayoutInflater(), IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState)

        debug("AdventuresmithActivity Started: ${AdventuresmithApp.watch}")

        setContentView(R.layout.activity_adventuresmith)

        setSupportActionBar(toolbar)

        MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(false)
                .withStatusBarPadding(true)
                .build()

        collapsing_toolbar.title = ""

        bkg_image.setImageResource(shuffler.pick(bkgImages))

        drawerHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(useStaticNavbar())
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(shuffler.pick(headerImages))
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
                .withAccountHeader(drawerHeader!!, false)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withDrawerItems(getNavDrawerItems(getCurrentLocale(resources)))
                .withOnDrawerItemLongClickListener(object : Drawer.OnDrawerItemLongClickListener {
                    override fun onItemLongClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
                        if (drawerItem == null)
                            return false

                        val drawerItemId = drawerItem.identifier

                        val favName = favoriteIdToName.get(drawerItemId)
                        if (favName != null) {
                            // the long-clicked drawer is a favorite group, they either want to rename it or remove it
                            val oldVals = getFavorites(favName)
                            if (oldVals.size == 0) {
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
                            val genManyItems = listOf(5, 10,25,50,100)
                            selector(getString(R.string.gen_many_selector), genManyItems.map{it.toString()}) { i ->
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
                val item = buttonAdapter.getAdapterItem(position)
                if (item == null) {
                    return btnSpanRegular
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

        //val resultsGridLayoutMgr = GridLayoutManager(this, resources.getInteger(R.integer.resultCols))
        val resultsGridLayoutMgr = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.resultCols),
                StaggeredGridLayoutManager.VERTICAL)

        recycler_results.layoutManager = resultsGridLayoutMgr
        recycler_results.itemAnimator = DefaultItemAnimator()
        recycler_results.adapter = resultAdapter
    }

    private fun selectDrawerItem(drawerItemId: Long?, savedInstanceState: Bundle?) {
        debug("selectDraweritem: $drawerItemId")

        if (drawerItemId == null)
            return
        val collGrp = drawerIdToGroup.get(drawerItemId)
        val favName = favoriteIdToName.get(drawerItemId)
        if (collGrp != null || favName != null) {

            if (collGrp != null) {
                info("selectDraweritem: collGrp : $collGrp")
                Answers.getInstance().logCustom(CustomEvent("Selected Dataset")
                        .putCustomAttribute("Dataset", collGrp.collectionId)
                )

                toolbar.title = collGrp.name
            } else {
                info("selectDraweritem: favName : $favName")
                Answers.getInstance().logCustom(CustomEvent("Selected Favorite"))

                toolbar.title = getString(R.string.nav_favs) + " / $favName"
            }

            doAsync {
                val generators : Map<GeneratorMetaDto, Generator> =
                        if (collGrp != null) {
                            debug("getting generators for: ${collGrp.collectionId} ${collGrp.groupId.orEmpty()}")
                            AdventuresmithCore.getGeneratorsByGroup(
                                    getCurrentLocale(resources),
                                    collGrp.collectionId,
                                    collGrp.groupId
                            )
                        } else if (favName != null) {
                            val favs = getFavorites(favName)
                            debug("favorites: $favs")
                            AdventuresmithCore.getGeneratorsByIds(
                                    getCurrentLocale(resources),
                                    favs
                                    )
                        } else {
                            mapOf()
                        }
                debug("Discovered generators: ${generators.keys}")

                uiThread {
                    synchronized(buttonAdapter) {
                        buttonAdapter.clear()
                        for (g in generators) {
                            buttonAdapter.add(
                                    GeneratorButton(
                                            g.value,
                                            sharedPreferences,
                                            getCurrentLocale(resources),
                                            g.key)
                            )
                        }
                        buttonAdapter.withSavedInstanceState(savedInstanceState)
                    }

                    appbar.visibility = View.VISIBLE
                    appbar.setExpanded(true, true)
                }
            }
        }
    }

    private val BUNDLE_CURRENT_DRAWER_ITEM = AdventuresmithActivity::class.java.name + ".currentDrawerItem"
    private val BUNDLE_RESULT_ITEMS = AdventuresmithActivity::class.java.name + ".resultItems"

    override fun onSaveInstanceState(outState: Bundle?) {

        synchronized(resultAdapter) {
            resultAdapter.saveInstanceState(outState)

            val results: ArrayList<ResultItem> = ArrayList()
            results.addAll(resultAdapter.adapterItems)
            outState!!.putSerializable(BUNDLE_RESULT_ITEMS, results)
        }
        drawerHeader!!.saveInstanceState(outState)
        drawer!!.saveInstanceState(outState)

        outState!!.putSerializable(BUNDLE_CURRENT_DRAWER_ITEM, currentDrawerItemId)

        super.onSaveInstanceState(outState)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        // NOTE: currentDrawerItem _may_ have saved null
        // the following will setup the button adapter
        selectDrawerItem(savedInstanceState!!.getSerializable(BUNDLE_CURRENT_DRAWER_ITEM) as Long?, savedInstanceState)

        // restore previous results
        val restoredResults: ArrayList<ResultItem> = savedInstanceState.getSerializable(BUNDLE_RESULT_ITEMS) as ArrayList<ResultItem>
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
        /*
        menu.findItem(R.id.action_share).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_share_variant)
                .color(Color.WHITE)
                .actionBar()
                */
        menu.findItem(R.id.search).icon = IconicsDrawable(this,
                CommunityMaterial.Icon.cmd_magnify)
                .color(Color.WHITE)
                .actionBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val searchView = menu.findItem(R.id.search).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {

                    synchronized(resultAdapter) {
                        resultAdapter.filter(newText)
                    }
                    currentFilter = newText
                    appbar.setExpanded(false,false)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    synchronized(resultAdapter) {
                        resultAdapter.filter(query)
                    }
                    currentFilter = query
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
            synchronized(resultAdapter) {
                resultAdapter.clear()
            }
            // expand buttons
            appbar.setExpanded(true, true)
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
    }
}
