/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stevesea.rpg_boy2000.data.perilous_wilds

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.steavesea.rpg_boy2000.data.Shuffler
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwPlace
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwRegion

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@CompileStatic
@RunWith(MockitoJUnitRunner)
class PerilousWildsGeneratorTest {

    @Mock
    Random mockRandom

    PwRegion region
    PwPlace place


    @Before
    void setup() {
        // set the shuffler up so that it's always just returning the first item
        Shuffler shuffler = new Shuffler(mockRandom);
        when(mockRandom.nextInt(any(Integer.class))).thenReturn(0)

        region = new PwRegion(shuffler);
        place = new PwPlace(shuffler);
    }

    @Test
    void testRegion() {
        assertEquals("Ageless Bay", region.generate())
    }

    @Test
    void testPlace() {
        assertEquals("The Barrier", place.generate())
    }
}
