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


package org.stevesea.rpg_boy2000.data

import groovy.transform.CompileStatic

// TODO: use http://groovy-lang.org/templating.html instead?
@CompileStatic
public abstract class AbstractGenerator {

    protected final Shuffler shuffler;

    AbstractGenerator() {
        this(new Shuffler(new Random()))
    }
    AbstractGenerator(Shuffler shuffler) {
        this.shuffler = shuffler
    }

    def pick(List<?> items) {
        return shuffler.pick(items)
    }

    List<?> pick(List<?> items, int num) {
        return shuffler.pick(items, num)
    }

    int rollDice(int numDice, int nSides) {
        return shuffler.rollDice(numDice, nSides)
    }

    abstract String generate()

    String[] generate(int num) {
        def strings = []
        num.times {
            strings << generate()
        }
        return strings as String[]
    }
}
