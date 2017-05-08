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

package org.stevesea.adventuresmith.core

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import mu.KLoggable
import java.util.TreeMap

/**
 * a RangeMap is a map meant to hold entries like
 *    1..2, optionA
 *    3..4, optionB
 *    5..9, optionC
 *    10, optionD
 *
 * This implementation assumes (and enforces) the following
 *  - there are no holes in the range
 *  - there are no overlaps in the range
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property="@id")
@JsonDeserialize(using = RangeMapDeserializer::class)
class RangeMap(
        val delegate: TreeMap<Int, String> = TreeMap<Int, String>()
) : Map<Int, String> by delegate, KLoggable {

    override val logger = logger()

    var maxKey : Int = -1
    val ranges: MutableSet<IntRange> = mutableSetOf()

    init {
        for (i in delegate.keys) {
            ranges.add(IntRange(i, i))
        }
        if (delegate.keys.size > 0) {
            maxKey = delegate.lastKey()
        }
    }

    fun with(newRange: IntRange, value: String) : RangeMap {
        for (range in ranges) {
            if (!range.intersect(newRange).isEmpty()) {
                throw IllegalArgumentException("Invalid range $newRange already included in RangeMap")
            }
        }
        ranges.add(newRange)

        delegate.put(newRange.start, value)
        maxKey = Math.max(newRange.endInclusive, maxKey)

        return this
    }

    fun with(k: Int, value: String) : RangeMap {
        return with(k..k, value)
    }

    fun select(k: Int) : String {
        try {
            if (k < delegate.firstKey()) {
                return delegate.firstEntry().value
            } else if (k > delegate.lastKey()) {
                return delegate.lastEntry().value
            } else {
                return delegate.floorEntry(k).value
            }
        } catch (e: Exception) {
            logger.warn("Unable to lookup entry $k. Keyrange: ${keyRange()}")
            throw e
        }
    }

    fun keyRange() : IntRange {
        return IntRange(delegate.firstKey(), maxKey)
    }

    override fun toString(): String {
        return delegate.toString()
    }

    fun validateNoHoles() {
        val rangeSet : MutableSet<Int> = mutableSetOf()
        keyRange().toCollection(rangeSet)

        for (r in ranges) {
            rangeSet.removeAll(r.toSet())
        }
        if (rangeSet.size != 0) {
            throw IllegalArgumentException("holes in range set. missing elements: $rangeSet")
        }
    }
}
