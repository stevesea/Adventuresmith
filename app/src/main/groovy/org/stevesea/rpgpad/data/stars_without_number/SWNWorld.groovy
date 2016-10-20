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
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap
import org.stevesea.rpgpad.data.Shuffler

@CompileStatic
class SWNWorld extends AbstractGenerator {

    SWNnames names = new SWNnames()
    SWNpolitical_party political = new SWNpolitical_party()

    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        names.withShuffler(shuff)
        political.withShuffler(shuff)
        this
    }

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

    @Canonical
    public static class WorldTag {
        String flavor
        List<String> enemies
        List<String> friends
        List<String> complications
        List<String> things
        List<String> places
    }
    Map<String, WorldTag> tagMap = [
            'Abandoned Colony': new WorldTag(
                    flavor: '''''',
                    enemies: 'Crazed survivors, Ruthless plunderers of the ruins, Automated defense system'.tokenize(','),
                    friends: 'Inquisitive stellar archaeologist, Heir to the colony’s property, Local wanting the place cleaned out'.tokenize(','),
                    complications: 'The local government wants the ruins to remain a secret, The locals claim ownership of it, The colony is crumbling and dangerous to navigate'.tokenize(','),
                    things: 'Long-lost property deeds, Relic stolen by the colonists when they left, Historical record of the colonization attempt'.tokenize(','),
                    places: 'Decaying habitation block, Vine-covered town square, Structure buried by an ancient landslide'.tokenize(','),
            ),
'Alien Ruins' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Altered Humanity' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Area 51' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Badlands World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Bubble Cities' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Civil War' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Cold War' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Colonized Population' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Desert World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Eugenic Cult' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Exchange Consulate' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Feral World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Flying Cities' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Forbidden Tech' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Freak Geology' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Freak Weather' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Friendly Foe' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Gold Rush' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hatred' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Heavy Industry' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Heavy Mining' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hostile Biosphere' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Hostile Space' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Local Specialty' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Local Tech' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Major Spaceyard' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Minimal Contact' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Misandry/Misogyny' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Oceanic World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Out of Contact' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Outpost World' : new WorldTag(
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Perimeter Agency' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Pilgrimage Site' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Police State' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Preceptor Archive' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Pretech Cultists' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Primitive Aliens' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Fear' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Worship' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Psionics Academy' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Quarantined World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Radioactive World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Regional Hegemon' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Restrictive Laws' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Rigid Culture' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Seagoing Cities' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Sealed Menace' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Sectarians' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Seismic Instability' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Secret Masters' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Theocracy' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Tomb World' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Trade Hub' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Tyranny' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Unbraked AI' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Warlords' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Xenophiles' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Xenophobes' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
'Zombies' : new WorldTag(
        flavor: '''''',
        enemies: ''.tokenize(','),
        friends: ''.tokenize(','),
        complications: ''.tokenize(','),
        things: ''.tokenize(','),
        places: ''.tokenize(',')
),
    ]
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
Heavy Gravity
""".readLines()


    String template =  '''\
<strong>World</strong> - {{name}}
<br/>
<br/><strong><small>Atmosphere:</small></strong> {{atmosphere}}
<br/><strong><small>Temperature:</small></strong> {{temperature}}
<br/><strong><small>Biosphere:</small></strong> {{biosphere}}
<br/>
<br/><strong><small>Population:</small></strong> {{population}}
<br/><strong><small>Tech Level:</small></strong> {{techlevel}}
<br/>
<br/><strong><small>World Tags:</small></strong>
<br/>&nbsp;&nbsp;{{tags}}
<br/>
<br/><strong><small>Cultures:</small></strong> {{cultures}}
'''
    String generate() {
        // how many cultures are on the world?
        int numCultures = pick(new RangeMap()
                .with(1..3, 1)
                .with(4..5, 2)
                .with(6, 3)
        ) as int

        List<String> cultures = names.pickCultures(numCultures)
        // pick a name from the 1st culture in list
        String worldName = names.getPlaceName(cultures[0])

        List<String> worldTags = pickN(world_tags, 2) as List<String>

        Mustache.compiler().compile(template).execute(
                [
                        name: worldName,
                        cultures: cultures.join(', '),
                        atmosphere: pick('2d6', atmospheres),
                        temperature: pick('2d6', temperatures),
                        biosphere: pick('2d6', biospheres),
                        population: pick('2d6', populations),
                        techlevel: pick('2d6', techlevels),
                        tags: worldTags.join('<br/>&nbsp;&nbsp; '),
                ]
        )
    }
}
