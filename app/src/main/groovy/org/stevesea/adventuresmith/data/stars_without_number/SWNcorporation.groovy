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

package org.stevesea.adventuresmith.data.stars_without_number

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.RangeMap;

@CompileStatic
class SWNcorporation extends AbstractGenerator {
    List<String> names = """\
Ad Astra
Colonial
Compass
Daybreak
Frontier
Guo Yin
Highbeam
Imani
Magnus
Meteor
Neogen
New Dawn
Omnitech
Outertech
Overwatch
Panstellar
Shogun
Silverlight
Spiker
Stella
Striker
Sunbeam
Terra Prime
Wayfarer
West Wind\
""".readLines()

    List<String> organizations = """\
Alliance
Association
Band
Circle
Clan
Combine
Company
Cooperative
Corporation
Enterprises
Faction
Group
Megacorp
Multistellar
Organization
Outfit
Pact
Partnership
Ring
Society
Sodality
Syndicate
Union
Unity
Zaibatsu\
""".readLines()

    List<String> businesses = """\
Aeronautics
Agriculture
Art
Assassination
Asteroid Mining
Astrotech
Biotech
Bootlegging
Computer Hardware
Construction
Cybernetics
Electronics
Energy Weapons
Entertainment
Espionage
Exploration
Fishing
Fuel Refining
Gambling
Gemstones
Gengineering
Grav Vehicles
Heavy Weapons
Ideology
Illicit Drugs
Journalism
Law Enforcement
Liquor
Livestock
Maltech
Pharmaceuticals
Piracy
Planetary Mining
Plastics
Pretech
Prisons
Programming
Projectile Guns
Prostitution
Psionics
Psitech
Robotics
Security
Shipyards
Snacks
Telcoms
Transport
Xenotech
""".readLines()

    RangeMap rumors = new RangeMap()
            .with(1..5, 'Reckless with the lives of their employees')
            .with(6..10, 'Have a dark secret about their board of directors')
            .with(11..15, 'Notoriously xenophobic towards aliens')
            .with(16..20, 'Lost much money to an embezzler who evaded arrest')
            .with(21..26, 'Reliable and trustworthy goods')
            .with(27..31, 'Stole a lot of R&D from a rival corporation')
            .with(32..37, 'They have high-level political connections')
            .with(38..43, 'Rumored cover-up of a massive industrial accident')
            .with(44..49, 'Stodgy and very conservative in their business plans')
            .with(50..53, "${ -> pick('1d49', rumors)}") // entries 50-53 missing from table. just make it pick the lower half of entries
            .with(54..59, 'The company’s owner is dangerously insane')
            .with(60..64, 'Rumored ties to a eugenics cult')
            .with(65..70, 'Said to have a cache of pretech equipment')
            .with(71..76, 'Possibly teetering on the edge of bankruptcy')
            .with(77..81, 'Front for a planetary government’s espionage arm')
            .with(82..86, 'Secretly run by a psychic cabal')
            .with(87..91, 'Secretly run by hostile aliens')
            .with(92..95, 'Secretly run by an unbraked AI')
            .with(96..98, 'They’ve turned over a new leaf with the new CEO')
            .with(99..100, 'Deeply entangled with the planetary underworld')


    String template =  '''\
<h4>Corporation</h4>
{{name}} {{organization}}
<h5>Businesses:</h5>
&nbsp;&nbsp;{{business}}
<h5>Rumors:</h5>
&nbsp;&nbsp;{{rumor}}\
'''
    String generate() {
        Mustache.compiler().escapeHTML(false).compile(template).execute(
                [
                        name: pick(names),
                        organization: pick(organizations),
                        business:  pickN(businesses, roll('1d3+1')).join('<br/>&nbsp;&nbsp;'),
                        rumor: pickN(rumors, roll('1d3+1')).join('<br/>&nbsp;&nbsp;'),
                ]
        )
    }
}
