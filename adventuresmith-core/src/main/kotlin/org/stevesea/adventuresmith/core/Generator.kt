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
                            val dice: List<String>?,
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
        //try {
            val dto = loaderFactory.invoke(resource_prefix).load(locale)

            return templateProcessor.process(gatherDtoResources(dto, locale))
        //} catch (ex: Exception) {
        //    return "error running generator ${resource_prefix}: ${ex.toString()}"
        //}
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
                results.add(d)
                results.addAll(gatherDtoResources(d, locale))
            }
        }
        return results
    }
}

class DataDrivenDtoTemplateProcessor(override val kodein: Kodein) : KodeinAware {
    companion object : KLogging()

    val shuffler : Shuffler = instance()


    fun process(dtos: List<DataDrivenGenDto>) : String {
        val context = mergeDtos(dtos)
        return processTemplate(context)
    }

    fun mergeDtos(dtos: List<DataDrivenGenDto>) : Map<String, Any> {
        // process the DTOs in reverse order, merging them together
        val result : MutableMap<String,Any> = mutableMapOf()
        for (d in dtos.reversed()) {
            d.tables?.let {
                result.putAll(d.tables)
            }
            d.nested_tables?.let {
                result.putAll(d.nested_tables)
            }
            d.dice?.let {
                for (dstr in d.dice) {
                    result.put(dstr, shuffler.dice(dstr))
                }
            }
            d.definitions?.let {
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
                        } else if (value is Dice) {
                            return value.roll().toString()
                        }
                        return value.toString()
                    }
                })
                .withLoader(object : Mustache.TemplateLoader {
                    // this method is called to evaluate Partials {{>subtmpl}}

                    // in mustache, this typically means loading a different file.

                    // TODO: use this, not the stdDice map. just do it this way, and force people
                    //      to do {{> dice: 1d24}}
                    //      that way, can have non-'dice' keywords. example:
                    //            {{> pickN: forms, 3}}
                    // TODO: is this abusing partials? (to use them to run a 'special' function?
                    //
                    // TODO: how complicated do want language to get? need parsing?
                    //    http://sargunvohra.me/cakeparse/
                    //    https://github.com/jparsec/jparsec/wiki/Overview
                    override fun getTemplate(name: String?): Reader {
                        if (name == null)
                            return StringReader("null")
                        val cmd_and_params = name.trim().split(" ", limit=2)
                        if (cmd_and_params[0] == "pickN:") {
                            val params = cmd_and_params[1].split(" ", limit=3)

                            // 1st param must be dice (includes int)
                            val n = shuffler.dice(params[0]).roll()
                            // 2nd param must be which key in context to load
                            val ctxtKey = params[1]
                            val coll = context.get(ctxtKey)
                            if (coll == null) {
                                throw IllegalArgumentException("unknown context key: ${ctxtKey}")
                            }
                            val results = shuffler.pickN(coll, n)
                            var delim = ", "
                            if (params.size > 2) {
                                delim = params[2]
                            }
                            return StringReader(results.joinToString(delim))
                        } else if (cmd_and_params[0] == "dice:") {
                            return StringReader(shuffler.dice(cmd_and_params[1]).roll().toString())
                        } else {
                            return StringReader("unknown instruction: '${name}'")
                        }

                    }
                })

        var template = context["template"].toString()

        var count = 0;
        do {
            val result =  compiler
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
        } while (true)
    }
}

