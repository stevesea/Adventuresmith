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

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator

@CompileStatic
class SWNroom_dressing extends AbstractGenerator {
    List<String> room_dressing = [
        'Armory: locked gun cabinets, armor racks',
        'Art studio: half-finished pieces, tools',
        'Balcony: flowers, crumbling vines',
        'Ballroom: musical instruments, decorations',
        'Barracks: footlockers, stacked bunks',
        'Bath chamber: large bathing pool, steam rooms',
        'Bedroom: locked cabinets, personal mementos',
        'Biotech lab: unfinished experiments, toxins',
        'Broadcasting stage: holocams, props',
        'Cellar: mold, dampness, spiderwebs on shelves',
        'Cold storage: haunches of alien meat',
        'Computer room: flickering servers, vent fans',
        'Council chamber: large table, mapboard, records',
        'Crypt: sarcophagi, bones, grave goods',
        'Dining room: long tables, epergnes, portraits',
        'Dormitory: bunks, dividers, common baths',
        'Engineering workshop: parts, electricity, scrap',
        'Game room: table games, nets, flashing baubles',
        'Garden: benches, fountains, exotic foliage',
        'Great hall: large hearth, tables, raised dais',
        'Greenhouse: vegetables, irrigation systems, insects',
        'Icon room: religious paintings, statues, kneelers',
        'Kennel: stink, fur, bones, feeding bowls',
        'Kitchen: boiling pots, ovens, numerous knives',
        'Lavatory: sonic showers, nonhuman facilities',
        'Lecture hall: podium, seats, holoboard',
        'Library: scrolls, dataslabs, codices, massive folios',
        'Lumber room: spare furniture, dropcloths, dust',
        'Maintenance closet: mops, cleaning solvents, sinks',
        'Medical clinic: empty sprayhypos, antiseptic smell',
        'Monitoring center: banks of screens, darkness',
        'Mortuary: emblaming tools, canopic jars',
        'Museum: display cases, plaques, audio explanations',
        'Nursery: toys, brightly-painted walls, cribs',
        'Operating theater: overhead lights, tables, scalpels',
        'Pantry: dusty cannisters, sacks of grain',
        'Prayer room: icons, kneelers, washbasins',
        'Prison cell: mold, vermin, peeling paint',
        'Quarantine room: cot, bedpan, handwritten will',
        'Solarium: transparent aluminum ceiling, plants',
        'Sparring room: floor mats, practice weapons',
        'Storeroom: crates, bales, cabinets, chests',
        'Theater: costumes, stage, secret machinery',
        'Torture chamber: straps, chemicals, recording gear',
        'Trophy room: xenolife body parts, plaques, chairs',
        'Unfinished room: bare wiring, stacked paneling',
        'Vault: cameras, thick doors, timed locks',
        'Vestibule: coat racks, shoe rests, matting',
        'Wardrobe: out-of-date clothing, mirrors, shoes',
        'Wellhouse: water filters, tanks, heaters',
]


    String generate () {
        pick(room_dressing)
    }
}
