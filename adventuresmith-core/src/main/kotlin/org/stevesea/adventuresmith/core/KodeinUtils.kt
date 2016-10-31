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
import org.stevesea.adventuresmith.core.fotf.*
import org.stevesea.adventuresmith.core.fp.*
import org.stevesea.adventuresmith.core.maze_rats.*
import java.security.*
import java.util.*


object AdventureSmithConstants {
    val GENERATORS = "generators"
}

val randModule = Kodein.Module {
    bind<Random>() with singleton { SecureRandom() }
    bind<Shuffler>() with singleton { Shuffler(kodein) }

    bind<Dice>() with factory { diceStr: String ->
        val dd = diceStrToDef(diceStr)
        Dice(dd.nSides, dd.nDice, dd.modifier, kodein)
    }
}

val generatorModules = Kodein.Module {
    import(fotfModule)
    import(fpModule)
    import(mrModule)

    bind<List<String>>(AdventureSmithConstants.GENERATORS) with provider {
        val res : MutableList<String> = mutableListOf()
        res.addAll(instance(FotfConstants.GROUP))
        res.addAll(instance(MrConstants.GROUP))
        res.addAll(instance(FpConstants.GROUP))
        res
    }
}
