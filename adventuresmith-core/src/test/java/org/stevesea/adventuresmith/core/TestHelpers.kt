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
import com.nhaarman.mockito_kotlin.*
import java.security.*
import java.util.*

fun getMockRandom(mockRandomVal: Int = 1) : Random {
    val mockRandom : Random = mock()
    whenever(mockRandom.nextInt(any())).thenReturn(mockRandomVal)
    return mockRandom
}

fun getKodein(random: Random) = Kodein {
    import(randModule)
    import(generatorModules)
    bind<Random>(overrides = true) with instance (random)
}

fun getGenerator(genName: String, mockRandomVal: Int = -1) : Generator{
    if (mockRandomVal < 0)
        return getKodein(SecureRandom()).instance(genName)
    return getKodein(getMockRandom(mockRandomVal)).instance(genName)
}
