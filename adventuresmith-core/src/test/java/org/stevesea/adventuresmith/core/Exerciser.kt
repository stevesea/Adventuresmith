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
import org.stevesea.adventuresmith.core.perilous_wilds.*
import java.security.*
import java.util.*


// this test doesn't actually do any verification, but it does run all the generators a bunch of
// times in a bunch of locales to try and smoke out any NPEs or other exceptions
class Exerciser {

    @Test
    fun exerciser() {
        val kodein = getKodein(SecureRandom())

        // output specific ones to console
        val enablePrinting = setOf(PwConstants.DISCOVERY)

        val gennames = kodein.instance<Set<String>>(AdventureSmithConstants.GENERATORS)
        for (g in gennames) {
            val generator_instance = kodein.instance<Generator>(g)

            // TODO: every time add a translation, add its locale here.
            for (locale in listOf(Locale.FRANCE, Locale.US)) {
                generator_instance.getMetadata(locale)
                for (i in 1..25) {
                    if (enablePrinting.contains(g))
                        println(generator_instance.generate(locale))
                    else
                        generator_instance.generate(locale)
                }
            }
        }
    }
}
