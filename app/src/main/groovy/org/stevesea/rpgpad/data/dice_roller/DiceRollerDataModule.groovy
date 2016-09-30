/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.stevesea.rpgpad.data.dice_roller

import dagger.Module
import groovy.transform.CompileStatic

@CompileStatic
@Module(
        injects = [
                DiceRoller1d6.class,
                DiceRoller1d8.class,
                DiceRoller1d10.class,
                DiceRoller1d12.class,
                DiceRoller1d20.class,
                DiceRoller1d20Advantage.class,
                DiceRoller1d20Disadvantage.class,
                DiceRoller1d30.class,
                DiceRoller1d100.class,
                DiceRoller2d6.class,
                DiceRoller3d6.class,
                DiceRoller4d4.class,
        ],
        library = true,
        complete = false
)
class DiceRollerDataModule {
}
