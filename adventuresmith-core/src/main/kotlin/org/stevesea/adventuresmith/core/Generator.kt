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
import org.stevesea.adventuresmith.core.fotf.*
import org.stevesea.adventuresmith.core.fp.*
import org.stevesea.adventuresmith.core.maze_rats.*
import java.util.*

val utilModule = Kodein.Module {
    bind<Shuffler>() with singleton { Shuffler() }
}

object AdventureSmithConstants {
    val GENERATORS = "generators"
}
val generatorModules = Kodein.Module {
    import(fotfModule)
    import(fpModule)
    import(mrModule)

    bind<List<String>>(AdventureSmithConstants.GENERATORS) with provider {
        val res : MutableList<String> = mutableListOf()
        res.addAll(instance(FotfConstants.GROUP))
        res.addAll(instance(MrConstants.GROUP))
        res.addAll(instance(FpConstants.GROUP))
        res
    }
}

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

// TODO: how does library advertise which generators exist, and how does client call 'em?
// TODO: need to do this entire chain? Or should the library only be supplying the model?
open class BaseGeneratorOld<
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
        return generateView(locale).toString().trim()
    }
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
        val shuffler: Shuffler = Shuffler()) : Generator {
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

/*
data class FotfSpellModel(val template: String, val map: Map<String,String>) {
    //neat... but we don't need these names
    val element: String by map
    val form1: String by map
}
    */

