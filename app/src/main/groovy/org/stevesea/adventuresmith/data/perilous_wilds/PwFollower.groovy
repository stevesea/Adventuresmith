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

package org.stevesea.adventuresmith.data.perilous_wilds

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.RangeMap
import org.stevesea.adventuresmith.data.Shuffler

@CompileStatic
class PwFollower extends AbstractGenerator{
    PwNPC pwNPC = new PwNPC()
    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwNPC.withShuffler(shuff)
        this
    }

    int qual=0
    int loy=0
    int tagCount=0
    Set<?> calculatedTags = new TreeSet<>()

    @Override
    String generate() {
        qual = 0
        loy = Integer.parseInt((String)pick(loyalty))
        tagCount = 0
        calculatedTags.clear()

        calculatedTags.add("?-Wise")

        def result = """\
${strong('Follower')}
<br/>
<br/>${ss('Competence:')} ${pick(competence)}
<br/>${ss('Background:')} ${pick(background)}
<br/>${ss('Tags:')}
<br/>&nbsp;&nbsp;${ -> calculatedTags.addAll(pickN(tags, tagCount)) ; calculatedTags.join(',<br/>&nbsp;&nbsp;')}
<br/>${ss('Quality:')} ${ -> qual}&nbsp;&nbsp;${ss('Loyalty:')} ${ -> loy}&nbsp;&nbsp;${ss('Cost:')} ${pick(cost)}
<br/>
<br/>${ss('Instinct:')} ${pick(instinct)}
<br/>
<br/>${ss('Hit Points:')} ${pick(hit_points)}
<br/>${ss('Damage Die:')} ${pick(damage_die)}
<br/>
${ pwNPC.genTraits()}\
"""
    }

    RangeMap competence = new RangeMap()
            .with(1..3, "A liability${ -> qual-=1; ''}")
            .with(4..9, "Competent${ -> tagCount+=1 ; ''}")
            .with(10..11, "Fully capable${ -> qual+=1 ; tagCount+=2; ''}")
            .with(12, "Exceptional${ -> qual+=2 ; tagCount+=4; ''}")

    RangeMap background = new RangeMap()
            .with(1..2, "Life of servitude/oppression${ -> calculatedTags.add('Meek'); ''}")
            .with(3, "Past their prime${ -> qual-=1; calculatedTags.add('?-Wise+'); ''}")
            .with(4..5, "Has lived a life of danger${ -> tagCount+=2; ''}")
            .with(6..9, "Unremarkable")
            .with(10, "Has lived a life of privilege ${ -> tagCount+=1; ''}")
            .with(11, "Specialist (${-> pick(pwNPC.specialist)}) ${ -> qual+=1 ; tagCount-=2; ''}")
            .with(12, "${ -> pickN(background, 2).join(', ')}")
    RangeMap loyalty = new RangeMap()
            .with(1..2, '0')
            .with(3..10, '1')
            .with(11..12, '2')
    RangeMap instinct = new RangeMap()
            .with(1, "Loot, pillage, and burn")
            .with(2, "Hold a grudge and seek payback")
            .with(3, "Question leadership or authority")
            .with(4..5, "Lord over others")
            .with(6..7, "Act impulsively")
            .with(8..9, "Give in to temptation")
            .with(10..11, "Slack off")
            .with(12, "Avoid danger or punishment")

    RangeMap cost = new RangeMap()
            .with(1, "Debauchery")
            .with(2, "Vengeance")
            .with(3..5, "Lucre")
            .with(6..7, "Renown")
            .with(8..9, "Glory")
            .with(10, "Affection")
            .with(11, "Knowledge")
            .with(12, "Good")

    RangeMap hit_points = new RangeMap()
            .with(1..3, '3HP (weak/frail/soft)')
            .with(4..9, '6HP (able-bodied)')
            .with(10..12, '9HP (tough/strong/hard)')
    RangeMap damage_die = new RangeMap()
            .with(1..4, "d4 (Not very dangerous)")
            .with(5..10, "d6 (Can defend themselves)")
            .with(11..12, "d8 (Veteren fighter)")


    List<String> tags ="""\
Archer
Athletic
Beautiful
Cautious
Connected (?pick steading/group)
Cunning
Devious
Group
Guide
Hardy
Healer
Meek
Magical
Organized
Self-sufficient
Stealthy
Warrior\
""".readLines()
}
