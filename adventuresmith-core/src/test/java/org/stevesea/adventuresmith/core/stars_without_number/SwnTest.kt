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

package org.stevesea.adventuresmith.core.stars_without_number

import org.junit.Assert
import org.junit.Test
import org.stevesea.adventuresmith.core.getFinalPackageName
import org.stevesea.adventuresmith.core.getGenerator
import java.util.Locale

object SwnConstants {
    val GROUP = getFinalPackageName(this.javaClass)

    val ADV_SEED = "${GROUP}/adventure_seed"
    val ALIEN = "${GROUP}/alien"
    val ANIMAL = "${GROUP}/animal"
    val ARCHITECTURE = "${GROUP}/architecture"
    val CORPORATION = "${GROUP}/corporation"
    val HERESY = "${GROUP}/heresy"
    val NAME_Arabic = "${GROUP}/names_arabic"
    val NAME_Chinese = "${GROUP}/names_chinese"
    val NAME_English = "${GROUP}/names_english"
    val NAME_Indian = "${GROUP}/names_indian"
    val NAME_Japanese = "${GROUP}/names_japanese"
    val NAME_Nigerian = "${GROUP}/names_nigerian"
    val NAME_Russian = "${GROUP}/names_russian"
    val NAME_Spanish = "${GROUP}/names_spanish"
    val NPC = "${GROUP}/npc"
    val POLITICAL_PARTY = "${GROUP}/political_party"
    val RELIGION = "${GROUP}/religion"
    val ROOM_DRESSING = "${GROUP}/room_dressing"

    val WORLD = "${GROUP}/world"

    val othergen = listOf(
            WORLD
    )

}

class SwnTest {

    @Test
    fun dressing() {
        Assert.assertEquals("Armory: locked gun cabinets, armor racks",
                getGenerator(SwnConstants.ROOM_DRESSING, 0).generate(Locale.US))
    }
    @Test
    fun arch() {
        Assert.assertEquals("""
        <h4>Architectural Elements</h4>

        Square foundations<br/>Bas-relief on walls
        <br/>Canals and pools
        """.trimIndent(), getGenerator(SwnConstants.ARCHITECTURE, 0).generate(Locale.US))
    }

    @Test
    fun religion() {
        Assert.assertEquals("""
        <h4>Religion</h4>
        <h5>Origin Tradition</h5>
        Judaism
        <h5>Evolution</h5>
        Schism; the faith's beliefs are actually almost identical to those of the majority of its origin tradition, save for a few minor points of vital interest to theologians and no practical difference whatsoever to believers. This does not prevent a burning resentment towards the parent faith.
        <h5>Leadership</h5>
        No universal leadership; no regional hierarchy
        """.trimIndent(), getGenerator(SwnConstants.RELIGION, 5).generate(Locale.US))
        // picked 5 for Random because that'll exercise one of the options that'll send us
        // through another pass of the template processor
    }

    @Test
    fun alien() {
        Assert.assertEquals("""
        <h4>Alien</h4>
        <strong><small>Body Type:</small></strong> Humanlike
        <br/><strong><small>Lenses:</small></strong> Curiosity, Despair
        <br/><strong><small>Social Structure:</small></strong> Democratic
        """.trimIndent(), getGenerator(SwnConstants.ALIEN, 0).generate(Locale.US))
    }

    @Test
    fun animal() {
        Assert.assertEquals("""
        <h4>Animal</h4>
        <strong>Insectile</strong><br/>&nbsp;&nbsp;Chewing mouthparts<br/>&nbsp;&nbsp;Jewel-colored chitin
        """.trimIndent(), getGenerator(SwnConstants.ANIMAL, 0).generate(Locale.US))
    }

