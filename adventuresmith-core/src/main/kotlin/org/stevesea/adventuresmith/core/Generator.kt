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
import com.samskivert.mustache.*
import mu.*
import java.io.*
import java.util.*


interface Generator {
    fun generate(locale: Locale = Locale.ENGLISH) : String
}
interface ModelGenerator<T> {
    fun generate(locale: Locale = Locale.ENGLISH) : T
}

interface DtoLoadingStrategy<out TDto> {
    fun load(locale: Locale) : TDto
}

interface ModelGeneratorStrategy<in TDto, out TModel> {
    fun transform(dto: TDto) : TModel
}

interface ViewStrategy<in TModel, out TView> {
    fun transform(model: TModel) : TView
}

open class BaseGenerator<
        TDto,
        TModel>(
        val loadingStrat : DtoLoadingStrategy<TDto>,
        val modelGeneratorStrat: ModelGeneratorStrategy<TDto, TModel>) : ModelGenerator<TModel> {
    override fun generate(locale: Locale): TModel {
        val input = loadingStrat.load(locale)
        val output = modelGeneratorStrat.transform(input)
        return output
    }
}

open class BaseGeneratorWithView<TModel, TView>(
        val modelGen: ModelGenerator<TModel>,
        val viewTransform: ViewStrategy<TModel, TView>) : Generator {
    override fun generate(locale: Locale): String {
        return viewTransform.transform(modelGen.generate(locale)).toString().trim()
    }
}

// TODO: seems like we should read data about generators too
//    bind to same resource_prefix and generator, but make it not part of generator itself?
data class DataDrivenGenMetaDto(val name: String,
                                val tags: List<String>?,
                                val desc: String)
data class DataDrivenGenDto(val templates: RangeMap?,
                            val tables: Map<String, RangeMap>?,
                            val include_tables: List<String>?,
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
}

class DataDrivenGenerator(
        val resource_prefix: String,
        override val kodein: Kodein) : Generator, KodeinAware {
    companion object : KLogging()

    val templateProcessor: DataDrivenDtoTemplateProcessor = instance()
    val shuffler : Shuffler = instance()
    val loaderFactory : (String) -> DataDrivenGenDtoCachingResourceLoader = factory()
    override fun generate(locale: Locale): String {
        try {
            val dto = loaderFactory.invoke(resource_prefix).load(locale)

            return templateProcessor.process(gatherDtoResources(dto, locale))
        } catch (ex: Exception) {
            throw IOException("problem running generator ${resource_prefix} (locale: ${locale}): ${ex.toString()}", ex)
        }
    }
    fun gatherDtoResources(dto: DataDrivenGenDto, locale: Locale) : List<DataDrivenGenDto> {
        val results: MutableList<DataDrivenGenDto> = mutableListOf(dto)

        dto.include_tables?.let {
            for (sibling in dto.include_tables) {
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

class DataDrivenDtoTemplateProcessor(override val kodein: Kodein) : KodeinAware {
    companion object : KLogging()

    val shuffler : Shuffler = instance()

    // TODO: seems like merging DTOs can be separate from template processing
    fun process(dtos: List<DataDrivenGenDto>) : String {
        val context = mergeDtos(dtos)
        return processTemplate(context)
    }

    // at first, i just wanted to silently overwrite. but, ran into too many issues during
    // generator creation where name overwrites resulted in obvious thrown exceptions, but
    // i'd always have to go into the debugger to realize "oh! that's why!"
    private fun throwOnKeyCollisions(existingKeys: Set<String> , proposedKeys: Set<String>) {
        val common = existingKeys.intersect(proposedKeys)
        if (common.size > 0) {
            throw IOException("conflicting context key names: ${common}")
        }
    }

    fun mergeDtos(dtos: List<DataDrivenGenDto>) : Map<String, Any> {
        // process the DTOs in reverse order, merging them together
        val result : MutableMap<String,Any> = mutableMapOf()
        for (d in dtos.reversed()) {
            d.tables?.let {
                throwOnKeyCollisions(result.keys, d.tables.keys)
                result.putAll(d.tables)
            }
            d.nested_tables?.let {
                throwOnKeyCollisions(result.keys, d.nested_tables.keys)
                result.putAll(d.nested_tables)
            }
            d.definitions?.let {
                throwOnKeyCollisions(result.keys, d.definitions.keys)
                result.putAll(d.definitions)
            }
        }
        // templates are only read from the first DTO
        result.put("template", shuffler.pick(dtos[0].templates))

        // TODO: apply lambdas to context?

        /*
        val lambdas = mapOf(
                "pick" to Mustache.Lambda { fragment, writer ->
                    val key = fragment.execute()
                    writer.write(key)
                }
        )
        result.putAll(lambdas)
        */


        return result
    }

    fun processTemplate(context: Map<String, Any>) : String {

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
                    // this method is called to evaluate Partials {{>subtmpl}}
                    // in mustache, this typically means loading a different file.
                    //
                    // TODO: how complicated do want language to get? need parsing?
                    //    http://sargunvohra.me/cakeparse/
                    //    https://github.com/jparsec/jparsec/wiki/Overview
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
                            // {{>pickN: <dice/#> <key> <delim>}}
                            val params = cmd_and_params[1].split(" ", limit = 3)

                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("pickN syntax must be: <dice/#> <key> [<delim>]. input: ${cmd_and_params[1]}")
                            }

                            // 1st param must be dice (NOTE: dice str allows plain int)
                            val n = shuffler.roll(params[0])
                            // 2nd param must be which key in context to load
                            val ctxtKey = params[1]
                            val ctxtVal = findCtxtVal(ctxtKey)
                            val results = shuffler.pickN(ctxtVal, n)
                            var delim = ", "
                            if (params.size > 2) {
                                delim = params[2]
                            }
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
                        } else if (cmd_and_params[0] == "roll:") {
                            // {{>roll: <dicestr>}}
                            return StringReader(shuffler.roll(cmd_and_params[1]).toString())
                        } else if (cmd_and_params[0] == "rollN:") {
                            // {{>rollN: <n> <dicestr> <delim>}}
                            val params = cmd_and_params[1].split(" ", limit=3)
                            if (!(2..3).contains(params.size)) {
                                throw IllegalArgumentException("rollN syntax must be: <#> <dice> [<delim>]. input: ${cmd_and_params[1]}")
                            }
                            val num = params[0].toInt()
                            val diceStr = params[1]
                            var delim = ", "
                            if (params.size > 2) {
                                delim = params[2]
                            }
                            return StringReader(shuffler.rollN(diceStr, num).joinToString(delim))
                        } else {
                            throw IllegalArgumentException("unknown instruction: '${name}'")
                        }

                    }
                })

        var template = context["template"].toString()

        var count = 0;
        do {
            try {
                val result = compiler
                        .compile(template)
                        .execute(context)
                        .trim()
                if (!result.contains("{{"))
                    return result
                // don't let a template force us into infinite loop
                if (count > 4)
                    return result

                // do another iteration to re-process the result
                template = result
                count++
            } catch (ex: MustacheException) {
                throw MustacheException("problem processing template: ${template}", ex)
            }
        } while (true)
    }
}

