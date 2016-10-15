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

// this isn't space-efficient, but it has a nice property of having a proportional
// amount of values
@CompileStatic
class RangeMap extends TreeMap<Integer, Object> {
    Set<IntRange> ranges = new HashSet<>();
    RangeMap with(IntRange range, Object obj) {
        for (IntRange ir: ranges) {
            if (ir.contains(range.first()) || ir.contains(range.last())) {
                throw new IllegalArgumentException("map already includes the given range!")
            }
        }
        ranges.add(range)
        range.each{ i -> put(i, obj)}
        return this
    }
    RangeMap with(Integer i, Object obj) {
        for (IntRange ir: ranges) {
            if (ir.contains(i)) {
                throw new IllegalArgumentException("map already includes the given range!")
            }
        }
        ranges.add(i..i)
        put(i, obj)
        return this
    }

    Object get(Integer key) {
        return super.get(Math.min(key, lastKey()))
    }
}
