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

package org.stevesea.adventuresmith.data_k

import java.util.*

class Shuffler<T> (val random: java.util.Random = java.security.SecureRandom()) {
    fun pick(items: List<T>) : T {
        return items.elementAt(random.nextInt(items.size))
    }

    fun pick(dice: Dice, items: List<T>): T {
        // use mod to ensure our index is within the acceptable range for the collection
        // dice are 1-based, list indexes are 0-based so subtract 1
        return items.elementAt(dice.roll() % items.size - 1)
    }

    fun pick(diceStr: String, items: List<T>) : T = pick(dice(diceStr), items)

    // create a dice object w/ the same Random instance as the shuffler
    fun dice(diceStr: String) : Dice = Dice.create(diceStr, random)

    fun pickN(items: List<T>, num: Int) : List<T> {
        val localItems = items.toMutableList()
        Collections.shuffle(localItems)
        return localItems.take(num)
    }

}
