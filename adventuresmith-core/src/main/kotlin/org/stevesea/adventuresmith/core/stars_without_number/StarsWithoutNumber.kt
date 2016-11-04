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

package org.stevesea.adventuresmith.core.stars_without_number

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*

val swnModule = Kodein.Module {

    SwnConstants.generators.forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }

    bind<List<String>>(SwnConstants.GROUP) with singleton {
        SwnConstants.generators
    }
}

object SwnConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val ROOM_DRESSING = "${GROUP}/room_dressing"
    val RELIGION = "${GROUP}/religion"
    val ADV_SEED = "${GROUP}/adventure_seed"
    val ARCHITECTURE = "${GROUP}/architecture"

    val generators = listOf(
            ROOM_DRESSING,
            RELIGION,
            ADV_SEED,
            ARCHITECTURE
    )
}
