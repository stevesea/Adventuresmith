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


import com.fasterxml.jackson.databind.*
import com.github.salomonbrys.kodein.*
import com.google.common.base.Throwables
import com.google.common.collect.*
import com.samskivert.mustache.*
import mu.KLoggable
import java.io.*
import java.util.*


interface Generator {
    fun getId() : String
    fun generate(locale: Locale = Locale.ENGLISH, input: Map<String, String>? = null) : String
    fun getMetadata(locale: Locale = Locale.ENGLISH): GeneratorMetaDto
}
interface ModelGenerator<T> {
    fun generate(locale: Locale = Locale.ENGLISH, input: Map<String, String>? = null) : T
    fun getMetadata(locale: Locale = Locale.ENGLISH): GeneratorMetaDto
}

interface DtoLoadingStrategy<out TDto> {
    fun load(locale: Locale) : TDto
    fun getMetadata(locale: Locale = Locale.ENGLISH): GeneratorMetaDto
}

interface ModelGeneratorStrategy<in TDto, out TModel> {
    fun transform(dto: TDto, input: Map<String, String>? = null) : TModel
}

interface ViewStrategy<in TModel, out TView> {
    fun transform(model: TModel) : TView
}

open class BaseGenerator<
        TDto,
        TModel>(
        val loadingStrat : DtoLoadingStrategy<TDto>,
        val modelGeneratorStrat: ModelGeneratorStrategy<TDto, TModel>) : ModelGenerator<TModel> {
    override fun generate(locale: Locale, input: Map<String, String>?): TModel {
        val inputData = loadingStrat.load(locale)
        val output = modelGeneratorStrat.transform(inputData, input)
        return output
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return loadingStrat.getMetadata(locale)
    }
}

open class BaseGeneratorWithView<TModel, TView>(
        val genId: String,
        val modelGen: ModelGenerator<TModel>,
        val viewTransform: ViewStrategy<TModel, TView>) : Generator {
    override fun getId(): String {
        return genId
    }
    override fun generate(locale: Locale, input: Map<String, String>?): String {
        return viewTransform.transform(modelGen.generate(locale, input)).toString().trim()
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return modelGen.getMetadata(locale)
    }
}

data class InputParamDto(val name: String,
                         val uiName: String,
                         val numbersOnly: Boolean = false, // hint to restrict input edits to numbers
                         val defaultValue: String = "",
                         val helpText: String = "",
                         val defaultOverridesUserEmpty: Boolean = true, // if user val is blank/null, use the default (ie. don't let user put blank in and confuse things)
                         val nullIfZero: Boolean = false,
                         val isInt: Boolean = false,
                         val maxVal: Int = Int.MAX_VALUE,
                         val minVal: Int = 0,
                         val values: List<String>? = null   // valid values
) {
    fun getVal(inputVal: String?) : Any? {
        var result = defaultValue
        if (inputVal.isNullOrBlank()) {
            if (!defaultOverridesUserEmpty) {
                // input is blank/null, and param's defaultVal should _not_ be used
                result = ""
            }
        } else {
            if (values != null) {
                // verify that the input is one of our valid values, set to default if not
                if (values.contains(inputVal)) {
                    result = inputVal.orEmpty()
                } else {
                    result = defaultValue
                }
            }
            result = inputVal.orEmpty()
        }
        if (result.isNullOrEmpty()) {
            return null
        }
        if (isInt) {
            try {
                val retval : Int = result.toInt()
                if (retval == 0 && nullIfZero) {
                    return null
                } else if (retval < minVal) {
                    return minVal
                } else if (retval > maxVal) {
                    return maxVal
                } else {
                    return retval
                }
            } catch (e: Exception) {
                return defaultValue
            }
        } else {
            return result
        }
    }
}
data class GeneratorInputDto(
        val displayTemplate: String,
        val useWizard: Boolean = true,
        val params: List<InputParamDto> = listOf()
) {
    fun mergeInputWithDefaults(input: Map<String,String>?) : Map<String,Any?> {
        val merged : MutableMap<String,Any?> = mutableMapOf()
        params.forEach {
            if (input != null) {
                merged.put(it.name, it.getVal(input.get(it.name)))
            } else {
                merged.put(it.name, it.getVal(""))
            }
        }
        return merged
    }

    fun processInputForDisplay(inputMap: Map<String, String>?) : String {
        try {
            val context = mergeInputWithDefaults(inputMap)
            return Mustache.compiler()
                    .escapeHTML(false)
                    .compile(displayTemplate)
                    .execute(context)
                    .trim()
        } catch (e: Exception) {
            return e.message.orEmpty()
        }
    }
}

