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
    // TODO: complete perilous wilds generators
    // TODO: minor arcana generator (get permission)
    // TODO: into the odd (get permission)
    // TODO: others?
    MazeRats(R.string.maze_rats),
    ThePerilousWilds(R.string.perilous_wilds),
    FreebootersOnTheFrontier(R.string.freebooters_on_the_frontier);

    int stringResourceId
    Dataset(int stringResourceId) {
        this.stringResourceId = stringResourceId
    }
}
