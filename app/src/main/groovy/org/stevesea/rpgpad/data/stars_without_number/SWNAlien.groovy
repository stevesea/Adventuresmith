/*
 * Copyright (c) 'Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 'of the License, or
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
class SWNAlien extends AbstractGenerator {
    // TODO: alien tech level
    // TODO: their political status in the sector
    // TODO: alien government motivatoin
    List<String> body_types = [
        'Humanlike',
        'Avian',
        'Reptilian',
        'Insectile',
        'Exotic',
        'Hybrid',
        ]
    List<String> lenses = [
        'Collectivity',
        'Curiosity',
        'Despair',
        'Domination',
        'Faith',
        'Fear',
        'Gluttony',
        'Greed',
        'Hate',
        'Honor',
        'Journeying',
        'Joy',
        'Pacifism',
        'Pride',
        'Sagacity',
        'Subtlety',
        'Tradition',
        'Treachery',
        'Tribalism',
        'Wrath',
        ]
    RangeMap social_structures = new RangeMap()
            .with(1,'Democratic')
            .with(2,'Monarchic')
            .with(3,'Tribal')
            .with(4,'Oligarchic')
            .with(5..6,'Multipolar') // Roll 1d6; on a 1-2 it has two, 3-4 indicates three, and 5-6 indicates
   // four. Roll on this table the requisite number of times to determine
   // the nature of these organizations.


    String template =  '''\
<h4>Alien</h4>
<strong><small>Body Type:</small></strong> {{body_type}}
<br/><strong><small>Lenses:</small></strong> {{lense}}
<br/>
<br/><strong><small>Social Structure:</small></strong> {{social_structure}}
'''
    String generate() {
        String structure = pick(social_structures)
        if (structure.equals('Multipolar')) {
            int num = new RangeMap()
                .with(1..2, 2)
                .with(3..4, 3)
                .with(5..6, 4).get(roll('1d6')) as int
            List<String> result = new ArrayList<>()
            num.times { result.add(pick('1d4', social_structures) as String)}
            structure = 'Multipolar: ' + result.join(', ')
        }
        String body_type = pick(body_types)
        if (body_type.equals('Hybrid')) {
            Set<String> allExceptHybrid = new HashSet<>()
            allExceptHybrid.addAll(body_types)
            allExceptHybrid.remove('Hybrid')

            // how many types is it a hybrid of?
            int num = new RangeMap()
                    .with(1..4, 2)
                    .with(5..6, 3).get(roll('1d6')) as int
            body_type = (pickN(allExceptHybrid, num) as List<String>).join(', ')
        }
        Mustache.compiler().compile(template).execute(
                [
                        body_type: body_type,
                        lense: pickN(lenses, roll('1d2+1')).join(', '),
                        social_structure: structure,
                ]
        )
    }
}
