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
 */
package org.stevesea.rpgpad

import groovy.transform.CompileStatic

@CompileStatic
public enum Dataset {
    // TODO: get rid of enums, is bad android practice http://guides.codepath.com/android/Replacing-Enums-with-Enumerated-Annotations
    None(R.string.nav_thanks, R.id.nav_thanks),
    MazeRats(R.string.maze_rats, R.id.nav_mr),
    ThePerilousWilds(R.string.perilous_wilds_explore, R.id.nav_pw),
    ThePerilousWildsNPC(R.string.perilous_wilds_npc, R.id.nav_pw_npc),
    ThePerilousWildsCreature(R.string.perilous_wilds_creature, R.id.nav_pw_creature),
    ThePerilousWildsTreasure(R.string.perilous_wilds_treasure, R.id.nav_pw_treasure),
    ThePerilousWildsNames(R.string.perilous_wilds_names, R.id.nav_pw_names),
    FreebootersOnTheFrontier(R.string.freebooters_on_the_frontier, R.id.nav_fotf),
    TheFourthPage(R.string.fourth_page, R.id.nav_fourth_page),
    DiceRoller(R.string.dice_roller, R.id.nav_dice)
    ;

    int stringResourceId
    int menuNavId

    Dataset(int stringResourceId, int menuNavId) {
        this.stringResourceId = stringResourceId
        this.menuNavId = menuNavId
    }
    static Dataset lookupDatasetForNavItem(int navItem) {
        return values().grep{((Dataset)it).menuNavId == navItem}[0]
    }
}
