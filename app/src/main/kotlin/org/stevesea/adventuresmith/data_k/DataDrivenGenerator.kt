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

// use strategy pattern for different components of generator pipeline
// https://github.com/dbacinski/Design-Patterns-In-Kotlin#strategy

// look here for fancy view idea
// https://kotlinlang.org/docs/reference/type-safe-builders.html

interface InputDataLoadingStrategy<GenInputData : AbstractInputData > {
    fun load() : GenInputData
}

interface GeneratorStrategy<GenInputData: AbstractInputData, GenOutputData : AbstractOutputData> {
    fun run(inDto: GenInputData) : GenOutputData
}

interface ViewStrategy<GenOutputData : AbstractOutputData> {
    fun transform(outData: GenOutputData) : String
}

abstract class AbstractInputData {

}

abstract class AbstractOutputData {

}

class DataDrivenGenerator {
}
