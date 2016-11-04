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

package org.stevesea.adventuresmith.core.maze_rats

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*
import java.util.*

val mrModule = Kodein.Module {

    MrConstants.generators.forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }

    bind<List<String>>(MrConstants.GROUP) with singleton {
        MrConstants.generators
    }
}

object MrConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val AFFLICTIONS = "${GROUP}/afflictions"
    val POTION_EFFECTS = "${GROUP}/potion_effects"
    val ITEM = "${GROUP}/items"
    val MAGIC = "${GROUP}/magic"
    val MONSTER = "${GROUP}/monsters"
    val CHAR = "${GROUP}/characters"

    val generators = listOf(
            MONSTER,
            ITEM,
            CHAR,
            MAGIC,
            POTION_EFFECTS,
            AFFLICTIONS
    )
}
