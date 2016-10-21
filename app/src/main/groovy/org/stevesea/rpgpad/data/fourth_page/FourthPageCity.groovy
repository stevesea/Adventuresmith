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

package org.stevesea.rpgpad.data.fourth_page

import org.stevesea.rpgpad.data.AbstractGenerator

class FourthPageCity extends AbstractGenerator {
    Map<String,List<String>> features = [
            'District' : '''\
The garrison is a well-oiled machine.
The docks are bustling at all hours.
The smell of garbage permeates the slums.
The merchants don't take kindly to loiterers.
No one goes into the abandoned quarter.
The university is accepting new students.\
'''.readLines(),
            'Resource' : '''\
The ore has almost run out.
There's good hunting here.
The soil is rich and fertile.
The river water is crystal clear.
You can buy hearty slaves here.
The air is charged with magic.\
'''.readLines(),
            'Terrain' : '''\
The salt-water rusts everything.
The buildings are in the treetops.
The canyon provides much protection.
There's nothing around for miles.
There are more canals than roads.
It's a long walk up the smokey mountain.\
'''.readLines(),
            ]
    Map<String,List<String>> populations = [
            'Attitude' : '''\
They want you gone as soon as possible.
They've learned to fear adventurers.
It's just another brawl between friends.
Your reputation precedes you.
Your old friend can't wait to see you.
They're curious to hear your stories.\
'''.readLines(),
            'Caste' : '''\
The nobles are meddling in your affairs.
You never know what the mages are up to.
The farmers have a job for you.
There's not enough work for the tradesmen.
The prisoners have been unruly lately.
The clergy watches your every move.\
'''.readLines(),
            'Race' : '''\
The elves barely hide their disdain.
Dwarven custom calls for hospitality.
The humans don't serve your kind here.
Lizardmen are rare in this part of the world.
The golems outnumber the people.
Uh oh. Gnomes.\
'''.readLines(),
    ]
    Map<String,List<String>> societies = [
            'Faith' : '''\
Your heathen ways will not be tolerated.
There's a different temple on every corner.
They're the last followers of a dead god.
There's yet another festival today.
The paladin has returned, bearing bad news.
Their god demands a sacrifice.\
'''.readLines(),
            'Guild' : '''\
The alchemists' services are in high demand.
There's no honour amongst these thieves.
Everyone has a favourite gladiator.
The adventurers' guild demands their share.
The blacksmiths make the finest weapons.
There's a sickness amongst the miners.\
'''.readLines(),
            'Ruler' : '''\
The young queen is stronger than she looks.
The despot's grasp is tenuous, at best.
The people are the creature's thralls.
Someone's paying off the mayor.
The mad wizard needs more test subjects.
No one's quite sure who's in charge.\
'''.readLines(),
    ]
    Map<String,List<String>> troubles = [
            'Rumour' : '''\
"There's treasure in those caves!"
"The sewer's crawling with rat-men!"
"There's a killer on the loose!"
"He's been brainwashed, I tell you!"
"The tournament was rigged!"
"This is all your fault!"\
'''.readLines(),
            'Threat' : '''\
The city can't survive this siege for long.
The sick have been quarantined.
A storm is coming. A very big one.
The dead are getting restless again.
The migration happens this year.
Something's disturbed the natives.\
'''.readLines(),
            'Unrest' : '''\
The workers are on strike.
The resistance needs your help.
The famine has hit some harder than others.
The balance of power has shifted.
They're preparing to go to war.
Racial tensions are running high.\
'''.readLines(),
    ]

    @Override
    String generate() {
        String featureKey = pick(features.keySet())
        String populationKey = pick(populations.keySet())
        String societyKey = pick(societies.keySet())
        String troubleKey = pick(troubles.keySet())

        """\
<h4>City</h4>
<h5>Feature</h5>
<strong><small>${featureKey}</small></strong> - ${pick(features.get(featureKey))}
<h5>Population<h5>
<strong><small>${populationKey}</small></strong> - ${pick(populations.get(populationKey))}
<h5>Society</h5>
<strong><small>${societyKey}</small></strong> - ${pick(societies.get(societyKey))}
<h5>Trouble</h5>
<strong><small>${troubleKey}</small></strong> - ${pick(troubles.get(troubleKey))}\
"""
    }
}
