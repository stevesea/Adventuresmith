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

import android.support.annotation.StringRes
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class DrawerItemData {
    @DrawerItemId
    int id
    @StringRes
    int nameResourceId
    @StringRes
    Integer descResourceId  = null;
    IIcon icon
    @DrawerItemId
    List<Integer> children = null
    int level = 1
    boolean selectable = true
    boolean expanded = false

    boolean drawDividerBefore = false
    boolean isSectionHeader = false

    IDrawerItem createDrawerItem() {
        if (children != null) {
            ExpandableDrawerItem di = new ExpandableDrawerItem()
                .withName(nameResourceId)
                .withIcon(icon)
                .withIdentifier(id)
                .withSelectable(selectable)
                .withIsExpanded(expanded)
            for (@DrawerItemId int child: children) {
                di.withSubItems(getDrawerItemData(child).createDrawerItem())
            }
            if (descResourceId != null) {
                di.withDescription(descResourceId)
            }
            return di

        } else if (level != 1) {
            IDrawerItem di = new SecondaryDrawerItem()
                .withName(nameResourceId)
                .withIcon(icon)
                .withIdentifier(id)
                .withLevel(level)
                .withSelectable(selectable)
            if (descResourceId != null) {
                di.withDescription(descResourceId)
            }
            return di
        } else {
            IDrawerItem di = new PrimaryDrawerItem()
                    .withName(nameResourceId)
                    .withIcon(icon)
                    .withIdentifier(id)
                    .withSelectable(selectable);
            if (descResourceId != null) {
                di.withDescription(descResourceId)
            }
            return di
        }
    }
    static List<IDrawerItem> createDrawerItems() {
        List<IDrawerItem> result = new ArrayList<>()
        // items will be in ascending order
        for (DrawerItemData diData : items.values()) {
            if (diData.level != 1) {
                continue;
            }
            if (diData.drawDividerBefore) {
                result.add(new DividerDrawerItem())
            }
            if (diData.isSectionHeader) {
                result.add(new SectionDrawerItem()
                    .withName(diData.getNameResourceId()))
            } else {
                result.add(diData.createDrawerItem())
            }
        }
        return result
    }

    static TreeMap<Integer, DrawerItemData> items = new TreeMap<>()
    static {
        items.put(DrawerItemId.PwHeader,
                new DrawerItemData(
                        id: DrawerItemId.PwHeader,
                        nameResourceId: R.string.perilous_wilds,
                        icon: CommunityMaterial.Icon.cmd_folder_multiple_image,
                        selectable: false,
                        children: [
                                DrawerItemId.PwDiscoveriesAndDangers,
                                DrawerItemId.PwNPC,
                                DrawerItemId.PwCreature,
                                DrawerItemId.PwTreasure,
                                DrawerItemId.PwNames,
                        ]
                ));
        items.put(DrawerItemId.PwDiscoveriesAndDangers,
                new DrawerItemData(
                        id: DrawerItemId.PwDiscoveriesAndDangers,
                        nameResourceId: R.string.perilous_wilds_explore,
                        icon: CommunityMaterial.Icon.cmd_image_filter_hdr,
                        level: 2,
                ));
        items.put(DrawerItemId.PwNPC,
                new DrawerItemData(
                        id: DrawerItemId.PwNPC,
                        nameResourceId: R.string.perilous_wilds_npc,
                        icon: CommunityMaterial.Icon.cmd_account_multiple,
                        level: 2,));
        items.put(DrawerItemId.PwCreature,
                new DrawerItemData(
                        id: DrawerItemId.PwCreature,
                        nameResourceId: R.string.perilous_wilds_creature,
                        icon: Ionicons.Icon.ion_ios_paw,
                        level: 2,));
        items.put(DrawerItemId.PwTreasure,
                new DrawerItemData(
                        id: DrawerItemId.PwTreasure,
                        nameResourceId: R.string.perilous_wilds_treasure,
                        icon: CommunityMaterial.Icon.cmd_white_balance_irradescent,
                        level: 2,));
        items.put(DrawerItemId.PwNames,
                new DrawerItemData(
                        id: DrawerItemId.PwNames,
                        nameResourceId: R.string.perilous_wilds_names,
                        icon: CommunityMaterial.Icon.cmd_book_open_page_variant,
                        level: 2,));

        items.put(DrawerItemId.SwnHeader,
                new DrawerItemData(
                        id: DrawerItemId.SwnHeader,
                        nameResourceId: R.string.swn,
                        selectable: false,
                        icon: CommunityMaterial.Icon.cmd_rocket,
                        drawDividerBefore: true,
                        children: [
                                DrawerItemId.SwnGen,
                                DrawerItemId.SwnQuick,
                                DrawerItemId.SwnNames,
                        ]
                ));
        items.put(DrawerItemId.SwnGen,
                new DrawerItemData(
                        id: DrawerItemId.SwnGen,
                        nameResourceId: R.string.swn_gen,
                        icon: Ionicons.Icon.ion_planet,
                        level: 2,));
        items.put(DrawerItemId.SwnQuick,
                new DrawerItemData(
                        id: DrawerItemId.SwnQuick,
                        nameResourceId: R.string.swn_quickgen,
                        icon: Ionicons.Icon.ion_person_stalker,
                        level: 2,));
        items.put(DrawerItemId.SwnNames,
                new DrawerItemData(
                        id: DrawerItemId.SwnNames,
                        nameResourceId: R.string.swn_names,
                        icon: Ionicons.Icon.ion_edit,
                        level: 2,));

        /*
        items.put(DrawerItemId.Other,
                new DrawerItemData(
                        id: DrawerItemId.Other,
                        isSectionHeader: true,
                        nameResourceId: R.string.nav_other
                ));
                */
        items.put(DrawerItemId.MazeRats,
                new DrawerItemData(
                        id: DrawerItemId.MazeRats,
                        drawDividerBefore: true,
                        nameResourceId: R.string.maze_rats,
                        icon: CommunityMaterial.Icon.cmd_castle,));
        items.put(DrawerItemId.FourthPage,
                new DrawerItemData(
                        id: DrawerItemId.FourthPage,
                        nameResourceId: R.string.fourth_page,
                        icon: CommunityMaterial.Icon.cmd_numeric_4_box_outline,));
        items.put(DrawerItemId.FotF,
                new DrawerItemData(
                        id: DrawerItemId.FotF,
                        nameResourceId: R.string.freebooters_on_the_frontier,
                        icon: Ionicons.Icon.ion_map,));

        items.put(DrawerItemId.DiceRoller,
                new DrawerItemData(
                        id: DrawerItemId.DiceRoller,
                        nameResourceId: R.string.dice_roller,
                        icon: CommunityMaterial.Icon.cmd_dice_6,));

        items.put(DrawerItemId.Attribution,
                new DrawerItemData(
                        id: DrawerItemId.Attribution,
                        nameResourceId: R.string.nav_thanks,
                        drawDividerBefore: true,
                        icon: CommunityMaterial.Icon.cmd_information_outline));
    }

    static public DrawerItemData getDrawerItemData(@DrawerItemId int id) {
        items.get(id);
    }

}