data class GeneratorMetaDto(val name: String,
                            val collectionId: String,
                            val input: GeneratorInputDto? = null,
                            val groupId: String? = null,
                            val tags: List<String>? = null,
                            val desc: String? = null,
                            val priority: Int = 1000) : Comparable<GeneratorMetaDto> {
    override fun compareTo(other: GeneratorMetaDto): Int {
        return ComparisonChain.start()
                .compare(priority, other.priority)
                .compare(name, other.name)
                .compare(collectionId, other.collectionId)
                .compare(groupId.orEmpty(), other.groupId.orEmpty())
                .result()
    }
    fun mergeInputWithDefaults(inputMap: Map<String,String>?) : Map<String,Any?> {
        if (input != null) {
            return input.mergeInputWithDefaults(inputMap)
        }
        else {
            return mutableMapOf()
        }
    }
    fun processInputForDisplay(inputMap: Map<String, String>?) : String {
        if (input != null) {
            return input.processInputForDisplay(inputMap)
        }
        else {
            if (inputMap != null)
                return inputMap.toString()
            else
                return ""
        }
    }
}

data class GeneratorListDto(val generators: Map<String,List<String>>);

data class CollectionMetaDto(val url: String? = null,
                             val id: String,
                             val name: String,
                             val priority: Int,
                             val icon: String,
                             val groupIcons: Map<String,String>? = null,
                             val desc: String? = null,
                             val credit: String? = null,
                             val attribution: String? = null,
                             val hasGroupHierarchy: Boolean = false,
                             val groups: Map<String, String>? = null) : Comparable<CollectionMetaDto> {
    override fun compareTo(other: CollectionMetaDto): Int {
        return ComparisonChain.start()
                .compare(priority, other.priority)
                .compare(name, other.name)
                .compare(id, other.id)
                .result()
    }
    fun toMarkdownStr() : String {
        val sb = StringBuffer("## ${name} - ${credit}\n")
        if (url != null)
            sb.append("[url](${url})\n")
        sb.append("\n")
        sb.append(attribution.orEmpty())
        sb.append("\n")
        sb.append("\n")
        return sb.toString()
    }
    fun toHtmlStr(): String {
        val collname = name
        val str = html {
            body {
                if (credit != null) {
                    h2 { +"$collname - ${credit.orEmpty() }" }
                } else {
                    h2 { +"$collname}" }
                }
                if (url != null) {
                    p { + url }
                }
                if (desc != null) {
                    p { +desc}
                }
                if (attribution != null) {
                    p { + attribution }
                }
            }
        }.toString()
        return str
    }
}

class CollectionMetaLoader(override val kodein: Kodein) : KodeinAware {
    val resourceDeserializer: CachingResourceDeserializer = instance()
    fun load(collection_path: String, locale: Locale = Locale.US): CollectionMetaDto {
        return resourceDeserializer.deserialize(
                CollectionMetaDto::class.java,
                collection_path + "/" + "meta",
                locale
        )
    }
}

