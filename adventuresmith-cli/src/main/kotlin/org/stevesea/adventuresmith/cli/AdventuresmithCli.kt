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

package org.stevesea.adventuresmith.cli

import com.beust.jcommander.Parameter
import mu.KLoggable

// http://jcommander.org/
// http://beust.com/weblog/2010/08/08/complex-line-command-syntaxes-with-jcommander/
class CommandExercise {
    var all: Boolean = true

}

object AdventuresmithCli : KLoggable {
    override val logger = logger()
    @JvmStatic fun main(args: Array<String>) {
        logger.info("Hello from logger!")
    }
}