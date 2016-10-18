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

package org.stevesea.rpgpad.data.stars_without_number

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap;

@CompileStatic
class SWNWorld extends AbstractGenerator {

    RangeMap atmospheres = new RangeMap()
            .with(2, 'Corrosive')
            .with(3, 'Inert gas')
            .with(4, 'Airless or thin atmosphere')
            .with(5..9, 'Breatheable mix')
            .with(10, 'Thick atmosphere, breathable with a pressure mask')
            .with(11, 'Invasive, toxic atmosphere')
            .with(12, 'Corrosive and invasive atmosphere')
    RangeMap temperatures = new RangeMap()
            .with(2, 'Frozen')
            .with(3, 'Variable cold-to-temperate')
            .with(4..5, 'Cold')
            .with(6..8, 'Temperate')
            .with(9..10, 'Warm')
            .with(11, 'Variable temperate-to-warm')
            .with(12, 'Burning')
    RangeMap biospheres = new RangeMap()
            .with(2, 'Biosphere remnants')
            .with(3, 'Microbial life')
            .with(4..5, 'No native biosphere')
            .with(6..8, 'Human-miscible biosphere')
            .with(9..10, 'Immiscible biosphere')
            .with(11, 'Hybrid biosphere')
            .with(12, 'Engineered biosphere')

    RangeMap populations = new RangeMap()
            .with(2, 'Failed colony')
            .with(3, 'Outpost')
            .with(4..5, 'Tens of thousands of inhabitants')
            .with(6..8, 'Hundreds of thousands of inhabitants')
            .with(9..10, 'Millions of inhabitants')
            .with(11, 'Billions of inhabitants')
            .with(12, 'Alien civilization')
    RangeMap techlevels = new RangeMap()
            .with(2, 'Tech Level 0. Stone-age technology.')
            .with(3, 'Tech level 1. Medieval technology.')
            .with(4, 'Tech level 2. Nineteenth-century technology.')
            .with(5..6, 'Tech level 3. Twentieth-century technology.')
            .with(7..10, 'Tech level 4. Baseline postech.')
            .with(11, 'Tech level 4 with specialties or some surviving pretech.')
            .with(12, 'Tech level 5. Pretech, pre-Silence technology.')

    List<String> world_tags = """\
Abandoned Colony
Alien Ruins
Altered Humanity
Area 51
Badlands World
Bubble Cities
Civil War
Cold War
Colonized Population
Desert World
Eugenic Cult
Exchange Consulate
Feral World
Flying Cities
Forbidden Tech
Freak Geology
Freak Weather
Friendly Foe
Gold Rush
Hatred
Heavy Industry
Heavy Mining
Hostile Biosphere
Hostile Space
Local Specialty
Local Tech
Major Spaceyard
Minimal Contact
Misandry/Misogyny
Oceanic World
Out of Contact
Outpost World
Perimeter Agency
Pilgrimage Site
Police State
Preceptor Archive
Pretech Cultists
Primitive Aliens
Psionics Fear
Psionics Worship
Psionics Academy
Quarantined World
Radioactive World
Regional Hegemon
Restrictive Laws
Rigid Culture
Seagoing Cities
Sealed Menace
Sectarians
Seismic Instability
Secret Masters
Theocracy
Tomb World
Trade Hub
Tyranny
Unbraked AI
Warlords
Xenophiles
Xenophobes
Zombies
Artificial World
Weak Gravity
Heavy Gravity\
""".readLines()


    String template =  '''\
<strong>World</strong>
<br/><strong><small>Atmosphere:</small></strong> {{atmosphere}}
<br/><strong><small>Temperature:</small></strong> {{temperature}}
<br/><strong><small>Biosphere:</small></strong> {{biosphere}}
<br/>
<br/><strong><small>Population:</small></strong> {{population}}
<br/><strong><small>Tech Level:</small></strong> {{techlevel}}
<br/>
<br/><strong><small>World Tags:</small></strong> {{tags}}
'''
    String generate() {
        Mustache.compiler().compile(template).execute(
                [
                        atmosphere: pick('2d6', atmospheres),
                        temperature: pick('2d6', temperatures),
                        biosphere: pick('2d6', biospheres),
                        population: pick('2d6', populations),
                        techlevel: pick('2d6', techlevels),
                        tags: pickN(world_tags, 2).join(', '),
                ]
        )
    }
}