data class DataDrivenGenDto(val templates: RangeMap?,
                            val tables: Map<String, RangeMap>?,
                            val imports: List<String>?,
                            val nested_tables : Map<String, Map<String, RangeMap>>?,
                            val definitions: Map<String, Any>?)

class DataDrivenGenDtoCachingResourceLoader(val resource_prefix: String, override val kodein: Kodein)
: DtoLoadingStrategy<DataDrivenGenDto>, KodeinAware  {
    val resourceDeserializer: CachingResourceDeserializer = instance()
    override fun load(locale: Locale): DataDrivenGenDto {
        return resourceDeserializer.deserialize(
                DataDrivenGenDto::class.java,
                resource_prefix,
                locale
        )
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return resourceDeserializer.deserialize(
                GeneratorMetaDto::class.java,
                resource_prefix + ".meta",
                locale
        )
    }
}

class DataDrivenGenDtoFileDeserializer(val input: File, override val kodein: Kodein) :
        DtoLoadingStrategy<DataDrivenGenDto>, KodeinAware {
    val objectReader : ObjectReader = instance()

    override fun load(locale: Locale): DataDrivenGenDto {
        val bestFileForLocale = LocaleAwareResourceFinder.findBestFile(input, locale)
        return bestFileForLocale.bufferedReader().use {
            objectReader.forType(DataDrivenGenDto::class.java).readValue(it)
        }
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        val bestFileForLocale = LocaleAwareResourceFinder.findBestFile(File(input.absolutePath.replace(".yml", ".meta.yml")), locale)
        return bestFileForLocale.bufferedReader().use {
            objectReader.forType(GeneratorMetaDto::class.java).readValue(it)
        }
    }
}
class DataDrivenGeneratorForFiles(
        val inputFile: File,
        override val kodein: Kodein) : Generator, KodeinAware {

    val templateProcessor: DataDrivenDtoTemplateProcessor = instance()
    val shuffler: Shuffler = instance()
    val dtoMerger: DtoMerger = instance()
    val loaderFactory: (File) -> DataDrivenGenDtoFileDeserializer = factory()
    override fun getId(): String {
        return inputFile.absolutePath
    }
    override fun generate(locale: Locale, input: Map<String, String>?): String {
        try {
            val dto = loaderFactory.invoke(inputFile).load(locale)

            // load the map with the defaults first
            val inputMapForContext = getMetadata(locale).mergeInputWithDefaults(input)

            val context = dtoMerger.mergeDtos(
                    gatherDtoResources(dto, locale),
                    inputMapForContext)
            val template = shuffler.pick(dto.templates)

            return templateProcessor.processTemplate(template, context)
        } catch (ex: Exception) {
            throw IOException("problem running generator ${inputFile.name}: ${ex.message}", ex)
        }
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return loaderFactory.invoke(inputFile).getMetadata(locale)
    }

    fun gatherDtoResources(dto: DataDrivenGenDto, locale: Locale): List<DataDrivenGenDto> {
        val results: MutableList<DataDrivenGenDto> = mutableListOf(dto)

        dto.imports?.let {
            for (sibling in dto.imports) {

                val inDir = inputFile.absoluteFile.parentFile
                val sibling_file = File(inDir, sibling + ".yml")
                val d = loaderFactory.invoke(sibling_file).load(locale)
                results.addAll(gatherDtoResources(d, locale))
            }
        }
        return results
    }
}

