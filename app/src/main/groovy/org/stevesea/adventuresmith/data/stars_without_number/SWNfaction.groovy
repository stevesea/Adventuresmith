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
class SWNfaction extends AbstractGenerator {
    String faction=
    """
minor:
  stats:
  - 4
  - 3
  - 1
  assets:
  - 1
  - 1
  hit_points: 15
major:
  stats:
  - 6
  - 5
  - 3
  assets:
  - 2
  - 2
  hit_points: 29
hegemon:
  stats:
  - 8
  - 7
  - 5
  assets:
  - 4
  - 4
  hit_points: 49
tags:
- Colonists
- Deep Rooted
- Eugenics Cult
- Exchange Consulate
- Fanatical
- Imperialists
- Machiavellian
- Mercenary Group
- Perimeter Agency
- Pirates
- Planetary Government
- Plutocratic
- Preceptor Archive
- Psychic Academy
- Savage
- Scavengers
- Secretive
- Technical Expertise
- Theocratic
- Warlike
force:
  1:
  - Security Personnel
  - Hitmen
  - Militia Unit
  2:
  - Heavy Drop Assets
  - Elite Skirmishers
  - Hardened Personnel
  - Guerilla Populace
  3:
  - Zealots
  - Cunning Trap
  - Counterintel Unit
  4:
  - Beachhead Landers
  - Extended Theater
  - Strike Fleet
  - Postech Infantry
  5:
  - Blockade Fleet
  - Pretech Logistics
  - Psychic Assassins
  6:
  - Pretech Infantry
  - Planetary Defenses
  - Gravtank Formation
  7:
  - Deep Strike Landers
  - Integral Protocols
  - Space Marines
  8:
  - Capital Fleet
cunning:
  1:
  - Smugglers
  - Informers
  - False Front
  2:
  - Lobbyists
  - Saboteurs
  - Blackmail
  - Seductress
  3:
  - Cyberninjas
  - Stealth
  - Covert Shipping
  4:
  - Party Machine
  - Vanguard Cadres
  - Tripwire Cells
  - Seditionists
  5:
  - Organization Moles
  - Cracked Comms
  - Boltholes
  6:
  - Transport Lockdown
  - Covert Transit Net
  - Demagogue
  7:
  - Popular Movement
  - Book of Secrets
  - Treachery
  8:
  - Panopticon Matrix
wealth:
  1:
  - Franchise
  - Harvesters
  - Local Investments
  2:
  - Freighter Contract
  - Lawyers
  - Union Toughs
  - Surveyors
  3:
  - Postech Industry
  - Laboratory
  - Mercenaries
  4:
  - Shipping Combine
  - Monopoly
  - Medical Center
  - Bank
  5:
  - Marketers
  - Pretech Researchers
  - Blockade Runners
  6:
  - Venture Capital
  - R&D Department
  - Commodities Broker
  7:
  - Pretech Manufactory
  - Hostile Takeover
  - Transit Web
  8:
  - Scavenger Fleet


goals
  military conquest
  commercial expansion
  intelligence coup
  planetary seizure
  expand influence
  blood the enemy
  peacable kingdom
  destroy the foe
  inside enemy territory
  invincible valor
  wealth of worlds

    """

    String rules = """

hit points: max based on force/cunning/wealth

force raiting (1..8)
cunning 1..8
wealth 1..8

FacCreds
experience points
homeworld

assets

1 or more tags
    """

    String generate () {
        "unimplemented"
    }
}
