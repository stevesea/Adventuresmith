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

package org.stevesea.adventuresmith.core.perilous_wilds

import com.github.salomonbrys.kodein.*
import org.stevesea.adventuresmith.core.*

val pwModule = Kodein.Module {

    PwConstants.generators.forEach {
        bind<Generator>(it) with provider {
            DataDrivenGenerator(it, kodein)
        }
    }

    bind<List<String>>(PwConstants.GROUP) with singleton {
        PwConstants.generators
    }
}

object PwConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    // TODO: read this list of generators from resource?

    val DETAILS = "${GROUP}/details"

    val PLACE = "${GROUP}/place_names"
    val REGION = "${GROUP}/region_names"

    val NAMES_Arpad = "${GROUP}/names_arpad"
    val NAMES_Oloru = "${GROUP}/names_oloru"
    val NAMES_Tamanarugan = "${GROUP}/names_tamanarugan"
    val NAMES_Valkoina = "${GROUP}/names_valkoina"

    val TREASURE_ITEM = "${GROUP}/treasure_item"
    val TREASURE_UNGUARDED = "${GROUP}/treasure_unguarded"

    // creature -- beast, human, humanoid, monster
    // danger
    // discovery
    // dungeon
    // dungeon disc/danger
    // follower
    // npc -- rural, urban, wilderness
    // steading
    // treasure
    // treasure items
    // treasure guarded (3)

    val generators = listOf(
            PLACE,
            REGION,
            NAMES_Arpad,
            NAMES_Oloru,
            NAMES_Tamanarugan,
            NAMES_Valkoina,
            TREASURE_ITEM,
            TREASURE_UNGUARDED
    )
}