class DataDrivenGeneratorForResources(
        val resource_prefix: String,
        override val kodein: Kodein) : Generator, KodeinAware {

    val templateProcessor: DataDrivenDtoTemplateProcessor = instance()
    val shuffler : Shuffler = instance()
    val dtoMerger : DtoMerger = instance()
    val loaderFactory : (String) -> DataDrivenGenDtoCachingResourceLoader = factory()
    override fun getId(): String {
        return resource_prefix
    }
    override fun generate(locale: Locale, input: Map<String, String>?): String {
        try {
            val dto = loaderFactory.invoke(resource_prefix).load(locale)

            // load the map with the defaults first
            val inputMap = getMetadata(locale).mergeInputWithDefaults(input)

            val context = dtoMerger.mergeDtos(
                    gatherDtoResources(dto, locale),
                    inputMap
            )
            val template = shuffler.pick(dto.templates)


            return templateProcessor.processTemplate(template, context)
        } catch (ex: Exception) {
            throw IOException("problem running generator ${resource_prefix}: ${ex.message}", ex)
        }
    }

    override fun getMetadata(locale: Locale): GeneratorMetaDto {
        return loaderFactory.invoke(resource_prefix).getMetadata(locale)
    }

    fun gatherDtoResources(dto: DataDrivenGenDto, locale: Locale) : List<DataDrivenGenDto> {
        val results: MutableList<DataDrivenGenDto> = mutableListOf(dto)

        dto.imports?.let {
            for (sibling in dto.imports) {
                val sibling_resource = if (resource_prefix.contains("/"))
                    resource_prefix.replaceAfterLast("/", sibling)
                else
                    sibling
                val d = loaderFactory.invoke(sibling_resource).load(locale)
                results.addAll(gatherDtoResources(d, locale))
            }
        }
        return results
    }
}

// TODO: if we merge as we read resources, we can give better error messages
class DtoMerger(override val kodein: Kodein) : KodeinAware {
    val shuffler : Shuffler = instance()

    // at first, i just wanted to silently overwrite. but, ran into too many issues during
    // generator creation where silent name overwrites resulted subtle bugs that took getting
    // into the debugger to figure out

    fun mergeDtos(dtos: List<DataDrivenGenDto>, input: Map<String, Any?>): Map<String, Any> {
        // process the DTOs in reverse order, merging them together
        val result: MutableMap<String, Any> = mutableMapOf()
        for (d in dtos.reversed()) {
            d.tables?.let {
                d.tables.entries.forEach {
                    if (result.containsKey(it.key)) {
                        if (result[it.key] != it.value) {
                            throw IOException("conflicting context key: ${it.key}")
                        }
                    }
                    result.put(it.key,it.value)
                }
            }
            d.nested_tables?.let {
                d.nested_tables.entries.forEach {
                    if (result.containsKey(it.key)) {
                        if (result[it.key] != it.value) {
                            throw IOException("conflicting context key: ${it.key}")
                        }
                    }
                    result.put(it.key,it.value)
                }
            }
            d.definitions?.let {
                d.definitions.entries.forEach {
                    if (result.containsKey(it.key)) {
                        if (result[it.key] != it.value) {
                            throw IOException("conflicting context key: ${it.key}")
                        }
                    }
                    result.put(it.key,it.value)
                }
            }
        }
        // templates are only read from the first DTO
        if (dtos[0].templates == null)
            throw IOException("missing 'templates' table")
        result.put("template", shuffler.pick(dtos[0].templates))
        result.put("input", input)

        return result
    }
}

class DataDrivenDtoTemplateProcessor(override val kodein: Kodein) : KodeinAware, KLoggable {

    override val logger = logger()

    val shuffler : Shuffler = instance()

