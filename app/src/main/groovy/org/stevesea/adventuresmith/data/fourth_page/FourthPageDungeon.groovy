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

package org.stevesea.adventuresmith.data.fourth_page

import org.stevesea.adventuresmith.data.AbstractGenerator

class FourthPageDungeon extends AbstractGenerator {
    Map<String,List<String>> histories = [
            'Builder' : '''\
The walls show signs of dwarven craftsmanship.
They were far more advanced than you.
These tunnels weren't cut. They were burrowed.
It must have taken thousands of slaves.
They tunnelled up from below.
Most elves wouldn't live underground.\
'''.readLines(),
            'Downfall' : '''\
Their hubris angered the gods.
The war left the land in ruins.
The walls crumbled against the forces of nature.
This place was simply forgotten.
The experiment went terribly wrong.
The flood has only now receded.\
'''.readLines(),
            'Purpose' : '''\
The vault held the kingdom's riches.
Murderers were imprisoned here.
The mine may still hold riches.
They sought protection from the storms.
This was once a hero's tomb.
The hatchery was carefully guarded.\
'''.readLines(),
    ]
    Map<String,List<String>> denizens = [
            'Community' : '''\
It's a perfect hideout for the outlaws.
The cult must perform their rituals.
One of the local clans has settled here.
The refugees have no other home.
The monks require peace and solitude.
You've just stepped in goblin dung.\
'''.readLines(),
            'Creature' : '''\
The hive never ceases its work.
A dragon has made this its lair.
They're in hibernation. For now.
An abomination lurks below.
The dead still guard this place.
The ooze spreads from a dark heart.\
'''.readLines(),
            'Hermit' : '''\
An exiled king rules this place.
The mad adventurer has lost his way.
The wraith wants only release.
She is the last of the giants.
The oracle would like a word with you.
The necromancer's soul is bound here.\
'''.readLines(),
    ]
    Map<String,List<String>> trials = [
            'Dilemma' : '''\
She's just a child.
You can only save one.
You must negotiate with them.
You're running out of time.
To proceed, you must solve the puzzle.
Your freedom comes with a price.\
'''.readLines(),
            'Hazard' : '''\
Crossing the raging river won't be easy.
The air is filled with poison spores.
You must navigate the labyrinth.
The bridge has seen much better days.
There are very few handholds on the cliff face.
The floor is lava.\
'''.readLines(),
            'Trap' : '''\
The water is rising fast.
You feel unnaturally weak.
Your lights are extinguished.
This gauntlet was designed by a mad man.
The way out is blocked by a cave in.
That statue just moved.\
'''.readLines(),
    ]
    Map<String,List<String>> secrets = [
            'Cache' : '''\
The library is full of ancient texts.
There's more gold here than you can carry.
The relic has been here all along.
There's something gleaming in the armoury.
The eggs won't survive on their own.
They've been keeping hostages.\
'''.readLines(),
            'Lore' : '''\
The scroll describes an object of great power.
There's something strange about this map.
The prophecy is coming true.
History was written by the victors.
These plans are incomplete.
The portal leads to undiscovered lands.\
'''.readLines(),
            'Revelation' : '''\
You weren't meant to find these documents.
They're the victims, here.
This shipment never went out.
You're too late to stop the invasion.
The duke's been involved for years.
Something terrible has returned.\
'''.readLines(),
    ]

    @Override
    String generate() {
        String historyKey = pick(histories.keySet())
        String denizenKey = pick(denizens.keySet())
        String trialKey = pick(trials.keySet())
        String secretKey = pick(secrets.keySet())

        """
<h4>Dungeon</h4>
<h5>History</h5>
<strong><small>${historyKey}</small></strong> - ${pick(histories.get(historyKey))}
<h5>Denizen</h5>
<strong><small>${denizenKey}</small></strong> - ${pick(denizens.get(denizenKey))}
<h5>Trial</h5>
<strong><small>${trialKey}</small></strong> - ${pick(trials.get(trialKey))}
<h5>Secret</h5>
<strong><small>${secretKey}</small></strong> - ${pick(secrets.get(secretKey))}\
"""
    }
}
