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

import com.samskivert.mustache.Mustache
import org.stevesea.rpgpad.data.AbstractGenerator

class FourthPageMonster extends AbstractGenerator {

    Map<String,List<String>> natures = [
            'Animal and Plant': '''\
Its barbs are filled with poison.
You're entangled in its web.
They travel in packs.
It's hunting you.
Teeth and claws rend and tear.
The place is swarming with them.\
'''.readLines(),
            'Construct': '''\
They fight with mindless perseverance.
They repair their own injuries.
It's disguised as a mundane object.
You're walking through it's hallways.
It's immune to your usual tricks.
Its patterns are predictable.\
'''.readLines(),
            'Elemental': '''\
Fire burns your flesh.
You're blown away by air.
The earth shakes, rocks fall.
Water freezes and drowns you.
You're seared by blinding light.
Your world is plunged into darkness.\
'''.readLines(),
            'Humanoid': '''\
Their traps and nets ensnare you.
Their primitive magic is surprisingly effective.
They're simply misunderstood.
You've offended them.
They wield strange devices.
They're a motley crew.\
'''.readLines(),
            'Mythical Beast': '''\
Its breath is worse than its bite.
It's greater than the sum of its parts.
You've encroached on its lair.
Its hide is worth a fortune.
Its appearance is an ill omen.
Its magics are beyond your ken.\
'''.readLines(),
            'Undead': '''\
Their howls make you tremble in fear.
They remember their former lives.
Their touch drains your life force.
They just won't stay down.
They carry terrible disease.
They phase through solid objects.\
'''.readLines(),
            ]

    Map<String,List<String>> roles = [
            'Artillery': '''\
They shoot at you from cover.
You're caught in the blast radius.
They're aware of your approach.
They fall back when threatened.
You're trapped by suppressing fire.
They use unusual ammunition.\
'''.readLines(),
            'Brute': '''\
It knocks you down.
It gets in your way.
It goes berserk.
It tramples you.
It wrecks the place.
It cuts a wide swath.\
'''.readLines(),
            'Controller': '''\
Their charms confuse you.
The terrain turns treacherous.
It counters your magic.
It's all an illusion.
They surround you from all sides.
It disarms you.\
'''.readLines(),
            'Leader': '''\
He heals his allies.
She inspires her minions to fight.
His extended monologues are tedious.
It's master plan is inscrutable.
There is method to his madness.
She leads the charge.\
'''.readLines(),
            'Lurker': '''\
You don't see the sneak attack coming.
It blends in with the surroundings.
They dart just out of reach.
It's literally invisible.
They're actually enjoying this.
It waits for just the right moment.\
'''.readLines(),
            'Soldier': '''\
They're careful in their approach.
They're well-armed and well-armoured.
Their teamwork puts yours to shame.
They've been forced to fight.
They defend their weaker allies.
They outnumber you.\
'''.readLines(),
            ]

    @Override
    String generate() {
        String roleKey = pick(roles.keySet())
        String natureKey = pick(natures.keySet())

        String template = '''\
<strong>Nature:</strong>
<br/>&nbsp;&nbsp;<strong><small>{{natureKey}}</small></strong> - {{nature}}
<br/>
<br/><strong>Role:</strong>
<br/>&nbsp;&nbsp;<strong><small>{{roleKey}}</small></strong> - {{role}}\
'''
        Mustache.compiler().compile(template).execute(
                [
                        roleKey: roleKey,
                        natureKey: natureKey,
                        role: pick(roles.get(roleKey)),
                        nature: pick(natures.get(natureKey)),
                ]
        )
    }
}