    fun processTemplate(template: String, context: Map<String, Any>) : String {
        val state : MutableMap<String, Any> = mutableMapOf()

        val compiler = Mustache.compiler()
                .escapeHTML(false)
                .withFormatter(object : Mustache.Formatter {
                    // this method is called after jmustache locates {{key}} in the context.
                    // the context.key is passed to this method, and are given opportunity to
                    // do something special w/ the value
                    override fun format(value: Any?): String {
                        if (value is RangeMap) {
                            return shuffler.pick(value)
                        }
                        return value.toString()
                    }
                })
                .withLoader(object : Mustache.TemplateLoader {
                    // getTemplate is called to evaluate Partials {{>subtmpl}}
                    // in mustache, this typically means loading a different file.
                    // i'm going to abuse it to allow some dynamic template stuff

                    fun findCtxtVal(findVal: String) : Any {
                        val ctxtVal = context[findVal]
                        if (ctxtVal != null)
                            return ctxtVal

                        if (findVal.contains(".")) {
                            val pieces = findVal.split(".")
                            val v = context.get(pieces[0])
                            if (v is Map<*,*>) {
                                val v2 = v.get(pieces[1])
                                if (v2 != null) {
                                    return v2
                                }
                                throw IllegalArgumentException("couldn't find child ${pieces[1]} for ${findVal}")
                            }
                        }
                        throw IllegalArgumentException("unknown context key: ${findVal}")
                    }
                    override fun getTemplate(name: String?): Reader {
                        if (name == null)
                            return StringReader("null")
                        val cmd_and_params = name.trim().split(" ", limit=2)
                        if (cmd_and_params[0] == "pickN:") {
                            // {{>pickN: <dice/#/variable> <key> <delim>}}
                            val params = cmd_and_params[1].split(" ", limit = 3)

                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("pickN syntax must be: <dice/#/variable> <key> [<delim>]. input: ${cmd_and_params[1]}")
                            }

                            // could be entry in state or context, if neither of those try to roll it
                            val n = if (state.containsKey(params[0])) state.get(params[0]) as Int
                                else if (context.containsKey(params[0])) context.get(params[0]) as Int
                                else shuffler.roll(params[0])
                            // 2nd param must be which key in context to load
                            val ctxtKey = params[1]
                            val ctxtVal = findCtxtVal(ctxtKey)
                            val results = shuffler.pickN(ctxtVal, n)
                            val delim = if (params.size > 2) { params[2] } else { ", "}
                            return StringReader(results.joinToString(delim))
                        } else if (cmd_and_params[0] == "pick:") {
                            // {{>pick: <dice> <key>}}
                            val params = cmd_and_params[1].split(" ", limit=2)

                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("pick syntax must be: <dice/#> <key>. input: ${cmd_and_params[1]}")
                            }
                            // 1st param must be dice
                            val ctxtKey = params[1]
                            val ctxtVal = findCtxtVal(ctxtKey)
                            return StringReader(shuffler.pickD(params[0], ctxtVal))
                        } else if (cmd_and_params[0] == "titleCase:") {
                            // {{>titleCase: <val>}}
                            return StringReader(titleCase(cmd_and_params[1]))
                        } else if (cmd_and_params[0] == "roll:") {
                            // {{>roll: <dicestr>}}
                            return StringReader(shuffler.roll(cmd_and_params[1]).toString())
                        } else if (cmd_and_params[0] == "rollN:") {
                            // {{>rollN: <n> <dicestr> <delim>}}
                            val params = cmd_and_params[1].split(" ", limit = 3)
                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("rollN syntax must be: <#> <dice> [<delim>]. input: ${cmd_and_params[1]}")
                            }
                            val num = params[0].toInt()
                            val diceStr = params[1]
                            val delim = if (params.size > 2) {
                                params[2]
                            } else {
                                ", "
                            }
                            return StringReader(shuffler.rollN(diceStr, num).joinToString(delim))
                        } else if (cmd_and_params[0] == "rollKeepHigh:") {
                            // {{>rollKeepHigh: <rollN> <KeepN> <dicestr>}}
                            val params = cmd_and_params[1].split(" ", limit = 3)
                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("rollKeepHigh syntax must be: '<# to roll> <# to Keep> <dice>'. input: ${cmd_and_params[1]}")
                            }
                            val numToRoll = params[0].toInt()
                            val numToKeep = params[1].toInt()
                            val diceStr = params[2]
                            val rolls = shuffler.rollN(diceStr, numToRoll).sortedDescending()
                            val kept = rolls.take(numToKeep)
                            logger.debug("keephigh. rolled: $rolls , kept: $kept")
                            return StringReader(kept.sum().toString())
                        } else if (cmd_and_params[0] == "rollKeepLow:") {
                            // {{>rollKeepLow: <rollN> <KeepN> <dicestr>}}
                            val params = cmd_and_params[1].split(" ", limit = 3)
                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("rollKeepLow syntax must be: '<# to roll> <# to Keep> <dice>'. input: ${cmd_and_params[1]}")
                            }
                            val numToRoll = params[0].toInt()
                            val numToKeep = params[1].toInt()
                            val diceStr = params[2]
                            val rolls = shuffler.rollN(diceStr, numToRoll).sortedDescending()
                            val kept = rolls.takeLast(numToKeep)
                            logger.debug("keeplow. rolled: $rolls , kept: $kept")
                            return StringReader(kept.sum().toString())
                        } else if (cmd_and_params[0] == "add:") {
                            // {{>add: <variable> <val>}}
                            val params = cmd_and_params[1].split(" ", limit = 2)
                            val key = params[0]
                            val curVal = state.get(key)
                            if (curVal == null) {
                                state.put(key, params[1].toInt())
                            } else if (curVal is Int) {
                                state.put(key, curVal + params[1].toInt())
                            } else {
                                throw IllegalArgumentException("cannot 'add:'. value of state.${key} is not an integer")
                            }
                            return StringReader("")
                        } else if (cmd_and_params[0] == "set:") {
                            // {{>set: <variable> <val>}}
                            val params = cmd_and_params[1].split(" ", limit = 2)
                            val key = params[0]
                            state.put(key, params[1])
                            return StringReader("")
                        } else if (cmd_and_params[0] == "accum:") {
                            // {{>accum: <list-variable> <val>}}
                            val params = cmd_and_params[1].split(" ", limit = 2)
                            val key = params[0]
                            val curVal = state.get(key)
                            if (curVal == null) {
                                state.put(key, listOf(params[1]))
                            } else if (curVal is List<*>) {
                                state.put(key, curVal + listOf(params[1]))
                            }
                            return StringReader("")
                        } else if (cmd_and_params[0] == "remove:") {
                            // {{>remove: <list-variable> <val>}}
                            val params = cmd_and_params[1].split(" ", limit = 2)
                            val key = params[0]
                            val curVal = state.get(key)
                            if (curVal != null && curVal is List<*>) {
                                state.put(key, curVal.filter { it != params[1] })
                            }
                            return StringReader("")
                        } else if (cmd_and_params[0] == "get:") {
                            // {{>get: <variable> [<delim>]}}
                            val params = cmd_and_params[1].split(" ", limit = 2)
                            val key = params[0]
                            val curVal = state.get(key)
                            if (curVal == null) {
                                throw IllegalStateException("key '$key' from command '>$name' is null")
                            } else if (curVal is Collection<*>) {
                                val delim = if (params.size > 1) { params[1] } else { ", "}
                                return StringReader(curVal.joinToString(delim))
                            } else {
                                return StringReader(curVal.toString())
                            }
                        } else {
                            throw IllegalArgumentException("unknown instruction: '${name}'")
                        }
                    }
                })

        var result = template

        var count = 0;
        do {
            try {
                result = compiler
                        .compile(result)
                        .execute(context)
                        .trim()
                if (!result.contains("{{")) {
                    if (result.contains("%[[")) {

                        result = compiler
                                .compile(result
                                        .replace("%[[", "{{")
                                        .replace("]]%","}}"))
                                .execute(context)
                                .trim()
                    } else {
                        // result didn't contain curlies, or our final-curlies,
                        // so we are done
                        break
                    }
                }
                // don't let a template force us into infinite loop
                if (count > 12)
                    break

                count++
            } catch (ex: MustacheException) {
                throw MustacheException("problem processing: ${template} - ${ex.message}", ex)
            }
        } while (true)

        return result
    }
}

