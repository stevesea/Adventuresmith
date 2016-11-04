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
import java.io.*
import java.text.*
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
//    bind to same resource_prefix and generator, but make it not part of generator
data class DataDrivenGenMetaDto(val name: String,
                                val tags: List<String>?,
                                val desc: String)
data class DataDrivenGenDto(val templates: RangeMap?,
                            val tables: Map<String, RangeMap>?,
                            val include_tables: List<String>?,
                            val dice: List<String>?,
                            val nested_tables : Map<String, Map<String, RangeMap>>?)

class DataDrivenGenDtoLoader(val resource_prefix: String, override val kodein: Kodein)
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
    val shuffler : Shuffler = instance()
    val loaderFactory : (String) -> DataDrivenGenDtoLoader = factory()
    override fun generate(locale: Locale): String {
        try {
            val dto = loaderFactory.invoke(resource_prefix).load(locale)
            val template = shuffler.pick(dto.templates)
            val generatedModel = createContext(dto, locale)

            return processTemplate(template, generatedModel, locale)
        } catch (ex: Exception) {
            return "error running generator ${resource_prefix}: ${ex.toString()}"
        }
    }
    fun createContext(dto: DataDrivenGenDto, locale: Locale) : Map<String, Any> {
        val result : MutableMap<String,Any> = mutableMapOf()
        dto.include_tables?.let {
            for (sibling in dto.include_tables.reversed()) {
                val sibling_resource = if (resource_prefix.contains("/"))
                    resource_prefix.replaceAfterLast("/", sibling)
                else
                    sibling

                val sibling_dto = loaderFactory.invoke(sibling_resource).load(locale)
                sibling_dto.tables?.let {
                    result.putAll(sibling_dto.tables)
                }
                sibling_dto.nested_tables?.let {
                    result.putAll(sibling_dto.nested_tables)
                }
                sibling_dto.dice?.let {
                    for (dstr in sibling_dto.dice) {
                        result.put(dstr, shuffler.dice(dstr))
                    }
                }
            }
        }
        dto.tables?.let {
            result.putAll(dto.tables)
        }
        dto.nested_tables?.let {
            result.putAll(dto.nested_tables)
        }
        dto.dice?.let {
            for (dstr in dto.dice) {
                result.put(dstr, shuffler.dice(dstr))
            }
        }
        return result
    }
    fun processTemplate(template: String, context: Map<String, Any>, locale: Locale) : String {
        // use jmustache & lambdas?
        // https://github.com/samskivert/jmustache
        // also, look into anchors https://github.com/FasterXML/jackson-dataformat-yaml/issues/3

        val nf = NumberFormat.getInstance(locale)
        return Mustache.compiler()
                .escapeHTML(false)
                .withFormatter(object : Mustache.Formatter {
                    override fun format(value: Any?): String {
                        if (value is RangeMap) {
                            return shuffler.pick(value)
                        } else if (value is Dice) {
                            return nf.format(value.roll())
                        } else if (value is Map<*, *>) {
                            val p = shuffler.pickPairFromMapofRangeMaps(value as Map<String, RangeMap>?)
                            return "${p.first} - ${p.second}"
                        }
                        return value.toString()
                    }
                })
                .withLoader(object: Mustache.TemplateLoader {
                    // TODO: use this, not the stdDice map. just do it this way, and force people
                    //      to do {{> dice: 1d24}}
                    //      that way, can have non-'dice' keywords. example:
                    //            {{> pickN: forms, 3}}
                    // TODO: is this abusing partials? (to use them to run a 'special' function?
                    override fun getTemplate(name: String?): Reader {
                        if (name == null)
                            return StringReader("null")
                        // assume all partials (e.g. {{>name}} is diceStr
                        return StringReader(nf.format(shuffler.dice(name).roll()))
                    }
                })
                .compile(template)
                .execute(context)
                .trim()
    }
}
