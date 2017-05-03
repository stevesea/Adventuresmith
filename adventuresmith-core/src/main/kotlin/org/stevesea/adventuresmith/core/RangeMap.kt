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
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.google.common.base.Throwables
import mu.KLoggable
import java.util.TreeMap

/**
 * this assumes the incoming data is a list of strings. each string needs to be parsed and applied
 * to the rangemap that'll be returned by the deserializer
 *
 * the RangeMap deserializer can deserialize YaML that looks like:
 *  rmap:
 *      - 1..2, value1
 *      - 3..4, value2
 *
 *  rmap:
 *      - value1
 *      - value1
 *      - value2
 *      - value2
 *
 *  (the above two are synonymous)
 */
class RangeMapDeserializer : StdDeserializer<RangeMap>(RangeMap::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): RangeMap {
        if (p == null)
            throw JsonMappingException(p, "null parser received")
        val jsonNode : JsonNode = p.codec.readTree(p)

        if (!jsonNode.isArray) {
            throw JsonMappingException(p, "RangeMap data must be an array of strings")
        }

        val result = RangeMap()

        var itemNum = 1
        for (v in jsonNode) {
            if (!v.isTextual)
                continue

            val str = v.asText()
            val words = str.split(",", limit = 2)

            if (words.size == 2) {
                val rangeStr = words[0]
                var itemRange : IntRange?
                try {
                    itemRange = strToIntRange(rangeStr)
                    // able to parse range, can it be added to the map? (if not, probably a colliding range, throw exception)
                    try {
                        result.with(itemRange, words[1].trim())
                    } catch (e: IllegalArgumentException) {
                        throw JsonMappingException(p, "${e.message} - '$str'")
                    }
                    itemNum = itemRange.endInclusive + 1
                } catch (e: IllegalArgumentException) {
                    //some problem parsing the range. just turn the whole thing into a single item
                    // this handles case of there being a comma in string, but isn't actually a <range>, <val>
                    result.with(itemNum, str)
                    itemNum++
                } catch (e: Exception) {
                    Throwables.throwIfInstanceOf(e, JsonMappingException::class.java)
                    throw JsonMappingException(p, "${e.message} - '$str'")
                }
            } else {
                result.with(itemNum, str)
                itemNum++
            }
        }
        result.validateNoHoles()
        return result
    }

    fun strToIntRange(rangeStr: String) : IntRange {
        try {
            var words = rangeStr.split("..", limit = 2)
            if (words.size != 2) {
                words = rangeStr.split("-", limit = 2)
                if (words.size != 2) {
                    // if no '..' or '-', must be a single int
                    val i = rangeStr.trim().toInt()
                    return IntRange(i, i)
                }
            }
            val start = words[0].trim().toInt()
            val end = words[1].trim().toInt()
            return IntRange(start, end)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("bad format for range : '%s'. Must be <Start>..<End> or <Int>. %s".format(rangeStr, e.message))
        }
    }
}

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
