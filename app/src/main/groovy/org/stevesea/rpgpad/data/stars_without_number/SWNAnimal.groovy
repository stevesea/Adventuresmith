/*
 * Copyright (c) Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version of the License, or
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
class SWNAnimal extends AbstractGenerator {
    Map<String, List<String>> templates_body_types = [
            Insectile: '''\
Sucking mouthparts
Chewing mouthparts
Jewel-colored chitin
2d4 pairs of limbs
Membranous wings
Deposits eggs in live prey
Has “silk” spinnerets
Has a chrysalis life stage
No mouth; adult form lives only to reproduce
1d4 pairs of eyes or eyespots
Sluglike body
Color-changing exoskeleton
Always encountered in groups
Hums or buzzes in intricate patterns
Lives in hives led by a queen
Killing one causes others nearby to go berserk
Emits powerful pheromones
Hides itself to ambush prey
Prefers subterranean environments
Emits noxious or poisonous stench when killed\
'''.readLines(),
            Reptilian: '''\
Sharp-edged scales
1d4 pairs of eyes
Extremely long tail
Bellowing vocalization
Burrowing foreclaws
Hide is damp and slimy
Eyeless
Strong swimmer
Spits venom
Lies in ambush in bodies of water
Brilliantly-hued scales or hide
Horns or body spikes
Large membranous frills
Hibernates in caves and undisturbed nooks
Glowing body parts
Body is patterned with both scales and hide
Springs on prey from elevated places
Warm-blooded
Furred
Limbless body\
'''.readLines(),
            Mammalian: '''\
Multiple mouths
Quill-like fur
Prehensile tail
Eyes or eyespots on body
Membranous wings
Stench glands
Peculiar vocalization
Marsupial pouch
Patterned fur or hide
Expands or inflates when threatened
Strictly nocturnal
1d6+1 limbs, including any tail
Mottled or mangy fur
Fires darts or quills
Animal is cold-blooded
Horns or body spikes
Superb scent tracker
Burrowing creature
Creature lacks a sense-hearing, sight, or smell
Creature is abnormally clever for an animal\
'''.readLines(),
            Avian: '''\
Sharp feathers
1d3 pairs of wings
Long, sinuous neck
Brilliant coloration
Membranous wings
Can hover
Beautiful song
Flightless
Fights prey on the ground
Launches secretions at prey
Lifts and drops prey
Exhales flame or other toxic substance
Always appears in groups
Long prehensile tail
Animal is cold-blooded
Fur instead of feathers
Scales instead of feathers
Toothed beak
Has valuable or delicious eggs
Flies by means of lighter-than-air gas\
'''.readLines(),
            Exotic: '''\
Rocklike body
1d4 pairs of eyestalks
Rolls on wheels
Chainsaw-like mouthparts or claws
Metallic hide
Natural laser emitters
Launches chemically-powered darts
Amoeba-like body
Crystalline tissues
Gas-sack body
2d10 tentacles
Gelatinous liquid body
Radioactive flesh
Uses sonic attacks to stun prey
Colony entity made up of numerous small animals
Controlled by neural symbiont
Absorbs electromagnetic energy
Precious mineral carapace or exoskeleton
Double damage from a particular type of injury
Mobile plant life\
'''.readLines(),
            Hybrid: [],
    ]

    String templateOut =  '''\
<strong>Animal</strong>
<br/><strong><small>Template:</small></strong> {{template}}
<br/>
<br/><strong><small>Trait or Body Variation:</small></strong>
<br/>&nbsp;&nbsp;{{trait_or_body}}
'''
    String generate() {
        Set<String> allExceptHybrid = new HashSet<>()
        allExceptHybrid.addAll(templates_body_types.keySet())
        allExceptHybrid.remove('Hybrid')

        String template = pick(templates_body_types.keySet())

        List<String> templates = new ArrayList<>()
        List<String> trait_or_body = new ArrayList<>()

        if (template.equals('Hybrid')) {
            // how many types is it a hybrid of?
            int num = new RangeMap()
                    .with(1..4, 2)
                    .with(5..6, 3).get(roll('1d6')) as int
            templates.addAll(pickN(allExceptHybrid, num) as List<String>)
        } else {
            templates.add(template)
        }
        templates.each { it ->
            // how many traits? at least 2, maybe 3
            trait_or_body.addAll(pickN(templates_body_types.get(it), roll('1d2+1')) as List<String>)
        }
        Mustache.compiler().escapeHTML(false).compile(templateOut).execute(
                [
                        template: templates.join(', '),
                        trait_or_body: trait_or_body.join('<br/>&nbsp;&nbsp;'),
                ]
        )

    }
}
