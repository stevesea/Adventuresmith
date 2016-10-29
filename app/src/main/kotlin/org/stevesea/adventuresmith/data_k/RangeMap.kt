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

package org.stevesea.adventuresmith.data_k

import java.security.SecureRandom
import java.util.*

class RangeMap<T>(
        val delegate: TreeMap<Int, T> = TreeMap<Int, T>()
    ) : Map<Int, T> by delegate {

    val ranges: MutableSet<IntRange> = mutableSetOf()
    init {
        for (i in delegate.keys) {
           ranges.add(IntRange(i,i))
        }
    }

    fun with(newRange: IntRange, value: T) : RangeMap<T> {
        for (range in ranges) {
            if (!range.intersect(newRange).isEmpty()) {
                throw IllegalArgumentException("Invalid range -- already included")
            }
        }
        ranges.add(newRange)
        for (k in newRange) {
            delegate.put(k, value)
        }
        return  this
    }

    fun with(k: Int, value: T) : RangeMap<T> {
        return with(k..k, value)
    }

    fun pick(k: Int) : T {
        val v = delegate.get(k % delegate.size - 1)
        if (v == null)
            return delegate.lastEntry().value
        else
            return v
    }

    fun pick(dice: Dice) : T {
        return pick(dice.roll())
    }

    fun pick(diceStr: String, random: Random = SecureRandom()) : T {
        return pick(Dice.roll(diceStr, random))
    }
}
