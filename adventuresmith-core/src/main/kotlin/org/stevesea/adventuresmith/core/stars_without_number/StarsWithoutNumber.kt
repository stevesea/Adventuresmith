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

    val ADV_SEED = "${GROUP}/adventure_seed"
    val ALIEN = "${GROUP}/alien"
    val ANIMAL = "${GROUP}/animal"
    val ARCHITECTURE = "${GROUP}/architecture"
    val CORPORATION = "${GROUP}/corporation"
    val HERESY = "${GROUP}/heresy"
    val NAME_Arabic = "${GROUP}/names_arabic"
    val NAME_Chinese = "${GROUP}/names_chinese"
    val NAME_English = "${GROUP}/names_english"
    val NAME_Indian = "${GROUP}/names_indian"
    val NAME_Japanese = "${GROUP}/names_japanese"
    val NAME_Nigerian = "${GROUP}/names_nigerian"
    val NAME_Russian = "${GROUP}/names_russian"
    val NAME_Spanish = "${GROUP}/names_spanish"
    val NPC = "${GROUP}/npc"
    val POLITICAL_PARTY = "${GROUP}/political_party"
    val RELIGION = "${GROUP}/religion"
    val ROOM_DRESSING = "${GROUP}/room_dressing"

    // world

    val generators = listOf(
            ADV_SEED,
            ALIEN,
            ANIMAL,
            ARCHITECTURE,
            CORPORATION,
            HERESY,
            NAME_Arabic,
            NAME_Chinese,
            NAME_English,
            NAME_Indian,
            NAME_Japanese,
            NAME_Nigerian,
            NAME_Russian,
            NAME_Spanish,
            NPC,
            POLITICAL_PARTY,
            RELIGION,
            ROOM_DRESSING
    )
}
