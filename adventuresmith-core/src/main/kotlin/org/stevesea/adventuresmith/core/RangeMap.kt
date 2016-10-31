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

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.*
import com.fasterxml.jackson.databind.deser.std.*
import java.util.*

/**
 * this assumes the incoming data is a list of strings. each string needs to be parsed and applied
 * to the rangemap that'll be returned by the deserializer
 */
class RangeMapDeserializer : StdDeserializer<RangeMap>(RangeMap::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): RangeMap {
        if (p == null)
            throw JsonMappingException(p, "null parser received")
        val jsonNode : JsonNode = p.codec.readTree(p)

        // TODO : is there better way to handle these sorts of errors than throwing?
        //    seems like this could be source of subtle crashes (especially as locales are added)
        //    if we just throw all the time.
        if (!jsonNode.isArray) {
            throw JsonMappingException(p, "RangeMap data must be an array of strings")
        }

        val result = RangeMap()

        for (v in jsonNode) {
            if (!v.isTextual)
                continue

            val str = v.asText()

            val indComma = str.indexOf(",")
            val rangeStr = str.substring(0..(indComma-1))
            val valStr = str.substring((indComma+1)..(str.length-1))



        }

        return result
    }
}

@JsonDeserialize(using = RangeMapDeserializer::class)
class RangeMap(
        val delegate: TreeMap<Int, String> = TreeMap<Int, String>()
) : Map<Int, String> by delegate {

    val ranges: MutableSet<IntRange> = mutableSetOf()

    init {
        for (i in delegate.keys) {
            ranges.add(IntRange(i,i))
        }
    }

    fun with(newRange: IntRange, value: String) : RangeMap {
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

    fun with(k: Int, value: String) : RangeMap {
        return with(k..k, value)
    }

    fun pick(k: Int) : String {
        val v = delegate.get(k % delegate.size - 1)
        if (v == null)
            return delegate.lastEntry().value
        else
            return v
    }

    fun pick(dice: Dice) : String {
        return pick(dice.roll())
    }
}
