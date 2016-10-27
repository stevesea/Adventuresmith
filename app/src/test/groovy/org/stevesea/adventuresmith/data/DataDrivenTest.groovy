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
import org.stevesea.adventuresmith.R
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

    static class ArtifactInputData {
        Map<String,List<String>> origins
        Map<String,List<String>> powers
    }
    static class ArtifactOutputData {
        KV origin
        KV power
    }
    static class KV {
        String key
        String val
    }

    static class MyGen extends AbstractDataDrivenGenerator {
        MyGen() {
            rawResId = R.raw.fourth_page_artifact
        }

        ArtifactOutputData pick() {
            def input = parseResource() as ArtifactInputData

            String originKey = pick(input.origins.keySet())
            String powerKey = pick(input.powers.keySet())
            new ArtifactOutputData(
                    origin: new KV(key: originKey, val: pick(input.origins.get(originKey)) as String),
                    power: new KV(key: powerKey, val: pick(input.powers.get(powerKey)) as String)
            )
        }

        @Override
        String generate() {
            def out = pick()
            """\
<h4>Artifact</h4>
<h5>Origin</h5>
<strong><small>${out.origin.key}</small></strong> - ${out.origin.val}
<h5>Power</h5>
<strong><small>${out.power.key}</small></strong> - ${out.power.val}
"""

        }
    }

    @Before
    void setup() {
        context = RuntimeEnvironment.application
    }

    @Test
    void testslurp() {
        def gen = new MyGen()
                .withContext(context)

        Assert.assertEquals("asdf", gen.generate())

    }


}
