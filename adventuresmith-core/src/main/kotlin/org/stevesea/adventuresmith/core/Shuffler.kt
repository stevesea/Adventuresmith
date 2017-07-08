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
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.core

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import mu.KLoggable
import org.stevesea.adventuresmith.core.dice_roller.DiceParser
import java.util.Collections
import java.util.Random

class Shuffler(override val kodein: Kodein) : KodeinAware, KLoggable {
    override val logger = logger()

    val random: Random = instance()
    val diceParser : DiceParser = instance()

    fun <T> pick(items: Collection<T>) : T {
        // use modulo because the randomizer might be a mock that's been setup to do something dumb
        // and returns greater than the # of items in list
        return items.elementAt(random.nextInt(items.size) % items.size)
    }
    fun pick(rmap: RangeMap) : String {
        val itemsInds = rmap.keyRange().toList()
        val sel = pick(itemsInds)
        return rmap.select(sel)
    }
    fun pickN(rmap: RangeMap, num: Int) : List<String> {
        if (num <= 0)
            return listOf()
        val itemsInds = rmap.keyRange().toList()
        val selN = pickN(itemsInds, num)
        return selN.map { rmap.select(it) }
    }

    fun <T> pickN(items: Collection<T>, num: Int) : Collection<T> {
        if (num <= 0)
            return listOf()
        val localItems = items.toMutableList()
        Collections.shuffle(localItems, random)
        return localItems.take(num)
    }

    fun pickN(thing: Any, num: Int) : Collection<Any?> {
        if (thing is RangeMap) {
            return pickN(thing, num)
        } else if (thing is Collection<*>) {
            return pickN(thing, num)
        } else {
            throw IllegalArgumentException("don't know how to select thing of type " + thing.javaClass)
        }
    }

    fun pickD(diceStr: String, thing: Any) : String {
        if (thing is RangeMap) {
            return pickD(diceStr, thing)
        } else if (thing is Collection<*>) {
            return pickD(diceStr, thing as Collection<String>)
        } else {
            throw IllegalArgumentException("don't know how to select thing of type " + thing.javaClass)
        }
    }

    fun pickD(diceStr: String, items: RangeMap) : String {
        try {
            return items.select(roll(diceStr))
        } catch (e: Exception) {
            logger.warn("Error looking up $diceStr from rmap", e.message)
            throw e
        }
    }

    fun <T> pickD(diceStr: String, items: Collection<T>): T {
        // use mod to ensure our index is within the acceptable range for the collection
        // dice are 1-based, list indexes are 0-based so subtract 1
        return items.elementAt((roll(diceStr) - 1) % items.size)
    }

    fun roll(diceStr: String) : Int = diceParser.roll(diceStr)
    fun rollN(diceStr: String, n: Int) : List<Int> = diceParser.rollN(diceStr, n)
}
