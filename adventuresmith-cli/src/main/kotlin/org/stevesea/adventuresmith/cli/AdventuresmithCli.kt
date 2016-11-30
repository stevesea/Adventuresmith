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

import com.beust.jcommander.*
import mu.*
import java.util.*



// http://jcommander.org/
// http://beust.com/weblog/2010/08/08/complex-line-command-syntaxes-with-jcommander/
class CommandExercise {
    @field:Parameter(names = arrayOf("--help","-h"), help = true, description = "asdf")
    var help: Boolean = false

    @field:Parameter(names = arrayOf("--include", "-i"),
            description = "include filter for generators")
    var include: String = ""

    @field:Parameter(names = arrayOf("--iterations", "-n"),
            description = "how many cycles to run")
    var iterations: Int = 25

    @field:Parameter(names = arrayOf("--locale", "-l"),
            description = "which locales to run")
    var locales : List<String> =
            listOf(Locale.FRANCE.toString(), Locale.US.toString())

}

class CommandMain {
    @field:Parameter(names = arrayOf("--help","-h"), help = true, description = "asdf")
    var help: Boolean = false
}

object AdventuresmithCli : KLoggable {
    override val logger = logger()
    @JvmStatic fun main(args: Array<String>) {
        logger.info("Hello from logger!")
        val cm = CommandMain()
        val jc = JCommander(cm)
        jc.setProgramName("adventuresmith-cli")

        val coreExerciser = CommandExercise()
        jc.addCommand("core-exerciser", coreExerciser)

        jc.parse(*args)

        if (cm.help) {
            jc.usage()
            return
        }
    }
}
