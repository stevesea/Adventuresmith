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

import mu.*
import net.sourceforge.argparse4j.*
import net.sourceforge.argparse4j.annotation.*
import net.sourceforge.argparse4j.impl.*
import net.sourceforge.argparse4j.inf.*
import org.stevesea.adventuresmith.core.*
import java.io.*
import java.util.*



class LocaleArgType : ArgumentType<Locale> {
    override fun convert(parser: ArgumentParser?, arg: Argument?, value: String?): Locale {
        try {
            val split = value!!.split('-','_')
            when (split.size ) {
                1 -> return Locale(split[0])
                2 -> return Locale(split[0].toLowerCase(), split[1])
                else -> return Locale(split[0].toLowerCase(), split[1], split[3])
            }
        } catch (e: Exception) {
            throw ArgumentParserException(e, parser)
        }
    }
}
class Options {
    @Arg
    var iterations: Int = 1

    @Arg
    var subcmd : String = ""

    @Arg
    var locale : Locale = Locale.US

    @Arg
    var fauxRandom: Int = -1

    @Arg
    var out : File? = null

    override fun toString(): String {
        return "Options(fauxRandom=$fauxRandom, iterations=$iterations, subcmd='$subcmd', locale=$locale, out=$out)"
    }
}

object AdventuresmithCli : KLoggable {
    val SUBCMD = "subcmd"
    val SUBCMD_CORE_EXERCISE = "core-exercise"
    val SUBCMD_CORE_LIST = "core-list"
    val SUBCMD_RUN = "run"

    override val logger = logger()
    @JvmStatic fun main(args: Array<String>) {
        val parser = ArgumentParsers
                .newArgumentParser("adventuresmith-cli")
                .description("A CLI interface to test Adventuresmith YaML files")
                .epilog("Go to https://stevesea.github.io/Adventuresmith/ for more information")
                .defaultHelp(true)
        val subparsers = parser.addSubparsers()
                .dest(SUBCMD)
                .help("additional help")

        val cmdExercise = subparsers.addParser(SUBCMD_CORE_EXERCISE)
                .defaultHelp(true)
                .help("exercise all the generators in Adventuresmith-core")
        val cmdList = subparsers.addParser(SUBCMD_CORE_LIST)
                .defaultHelp(true)
                .help("list all the generators in Adventuresmith-core")
        val cmdRun = subparsers.addParser(SUBCMD_RUN)
                .defaultHelp(true)
                .help("run a generator from the filesystem")

        listOf(cmdList, cmdRun, cmdExercise).forEach {
            it.addArgument("-l", "--locale")
                    .metavar("L")
                    .type(LocaleArgType())
                    .setDefault(Locale.US)
                    .help("can be specified multiple times. generator(s) will be run in the given locales")
        }
        mapOf(
                cmdRun to 1,
                cmdExercise to 20
        ).forEach {
            it.key.addArgument("-i", "--iterations")
                    .metavar("N")
                    .setDefault(it.value)
                    .help("number of iterations")
        }

        cmdExercise.addArgument("--out")
                .type(Arguments.fileType().verifyCanWrite())
                .help("Output file to which generator output will be written. If none given, will be output to console")

        cmdRun.addArgument("-R", "--fauxRandom")
                .metavar("R")
                .type(Int::class.java)
                .setDefault(-1)
                .help("Force the random # generator to always return the given number.")

        val opts = Options()
        try {
            parser.parseArgs(args, opts)

            when (opts.subcmd) {
                SUBCMD_CORE_EXERCISE -> exercise(opts)
                SUBCMD_CORE_LIST -> list(opts)
                SUBCMD_RUN -> runGenerator(opts)
                else -> parser.printUsage()
            }
        }catch ( e: ArgumentParserException) {
            parser.handleError(e);
            System.exit(1);
        }
    }

    private fun runGenerator(opts: Options) {
        logger.info("Running generator: ")
    }

    private fun listGens(locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(locale, collId, grpId).forEach {
            it -> logger.info("   -> {}", it.key.name)
        }
    }

    private fun runGens(iterations: Int, locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(locale, collId, grpId).forEach {
            it ->
                val gen = it.value
                val results = (1..iterations).map { gen.generate(locale) }.joinToString("\n")
                logger.info("   -> {}\n{}", it.key.name, results)
        }
    }

    private fun list(opts: Options) {
        val l = opts.locale
        for (coll in AdventuresmithCore.getCollections(l)) {
            if (coll.groups == null || coll.groups!!.isEmpty()) {
                logger.info("{} - {}", l, coll.name)
                listGens(l, coll.id)
                continue
            }
            for (grp in coll.groups!!.entries) {
                logger.info("{} - {} / {}", l, coll.name, grp.value)
                listGens(l, coll.id, grp.key)
            }
        }
    }

    private fun exercise(opts: Options) {
        val l = opts.locale
        for (coll in AdventuresmithCore.getCollections(l)) {
            if (coll.groups == null || coll.groups!!.isEmpty()) {
                logger.info("{} - {}", l, coll.name)
                runGens(opts.iterations, l, coll.id)
                continue
            }
            for (grp in coll.groups!!.entries) {
                logger.info("{} - {} / {}", l, coll.name, grp.value)
                runGens(opts.iterations, l, coll.id, grp.key)
            }
        }
    }
}
