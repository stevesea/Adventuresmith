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

import com.github.salomonbrys.kodein.*
import java.util.*

class Shuffler(override val kodein: Kodein): KodeinAware {
    val random: Random = instance()
    val diceFactory : (String) -> Dice = factory()

    fun <T> pick(items: Collection<T>?) : T {
        // use modulo because the randomizer might be a mock that's been setup to do something dumb
        // and returns greater than the # of items in list
        return items!!.elementAt(random.nextInt(items.size) % items.size)
    }
    fun pick(rmap: RangeMap?) : String {
        val itemsInds = rmap!!.keyRange().toList()
        val sel = pick(itemsInds)
        return rmap.select(sel)
    }
    fun pickN(rmap: RangeMap?, num: Int) : List<String> {
        val itemsInds = rmap!!.keyRange().toList()
        val selN = pickN(itemsInds, num)
        return selN.map { rmap.select(it)}
    }

    fun <T> pick(dice: Dice, items: Collection<T>?): T {
        // use mod to ensure our index is within the acceptable range for the collection
        // dice are 1-based, list indexes are 0-based so subtract 1
        return items!!.elementAt(dice.roll() % items.size - 1)
    }

    fun <T> pick(diceStr: String, items: Collection<T>?) : T = pick(dice(diceStr), items)

    fun dice(diceStr: String) : Dice = diceFactory.invoke(diceStr)

    fun <T> pickN(items: Collection<T>?, num: Int) : Collection<T> {
        val localItems = items!!.toMutableList()
        Collections.shuffle(localItems, random)
        return localItems.take(num)
    }

    fun pickPairFromMapofLists(m: Map<String,Collection<String>>?) : Pair<String, String> {
        val k = pick(m!!.keys)
        return Pair(k, pick(m.getOrElse(k) {listOf("not found")}))
    }
    fun pickPairFromMapofRangeMaps(m: Map<String,RangeMap>?) : Pair<String, String> {
        val k = pick(m!!.keys)
        return Pair(k, pick(m[k]))
    }
}
