/*
 * Copyright (c) 2017 Steve Christensen
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

package org.stevesea.adventuresmith.core.ss_and_ss

import org.junit.Assert
import org.junit.Test
import org.stevesea.adventuresmith.core.getFinalPackageName
import org.stevesea.adventuresmith.core.getGenerator
import java.util.Locale

object SsAndSsConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val MONSTER = "${GROUP}/monster"

}

class SsAndSsTest {

    @Test
    fun monster() {
        Assert.assertEquals("""
            <h4>Humanoid</h4>
            Elongated Limbs (can make melee attacks to opponents within nearby range)
            <br/><br/>
            Culture: Praises brute strength above all else and dominates inferior races.""".trimIndent(),
                getGenerator(SsAndSsConstants.MONSTER, 0).generate(Locale.US))
    }
    @Test
    fun monster2() {
        Assert.assertEquals("""
            <h4>Plant</h4>
            Vines
            <br/><br/>
            Attack: Poisonous Spores

            <h5>Powers</h5>
            <em>Acid Attack</em>: Acid deals a die of damage 1 step below normal for the creature's
            HD for HD rounds.


            <h5>Weaknesses</h5>
            Vanity""".trimIndent(),
                getGenerator(SsAndSsConstants.MONSTER, 2).generate(Locale.US))
    }
}