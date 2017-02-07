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
    var filter: String = ""

    @Arg
    var subcmd : String = ""

    @Arg
    var locale : Locale = Locale.US

    @Arg
    var fauxRandom: Int = -1

    @Arg
    var out : File? = null

    @Arg
    var input : File? = null

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
                    .help("generator(s) will be run in the given locale")
        }
        mapOf(
                cmdRun to 1,
                cmdExercise to 20
        ).forEach {
            it.key.addArgument("-i", "--iterations")
                    .metavar("N")
                    .type(Int::class.java)
                    .setDefault(it.value)
                    .help("number of iterations")
        }

        listOf(cmdRun, cmdExercise).forEach {
            it.addArgument("-o", "--out")
                .type(Arguments.fileType())
                .help("Output file to which generator output will be written. If none given, will be output to console")
        }

        cmdExercise.addArgument("--filter")
                .help("filter which generators to run")

        cmdRun.addArgument("input")
                .type(Arguments.fileType().verifyExists().verifyCanRead())
                .metavar("FILE")
                .required(true)
                .help("generator input file")

        /*
        TODO: use mockito?
        cmdRun.addArgument("-R", "--fauxRandom")
                .metavar("R")
                .type(Int::class.java)
                .setDefault(-1)
                .help("Force the random # generator to always return the given number.")
        */

        val opts = Options()
        try {
            parser.parseArgs(args, opts)

            when (opts.subcmd) {
                SUBCMD_CORE_EXERCISE -> exercise(opts)
                SUBCMD_CORE_LIST -> list(opts)
                SUBCMD_RUN -> runGenerator(opts)
                else -> parser.printUsage()
            }
        } catch ( e: ArgumentParserException) {
            parser.handleError(e)
            System.exit(1);
        } catch ( e: IOException) {
            logger.error(e.message)
            System.exit(1)
        } catch ( e: Exception) {
            logger.error(e.message.orEmpty(), e)
            System.exit(1)
        }
    }

    private fun runGenerator(opts: Options) {
        val generator = AdventuresmithCore.getGenerator(opts.input!!)

        val results = (1..opts.iterations).map { generator.generate(opts.locale) }.joinToString("\n")
        if (opts.out == null) {
            logger.info("Running generator: {}\n{}", opts.input!!.normalize(), results)
        } else {
            logger.info("Running generator: {} -> {}", opts.input!!.normalize(), opts.out!!.absolutePath)
            opts.out!!.writeText(results)
        }
    }

    private fun listGens(locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(locale, collId, grpId).forEach {
            it -> logger.info("   -> {}", it.key.name)
        }
    }

    private fun runGens(opts: Options, locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(locale, collId, grpId).forEach {
            it ->
                val gen = it.value
                val results = (1..opts.iterations).map { gen.generate(locale) }.joinToString("\n")
                if (opts.out == null) {
                    logger.info("   -> {}\n{}", it.key.name, results)
                } else {
                    logger.info("   -> {}", it.key.name)
                    // TODO: this isn't efficient, should user buffered writer
                    opts.out!!.appendText(results + "\n")
                }
        }
    }

    private fun list(opts: Options) {
        val l = opts.locale
        for (coll in AdventuresmithCore.getCollections(l)) {
            if (coll.groups == null || coll.groups!!.isEmpty()) {
                logger.info("{} - {} ({})", l, coll.name, coll.id)
                listGens(l, coll.id)
                continue
            }
            for (grp in coll.groups!!.entries) {
                logger.info("{} - {} / {} ({}/{})", l, coll.name, grp.value, coll.id, grp.key)
                listGens(l, coll.id, grp.key)
            }
        }
    }

    private fun exercise(opts: Options) {
        val l = opts.locale
        if (opts.out != null) {
            if (opts.out!!.exists()) {
                opts.out!!.delete()
            }
        }

        for (coll in AdventuresmithCore.getCollections(l)) {
            if (coll.groups == null || coll.groups!!.isEmpty()) {
                if (!opts.filter.isNullOrEmpty() && !coll.id.toLowerCase().startsWith(opts.filter.toLowerCase())) {
                    continue
                }
                logger.info("{} - {} ({})", l, coll.name, coll.id)
                runGens(opts, l, coll.id)
                continue
            }
            for (grp in coll.groups!!.entries) {
                if (!opts.filter.isNullOrEmpty() && !"${coll.id}/${grp.key}".toLowerCase().startsWith(opts.filter.toLowerCase())) {
                    continue
                }
                logger.info("{} - {} / {} ({}/{})", l, coll.name, grp.value, coll.id, grp.key)
                runGens(opts, l, coll.id, grp.key)
            }
        }
    }
}
