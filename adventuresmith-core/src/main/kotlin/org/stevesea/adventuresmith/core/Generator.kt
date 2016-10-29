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

import java.util.*

interface Generator {
    fun generate(locale: Locale = Locale.ENGLISH) : String
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
open class GeneratorLTV<
        TDto,
        TModel,
        out TView>(
        val loadingStrat : DtoLoadingStrategy<TDto>,
        val modelGeneratorStrat: ModelGeneratorStrategy<TDto, TModel>,
        val viewStrat: ViewStrategy<TModel, TView>) : Generator {

    fun generateView(locale: Locale) : TView {
        val input = loadingStrat.load(locale)
        val output = modelGeneratorStrat.transform(input)
        val viewOutput = viewStrat.transform(output)
        return viewOutput
    }

    override fun generate(locale: Locale): String {
        return generateView(locale).toString()
    }
}