    @Test
    fun corp() {
        Assert.assertEquals("""
        <h4>Corporation</h4>
        Ad Astra Alliance
        <h5>Businesses</h5>
        Agriculture<br/>Art
        <h5>Rumors</h5>
        Reckless with the lives of their employees<br/>Reckless with the lives of their employees
        """.trimIndent(), getGenerator(SwnConstants.CORPORATION, 0).generate(Locale.US))
    }
    @Test
    fun heresy() {
        Assert.assertEquals("""
        <h4>Heresy</h4>
        <h5>Founder</h5>
        <em>Defrocked clergy:</em> founded by a cleric outcast from the faith
        <h5>Major Heresy:</h5>
        <em>Manichaeanism:</em> the sect believes in harsh austerities and rejection of matter as something profane and evil
        <h5>Attitude towards orthodoxy</h5>
        <em>Filial:</em> the sect honors and respects the orthodox faith, but feels it is substantially in error
        <h5>Quirks</h5>
        Dietary prohibitions<br/>Characteristic item of clothing or jewelry
        """.trimIndent(), getGenerator(SwnConstants.HERESY, 0).generate(Locale.US))
    }
    @Test
    fun political_party() {
        Assert.assertEquals("""
        <h4>Political Party</h4>
        People's Front
        <h5>Leadership</h5>
        <em>Social elite:</em> the group is led by members of the planet's ruling class
        <h5>Economic Policy</h5>
        <em>Laissez-faire:</em> minimal or no government intervention in the market
        <h5>Important Issues</h5>
        Social hostility to the group's membership<br/>Immigration and immigrants
        """.trimIndent(), getGenerator(SwnConstants.POLITICAL_PARTY, 0).generate(Locale.US))
    }

    @Test
    fun world() {
        Assert.assertEquals("""
        <html>
          <body>
            <h3>
              World
            </h3>
            <h4>
              Physical Attributes
            </h4>
            <p>
              <strong>
                <small>
                  Atmosphere
                </small>
              </strong>
              Corrosive
              <br>
              </br>
              <strong>
                <small>
                  Temperature
                </small>
              </strong>
              Frozen
              <br>
              </br>
              <strong>
                <small>
                  Biosphere
                </small>
              </strong>
              Biosphere remnants
            </p>
            <h4>
              Cultural Attributes
            </h4>
            <p>
              <strong>
                <small>
                  Name
                </small>
              </strong>
              Andong
              <br>
              </br>
              <strong>
                <small>
                  Cultures
                </small>
              </strong>
              Chinese
            </p>
            <p>
              <strong>
                <small>
                  Population
                </small>
              </strong>
              Failed colony
              <br>
              </br>
              <strong>
                <small>
                  Tech Level
                </small>
              </strong>
              Tech Level 0. Stone-age technology.
            </p>
            <h4>
              World Tags
            </h4>
            <h6>
              Alien Ruins
            </h6>
            <blockquote>
              <em>
                The world has significant alien ruins present. The locals may or may not permit others to investigate the ruins, and may make it difficult to remove any objects of value without substantial payment.
              </em>
            </blockquote>
            <h6>
              Altered Humanity
            </h6>
            <blockquote>
              <em>
                The humans on this world are visibly and drastically different from normal humanity. They may have additional limbs, new sensory organs, or other significant changes. Were these from ancestral eugenic manipulation, or from environmental toxins?
              </em>
            </blockquote>
            <h6>
              Enemies
            </h6>
            <p>
              Worshipper of the ruins<br/>Hidden alien survivor<br/>Biochauvinist local
            </p>
            <h6>
              Friends
            </h6>
            <p>
              Avaricious local resident<br/>Interstellar smuggler<br/>Local seeking a “cure”
            </p>
            <h6>
              Complications
            </h6>
            <p>
              Remote location<br/>Paranoid customs officials<br/>Alteration is contagious
            </p>
            <h6>
              Things
            </h6>
            <p>
              Objects left with the remains of a prior unsuccessful expedition<br/>Untranslated alien texts<br/>Untouched hidden ruins
            </p>
            <h6>
              Places
            </h6>
            <p>
              Orbital ruin<br/>Perfectly preserved alien building<br/>Alien mausoleum
            </p>
          </body>
        </html>
        """.trimIndent(),
                getGenerator(SwnConstants.WORLD, 0).generate(Locale.US))
    }
}
