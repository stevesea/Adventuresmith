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

package org.stevesea.adventuresmith.data.stars_without_number

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator

@CompileStatic
class SWNarchitecture extends AbstractGenerator {
    Map<String, List<String>> arch_elements = [
        towers: """\
Sharply pointed towers
Squat towers
Square, hexagonal, or oval towers
Multi-branching towers
Inverted towers, stretching underground
Bulbous towers
Flat-topped towers
Multiple towers that merge into one
Skeletal towers
Twisted towers\
""".readLines(),
        foundations: """\
Square foundations
Hexagonal foundations
Circular foundations
Raised foundations
Pillared foundations
Sloped foundations
Entrenched foundations
Elongated rectangular foundations
Triangular foundations
Oval foundations\
""".readLines(),
        wall_decoration: """\
Bas-relief on walls
Painting on walls
Mosaics on walls or floors
Statues inset in wall niches
Tiling on surfaces
Carvings on walls
Moldings on walls
Paneling on walls
Geometric designs on surfaces
Featureless surfaces\
""".readLines(),
        pillars: """\
Flying buttresses
Smooth pillars
Adorned pillars
Raised embankments
Pier buttresses
Scroll buttresses
False, decorative buttresses
Pyramidal support piers
Squared support piers
Seeming lack of supports or buttresses\
""".readLines(),
        arches: """\
Round arches
Lancet arches
Oriental arches
Horseshoe arches
Multifoil arches
Monumental arches
Keyhole arches
Inflexed arches
Flat arches
Corbelled arches\
""".readLines(),
]
    List<String> misc = """\
Canals and pools
Balconies and overlooks
Subterranean structures
Absence or profusion of windows
Walled or enclosed courtyards
Open plazas
Elevated walkways
Monoliths or standing stones
Meandering pathways
Climbing vegetation\
""".readLines()


    String generate () {
        List<String> elements = new ArrayList<>()
        List<String> keys = pickN(arch_elements.keySet(), roll('1d3+2')) as List<String> // at least 2, up to 5

        keys.each { it -> elements.add(pick(arch_elements.get(it)) as String)}

        """\
<h4>Architectural Elements</h4>
&nbsp;&nbsp;${elements.join('<br/>&nbsp;&nbsp;')}
<br/>&nbsp;&nbsp;${pickN(misc, roll('1d3')).join('<br/>&nbsp;&nbsp;')}
"""
    }
}
