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

package org.stevesea.adventuresmith.data

import android.content.Context
import android.os.Build
import groovy.transform.CompileStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.stevesea.adventuresmith.AdventuresmithApp
import org.stevesea.adventuresmith.BuildConfig
import org.stevesea.adventuresmith.data_k.ContextProvider
import org.stevesea.adventuresmith.data_k.fourth_page.FourthPageArtifactPipeline
import org.stevesea.adventuresmith.data_k.fourth_page.FourthPageCityPipeline

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

    @Before
    void setup() {
        context = RuntimeEnvironment.application
        ContextProvider.context = context

    }

    @Test
    void testFpArtifact() {
        def mockRandom = mock(Random.class)
        when(mockRandom.nextInt()).thenReturn(1)
        def shuffler = new org.stevesea.adventuresmith.data_k.Shuffler<String>(mockRandom)
        def gen = new FourthPageArtifactPipeline(shuffler)

        Assert.assertEquals("""\
<html>
  <body>
    <h4>
      Artifact
    </h4>
    <h5>
      Origin
    </h5>
    <p>
      <strong>
        <small>
          Holy
        </small>
      </strong>
      - It's been lost for centuries.
    </p>
    <h5>
      Power
    </h5>
    <p>
      <strong>
        <small>
          Blessing
        </small>
      </strong>
      - You see visions of the future.
    </p>
  </body>
</html>
""", gen.generate())

    }


    @Test
    void testFpCity() {
        def mockRandom = mock(Random.class)
        when(mockRandom.nextInt()).thenReturn(1)
        def shuffler = new org.stevesea.adventuresmith.data_k.Shuffler<String>(mockRandom)
        def gen = new FourthPageCityPipeline(shuffler)

        Assert.assertEquals("""\
<html>
  <body>
    <h4>
      City
    </h4>
    <h5>
      Feature
    </h5>
    <p>
      <strong>
        <small>
          District
        </small>
      </strong>
      - The garrison is a well-oiled machine.
    </p>
    <h5>
      Population
    </h5>
    <p>
      <strong>
        <small>
          Attitude
        </small>
      </strong>
      - They want you gone as soon as possible.
    </p>
    <h5>
      Society
    </h5>
    <p>
      <strong>
        <small>
          Faith
        </small>
      </strong>
      - Your heathen ways will not be tolerated.
    </p>
    <h5>
      Trouble
    </h5>
    <p>
      <strong>
        <small>
          Rumour
        </small>
      </strong>
      - There's treasure in those caves!
    </p>
  </body>
</html>
""", gen.generate())

    }
}
