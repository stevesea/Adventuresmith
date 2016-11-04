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

package org.stevesea.adventuresmith.core.fourth_page

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*
import org.stevesea.adventuresmith.core.maze_rats.MrConstants
import java.util.*

val fpModule = Kodein.Module {
    FpConstants.generators.forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }

    bind<List<String>>(FpConstants.GROUP) with singleton {
        FpConstants.generators
    }
}

object FpConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val DUNGEON = "${GROUP}/dungeon"
    val CITY = "${GROUP}/city"
    val ARTIFACT = "${GROUP}/artifact"
    val MONSTER = "${GROUP}/monster"


    val generators = listOf(
            MONSTER,
            DUNGEON,
            CITY,
            ARTIFACT
    )
}




