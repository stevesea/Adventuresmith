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

import com.github.salomonbrys.kodein.*
import org.junit.*
import java.security.*
import java.util.*

// TODO: every time add a translation, add its locale here.

// this test doesn't actually do any verification, but it does run all the generators a bunch of
// times in a bunch of locales to try and smoke out any NPEs or other exceptions
class Exerciser {

    @Test
    fun exerciser() {
        val kodein = getKodein(SecureRandom())

        val gennames = kodein.instance<Set<String>>(AdventureSmithConstants.GENERATORS)
        for (g in gennames) {
            for (locale in listOf(Locale.FRANCE, Locale.US)) {
                for (i in 1..50) {
                    kodein.instance<Generator>(g).generate(locale)
                }
            }
        }
    }
}
