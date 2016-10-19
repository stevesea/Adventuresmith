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

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

@CompileStatic
class SWNpolitical_party extends AbstractGenerator {
    List<String> leadership = [
            '<em>Social elite:</em> the group is led by members of the planet\'s ruling class',
            '<em>Outcasts:</em> the group\'s leadership is filled out by members of a taboo, outcast, or otherwise socially marginal group',
            '<em>Bourgeoisie:</em> the group is driven by a leadership drawn from the middle class and those aspiring to join the elite',
            '<em>Proletariat:</em> the working class, both agricultural and industrial, provides the leadership for this group',
            '<em>Urban:</em> city-dwellers compose the leadership of the group',
            '<em>Rural:</em> farmers, herdsmen, small-town artisans, and other residents of the rural zones of a planet make up the leadership of the group',
            '<em>Pious:</em> clergy and devout laymen of a religion form the leadership',
            '<em>Intellectuals:</em> the movement is led by intellectuals',
    ]
    List<String> economic_policy = [
            '<em>Laissez-faire:</em> minimal or no government intervention in the market',
            '<em>State industry:</em> the government should own or support specific industries important to the group',
            '<em>Protectionist:</em> the government should tax imports that threaten to displace the products of local manufactures',
            '<em>Autarky:</em> the government should ensure that the world can provide all of its own goods and services and forbid the import of foreign goods',
            '<em>Socialist:</em> the market should be harnessed to ensure a state-determined minimal standard of living for all',
            '<em>Communist:</em> the state should control the economy, disbursing its products according to need and determined efficiency',
    ]
    List<String> important_issues = """\
Poverty among the group's membership
Social hostility to the group's membership
Immigration and immigrants
The membership's important industries
Religion in public life
Gender roles and sexual mores
Culture of the group membership
Military preparedness
Governmental reform
Secession
Foreign relations
Wealth redistribution\
""".readLines()
    List<String> name1 = """\
People's
Freedom
National
Unified
Democratic
Royal
Social
Progressive
Popular
Republican
Federal
Liberty
(A local animal)
Homeland
Conservative
Liberal
Victory
Black
White
Red
Green
Blue
Yellow
Scarlet
Emerald
Cyan
Amber
Purple
Crimson
Grey
Orange
Bronze
Silver
Gold
Steel
Iron
Northern
Southern
Western
Eastern
Ascendant
Upward\
""".readLines()

    List<String> name2 = """\
Front
Party
Faction
Group
Element
Consensus
Council
Banner
Union
Combine
Society
Sodality
Brotherhood
Commune
Pact
Foundation
Fellowship
Guild
Federation
Alliance\
""".readLines()

    String generate () {
        """\
<strong>Political Party</strong> - ${pick(name1)} ${pick(name2)}
<br/>
<br/><strong><small>Leadership:</small></strong>
<br/>&nbsp;&nbsp;${pick(leadership)}
<br/>
<br/><strong><small>Economic Policy:</small></strong>
<br/>&nbsp;&nbsp;${pick(economic_policy)}
<br/>
<br/><strong><small>Important Issues:</small></strong>
<br/>&nbsp;&nbsp;${pickN(important_issues, roll('1d2+1')).join('<br/>&nbsp;&nbsp;')}
"""
    }
}
