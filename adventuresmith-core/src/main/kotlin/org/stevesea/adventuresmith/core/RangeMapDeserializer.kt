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

package org.stevesea.adventuresmith.core

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.google.common.base.Throwables

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