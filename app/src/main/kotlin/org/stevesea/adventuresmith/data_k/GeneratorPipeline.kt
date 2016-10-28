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

// TODO: https://github.com/mcxiaoke/kotlin-koi

// TODO: use strategy pattern for different components of generator pipeline
// https://github.com/dbacinski/Design-Patterns-In-Kotlin#strategy
// https://github.com/dbacinski/Design-Patterns-In-Kotlin#strategy

// TODO: look here for fancy view idea  https://kotlinlang.org/docs/reference/type-safe-builders.html

interface DataLoadingStrategy<out TInputDto> {
    fun load() : TInputDto
}

interface GeneratorTransformStrategy<in TInputDto, out TOutputDto> {
    fun transform(inDto: TInputDto) : TOutputDto
}

interface ViewTransformStrategy<in TInputDto, in TOutputDto, out ViewData> {
    fun transform(inData: TInputDto, outData: TOutputDto) : ViewData
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

object RawResourceDeserializer
{
    val maxSize = 16

    val cache: LruCache<Int, Any> = LruCache(maxSize)

    private fun open(@RawRes resId: Int): InputStream {
        return ContextProvider.context!!.resources.openRawResource(resId)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> cachedDeserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        synchronized(cache) {
            var result = cache.get(resId)
            if (result == null) {
                result = deserialize(resId, clazz, charset)
                cache.put(resId, result)
            }
            return result as T
        }
    }

    fun <T> deserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        return open(resId).bufferedReader(charset).use {
            MapperProvider.mapper.readValue(it, clazz)
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

open class GeneratorPipeline<
        TInputDto,
        TOutputDto,
        out ViewData>(
        val loadingStrat : DataLoadingStrategy<TInputDto>,
        val generatorStrat: GeneratorTransformStrategy<TInputDto, TOutputDto>,
        val viewStrat: ViewTransformStrategy<TInputDto, TOutputDto, ViewData>) : Generator {

    fun generateView() : ViewData {
        val input = loadingStrat.load()
        val output = generatorStrat.transform(input)
        val viewOutput = viewStrat.transform(input, output)
        return viewOutput
    }

    override fun generate(): String {
        return generateView().toString()
    }
}
