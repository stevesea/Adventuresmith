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

import com.samskivert.mustache.Mustache
import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.RangeMap;

@CompileStatic
class SWNnpc extends AbstractGenerator {
    List<String> genders = ['Male', 'Female']
    RangeMap ages = new RangeMap()
            .with(1..2, 'Young')
            .with(3..4, 'Middle-aged')
            .with(5..6, 'Old')

    RangeMap heights = new RangeMap()
            .with(1, 'Very Short')
            .with(2..3, 'Short')
            .with(4..5, 'Average height')
            .with(6..7, 'Tall')
            .with(8, 'Very Tall')

    List<String> problems = """\
Grudge against local authorities.
Has a secret kept from their family.
Chronic illness
Enmity of a local psychic
Has enemies at work
Owes loan sharks
Threatened with loss of spouse, sibling, or child
Close relative in trouble with the law
Drug or behavioral addict
Blackmailed by enemy\
""".readLines()
    List<String> job_motivations = """\
Greed, because nothing else they can do pays better
Idealistic about the job
Sense of social duty
Force of habit takes them through the day
Seeks to please another
Feels inadequate as anything else
Family tradition
Religious obligation or vow
Nothing better to do, and they need the money
They’re quitting at the first good opportunity
It’s a stepping stone to better things
Spite against an enemy discomfited by the work\
""".readLines()
    List<String> quirks = """\
Bald
Terrible taste in clothing
Very thin
Powerful build
Bad eyesight, wears spectacles
Carries work tools constantly
Long hair
Bearded, if male. Ankle-length hair if female.
Scars all over hands
Missing digits or an ear
Smells like their work
Repeats himself constantly
Talks about tabloid articles
Booming voice
Vocal dislike of off worlders
Always snuffling
Missing teeth
Fastidiously neat
Wears religious emblems
Speaks as little as possible\
""".readLines()


    String template =  '''\
<strong>NPC</strong> - {{gender}}
<br/><strong><small>Age:</small></strong> {{age}} &nbsp;&nbsp;<strong><small>Height:</small></strong> {{height}}
<br/>
<br/><strong><small>Job Motivation:</small></strong> {{motivation}}
<br/>
<br/><strong><small>Problems:</small></strong> {{problem}}
<br/>
<br/><strong><small>Quirks:</small></strong> {{quirk}}
'''
    String generate() {
        Mustache.compiler().compile(template).execute(
                [
                        gender: pick(genders),
                        age: pick(ages),
                        height: pick(heights),
                        problem: pick(problems),
                        motivation: pick(job_motivations),
                        quirk: pick(quirks),
                ]
        )
    }
}
