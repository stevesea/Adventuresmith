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


package org.stevesea.rpg_boy2000.data.freebooters_on_the_frontier

import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.AbstractGenerator
import org.stevesea.rpg_boy2000.data.RpgBoyData
import org.stevesea.rpg_boy2000.data.Shuffler

import javax.inject.Inject

@CompileStatic
class FotFChars extends AbstractGenerator {
    public String getName() {
        return "Characters"
    }
    public String getDataset() {
        return RpgBoyData.FREEBOOTERS
    }
    static final List<String> virtues = """\
""".readLines()

    static final List<String> vices = """\
""".readLines()

    @Inject
    FotFChars(Shuffler shuffler) {
        super(shuffler);
    }

    String generate() {
        return "${ -> pick(virtues)} <> ${ -> pick(vices)}"
    }

}
