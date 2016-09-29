/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.stevesea.rpg_boy2000.data;

import org.stevesea.rpg_boy2000.R;

import groovy.transform.CompileStatic;

@CompileStatic
public enum Dataset {
    MazeRats(R.string.maze_rats),
    ThePerilousWilds(R.string.perilous_wilds),
    FreebootersOnTheFrontier(R.string.freebooters_on_the_frontier);

    int stringResourceId
    Dataset(int stringResourceId) {
        this.stringResourceId = stringResourceId
    }
}
