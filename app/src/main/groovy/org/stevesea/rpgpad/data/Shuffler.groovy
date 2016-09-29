/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.stevesea.rpgpad.data

import groovy.transform.CompileStatic

import javax.inject.Inject

@CompileStatic
class Shuffler {
    private final Random random;

    @Inject
    Shuffler(Random random) {
        this.random = random
    }

    def pick(List<?> items) {
        return items.get(random.nextInt(items.size()))
    }

    List<?> pick(List<?> items, int num) {
        def local = items.collect()
        Collections.shuffle(local, random)
        return local.take(num)
    }

    int rollDice(int numDice, int nSides) {
        int sum = 0
        numDice.times {
            sum += random.nextInt(nSides) + 1
        }
        return sum
    }
}
