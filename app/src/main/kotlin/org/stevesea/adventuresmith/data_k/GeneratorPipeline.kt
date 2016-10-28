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

interface ViewTransformStrategy<in TOutputDto, out ViewData> {
    fun transform(outData: TOutputDto) : ViewData
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

// TODO: this holds a copy of the output object in the field. is that bad?
abstract class RawResourceLoader<T>(
        @RawRes val resId: Int,
        val charset: Charset = StandardCharsets.UTF_8)
: DataLoadingStrategy<T>
{
    fun open(@RawRes resId: Int): InputStream {
        return ContextProvider.context!!.resources.openRawResource(resId)
    }

    fun deserialize(clazz: Class<T>): T {
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

open class GeneratorPipeline<
        TInputDto,
        TOutputDto,
        out ViewData>(
        val loadingStrat : DataLoadingStrategy<TInputDto>,
        val generatorStrat: GeneratorTransformStrategy<TInputDto, TOutputDto>,
        val viewStrat: ViewTransformStrategy<TOutputDto, ViewData>) {

    fun generate() : ViewData {
        val input = loadingStrat.load()
        val output = generatorStrat.transform(input)
        val viewOutput = viewStrat.transform(output)
        return viewOutput
    }
}
