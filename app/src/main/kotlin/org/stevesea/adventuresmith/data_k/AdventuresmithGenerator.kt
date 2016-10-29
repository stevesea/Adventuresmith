/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.data_k

import android.content.*
import android.support.annotation.*
import android.support.v4.util.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import java.io.*
import java.nio.charset.*

// TODO: seems like long-term, having all this generation happen in a Jar library would make this
//  much easier to share among different client GUIs.

// TODO: use chain-of-responsibility to pick which generator to use? http://www.oodesign.com/chain-of-responsibility-pattern.html

// TODO: https://github.com/mcxiaoke/kotlin-koi (android)


// replaces elements of String with entries from the given map
// any keys from map in source string which match %{key} will be substituted with val
fun inefficientStrSubstitutor(inputStr: String, replacements: Map<String,String>) : String {
    var result = inputStr
    for (e in replacements.entries) {
        result = result.replace("%{${e.key}}", e.value )
    }
    return result
}

object MapperProvider {
    val mapper: ObjectMapper by lazy {
        ObjectMapper(YAMLFactory())
                .registerKotlinModule()
    }

    fun getReader() : ObjectReader = mapper.reader()
    fun getWriter() : ObjectWriter = mapper.writer()
}

object ContextProvider {
    var context: Context? = null
}

object CachingRawResourceDeserializer
{
    val maxSize = 16

    val cache: LruCache<Int, Any> = LruCache(maxSize)

    private fun open(@RawRes resId: Int): InputStream {
        return ContextProvider.context!!.resources.openRawResource(resId)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        synchronized(cache) {
            var result = cache.get(resId)
            if (result == null) {
                result = uncached_deserialize(resId, clazz, charset)
                cache.put(resId, result)
            }
            return result as T
        }
    }

    private fun <T> uncached_deserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        return open(resId).bufferedReader(charset).use {
            MapperProvider.getReader().forType(clazz).readValue(it)
        }
    }
}

object StringResourceLoader {
    fun get(@StringRes resId: Int) {
        ContextProvider.context!!.resources.getString(resId)
    }
}

interface Generator {
    fun generate() : String
}

interface DtoLoadingStrategy<out TDto> {
    fun load() : TDto
}

interface ModelGeneratorStrategy<in TDto, out TModel> {
    fun transform(dto: TDto) : TModel
}

interface ViewStrategy<in TModel, out TView> {
    fun transform(model: TModel) : TView
}
open class GeneratorLTV<
        TDto,
        TModel,
        out TView>(
        val loadingStrat : DtoLoadingStrategy<TDto>,
        val modelGeneratorStrat: ModelGeneratorStrategy<TDto, TModel>,
        val viewStrat: ViewStrategy<TModel, TView>) : Generator {

    fun generateView() : TView {
        val input = loadingStrat.load()
        val output = modelGeneratorStrat.transform(input)
        val viewOutput = viewStrat.transform(output)
        return viewOutput
    }

    override fun generate(): String {
        return generateView().toString()
    }
}
