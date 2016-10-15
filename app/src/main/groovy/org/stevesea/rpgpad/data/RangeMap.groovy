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

package org.stevesea.rpgpad.data

import groovy.transform.CompileStatic;

// TODO: this can be cleaned up. we're creating copies of each entry
@CompileStatic
class RangeMap extends TreeMap<Integer, Object> {
    RangeMap with(IntRange range, Object obj) {
        range.each{ i -> put(i, obj)}
        return this
    }
    RangeMap with(Integer i, Object obj) {
        put(i, obj)
        return this
    }

    Object get(Integer key) {
        return super.get(Math.min(key, lastKey()))
    }

    Object pick(Dice dice) {
        return get(dice.roll())
    }
}
