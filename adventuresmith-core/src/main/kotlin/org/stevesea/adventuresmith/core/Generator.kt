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
import com.google.common.base.*
import com.samskivert.mustache.*
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

// if all you need is just to load a YaML that's a single list
interface SimpleListLoader {
    fun load(locale: Locale) : List<String>
}
open class BaseSimpleGenerator (
        val listLoader : SimpleListLoader,
        override val kodein: Kodein) : Generator, KodeinAware {
    val shuffler : Shuffler = instance()
    override fun generate(locale: Locale): String {
        return shuffler.pick(listLoader.load(locale))
    }
}


data class TemplateMapModel(val template: String,
                        val map: Map<String,String>)

class ApplyTemplateView: ViewStrategy<TemplateMapModel, String> {
    override fun transform(model: TemplateMapModel): String {
        val result = inefficientStrSubstitutor(model.template, model.map)
        // throw an exception if any keywords weren't replaced
        Preconditions.checkArgument(!result.contains("%{"), "unreplaced keywords: '%s'", result)
        return result
    }
}

data class DataDrivenGenDto(val templates: RangeMap,
                            val tables: Map<String, RangeMap>)

class DataDrivenGenDtoLoader(val resource_prefix: String, override val kodein: Kodein) : DtoLoadingStrategy<DataDrivenGenDto>, KodeinAware  {
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
    val loader = loaderFactory.invoke(resource_prefix)

    override fun generate(locale: Locale): String {
        val dto = loader.load(locale)
        val template = shuffler.pick(dto.templates)
        val generatedModel = processDto(dto)

        return processTemplate(template, generatedModel)
    }
    fun processDto(dto: DataDrivenGenDto) : Map<String, String> {
        val result : MutableMap<String,String> = mutableMapOf()
        for (k in dto.tables.keys) {
            result.put(k, shuffler.pick(dto.tables[k]))
        }
        return result
    }
    fun processTemplate(template : String, model: Map<String, String>) : String {
        // use jmustache & lambdas?
        // https://github.com/samskivert/jmustache
        // also, look into anchors https://github.com/FasterXML/jackson-dataformat-yaml/issues/3

        try {
            return Mustache.compiler()
                    .escapeHTML(false)
                    .withFormatter(object : Mustache.Formatter {
                        override fun format(value: Any?): String {
                            return value.toString()
                        }
                    })
                    .compile(template)
                    .execute(model)
        } catch (ex: MustacheException) {
            return "problem running generator ${resource_prefix}: ${ex.message}"
        }
    }
}
