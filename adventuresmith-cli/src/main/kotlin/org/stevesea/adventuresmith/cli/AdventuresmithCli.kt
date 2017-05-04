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

import mu.KLoggable
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.annotation.Arg
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Argument
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.ArgumentType
import org.stevesea.adventuresmith.core.AdventuresmithCore
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Locale

class LocaleArgType : ArgumentType<Locale> {
    override fun convert(parser: ArgumentParser?, arg: Argument?, value: String?): Locale {
        try {
            val split = value?.split('-', '_')
            when (split?.size ) {
                1 -> return Locale(split[0])
                2 -> return Locale(split[0].toLowerCase(), split[1])
                else -> return Locale(split?.get(0)?.toLowerCase(), split?.get(1), split?.get(3))
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
    val SUBCMD_CORE_DOCS = "core-docs"
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
        val cmdDocs = subparsers.addParser(SUBCMD_CORE_DOCS)
                .defaultHelp(true)
                .help("generate documentation for all the generators in Adventuresmith-core")
        val cmdRun = subparsers.addParser(SUBCMD_RUN)
                .defaultHelp(true)
                .help("run a generator from the filesystem")

        listOf(cmdList, cmdRun, cmdExercise, cmdDocs).forEach {
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

        listOf(cmdRun, cmdExercise, cmdDocs).forEach {
            it.addArgument("-o", "--out")
                .type(Arguments.fileType())
                .help("Output file to which generator output will be written. If none given, will be output to console")
        }

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
                SUBCMD_CORE_DOCS -> docsGen(opts)
                else -> parser.printUsage()
            }
        } catch ( e: ArgumentParserException) {
            parser.handleError(e)
            System.exit(1)
        } catch ( e: IOException) {
            logger.error(e.message)
            System.exit(1)
        } catch ( e: Exception) {
            logger.error(e.message.orEmpty(), e)
            System.exit(1)
        }
    }

    private fun runGenerator(opts: Options) {
        val generator = AdventuresmithCore.getGenerator(opts.input ?: throw IllegalArgumentException("No file specified"))

        val results = (1..opts.iterations).map { generator.generate(opts.locale) }.joinToString("\n")
        if (opts.out == null) {
            logger.info("Running generator: {}\n{}", opts.input?.normalize(), results)
        } else {
            logger.info("Running generator: {} -> {}", opts.input?.normalize(), opts.out?.absolutePath)
            opts.out?.writeText(results)
        }
    }

    private fun listGens(locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(collId, grpId).forEach {
            val genMeta = it.getMetadata(locale)
            logger.info("   -> {}", genMeta.name)
        }
    }

    private fun docsGen(opts: Options) {
        val l = opts.locale
        val message = "# Content Attribution\n"
        if (opts.out == null) {
            print(message)
        } else {
            opts.out?.writeText(message)
        }
        AdventuresmithCore.getCollectionMetas(l).values.forEach { coll ->
            if (coll.credit != null) {
                if (opts.out == null) {
                    print(coll.toMarkdownStr())
                } else {
                    opts.out?.appendText(coll.toMarkdownStr())
                }
            }
        }
    }

    private fun runGens(opts: Options, locale: Locale, collId: String, grpId: String? = null) {
        AdventuresmithCore.getGeneratorsByGroup(collId, grpId).forEach {
            it ->
                val genMeta = it.getMetadata(locale)
                val gen = it
                val results = (1..opts.iterations).map { gen.generate(locale) }.joinToString("\n")
                if (opts.out == null) {
                    logger.info("   -> {}\n{}", genMeta.name, results)
                } else {
                    logger.info("   -> {}", genMeta.name)
                    // TODO: this isn't efficient, should user buffered writer
                    opts.out?.appendText(results + "\n")
                }
        }
    }

    private fun list(opts: Options) {
        val l = opts.locale
        AdventuresmithCore.collections.forEach { (collId, collDto) ->
            val coll = AdventuresmithCore.getCollectionMetaData(collId, l)

            if (collDto.generators.isNotEmpty()) {
                logger.info("{} - {} ({})", l, coll.name, collId)
                listGens(l, collId)
            }
            collDto.groupedGenerators.keys.forEach { grp ->
                logger.info("{} - {} / {} ({})", l, coll.name, grp, collId)
                listGens(l, collId, grp)
            }
        }
    }

    private fun exercise(opts: Options) {
        val l = opts.locale
        if (opts.out != null) {
            if (opts.out?.exists() as Boolean) {
                opts.out?.delete()
            }
        }
        AdventuresmithCore.collections.forEach { (collId, coll) ->
            val collMeta = AdventuresmithCore.getCollectionMetaData(collId, l)
            if (coll.generators.isNotEmpty()) {
                logger.info("{} - {} ({})", l, collMeta.name, collId)
                runGens(opts, l, collId)
            }
            if (coll.groupedGenerators.isNotEmpty()) {
                coll.groupedGenerators.keys.forEach { grp ->
                    logger.info("{} - {} / {} ({}/{})", l, collMeta.name, grp, collId)
                    runGens(opts, l, collId, grp)
                }
            }
        }
    }
}
