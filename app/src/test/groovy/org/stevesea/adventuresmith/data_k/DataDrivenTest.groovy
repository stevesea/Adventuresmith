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

package org.stevesea.adventuresmith.data_k

import android.content.Context
import android.os.Build
import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.stevesea.adventuresmith.AdventuresmithApp
import org.stevesea.adventuresmith.BuildConfig
import org.stevesea.adventuresmith.core.Shuffler

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
// http://stackoverflow.com/questions/28960898/getting-context-in-androidtestcase-or-instrumentationtestcase-in-android-studio
// http://wiebe-elsinga.com/blog/whats-new-in-android-testing/
// https://developer.android.com/training/testing/unit-testing/local-unit-tests.html
// http://www.crmarsh.com/configuring-robolectric/
@Config(application = AdventuresmithApp.class,
        constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP,
        packageName = "org.stevesea.adventuresmith" // have product flavors, so must define packageName
)
@RunWith(RobolectricGradleTestRunner.class)
@CompileStatic
class DataDrivenTest {
    Context context
    Shuffler shuffler

    @Before
    void setup() {
        context = RuntimeEnvironment.application
        //ContextProvider.context = context
        def mockRandom = mock(Random.class)
        when(mockRandom.nextInt()).thenReturn(1)
        //shuffler = new Shuffler(mockRandom)

    }

    @Test
    void testFpArtifact() {

    }
}
