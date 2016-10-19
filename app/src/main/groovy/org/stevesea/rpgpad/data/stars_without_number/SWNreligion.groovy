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

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap

@CompileStatic
class SWNreligion extends AbstractGenerator {

    RangeMap evolutions = new RangeMap()
            .with(1, '<em>New holy book</em>; someone in the faith\'s past penned or discovered a text that is now taken to be holy writ and the expressed will of the divine.')
            .with(2, '<em>New prophet</em>; this faith reveres the words and example of a relatively recent prophet, esteeming him or her as the final word on the will of God. The prophet may or may not still be living.')
            .with(3, '<em>Neofundamentalism</em>; the faith is fiercely resistant to perceived innovations and deviations from their beliefs. Even extremely onerous traditions and restrictions will be observed to the letter.')
            .with(4, '<em>Quietism</em>; the faith shuns the outside world and involvement with the affairs of nonbelievers. They prefer to keep their own kind and avoid positions of wealth and power.')
            .with(5, '<em>Sacrifices</em>; the faith finds it necessary to make substatial sacrifices to please God. Some faiths may go so far as to offer human sacrifices, while others insist on huge tithes offered to the building of religious edifices.')
            .with(6, '<em>Schism</em>; the faith\'s beliefs are actually almost identical to those of the majority of its origin tradition, save for a few minor points of vital interest to theologians and no practical difference whatsoever to believers. This does not prevent a burning resentment towards the parent faith.')
            .with(7, '<em>Holy family</em>; God\'s favor has been shown especially to members of a particular lineage. It may be that only men and women of this bloodline are permitted to become clergy, or they may serve as helpless figureheads for the real leaders of the faith.')
            .with(8, "<em>Syncretism</em>; The faith has merged many of its beliefs with another religion [${ -> pick(origin_traditions.keySet())}]. this faith has reconciled the major elements of both beliefs into its tradition.")

    RangeMap leaderships_short = new RangeMap()
            .with(1..2, '<em>Patriarch/Matriarch</em>')
            .with(3..4, '<em>Council</em>')
            .with(5, '<em>Democracy</em>')
            .with(6, 'No hierarchy')
    RangeMap leaderships = new RangeMap()
            .with(1..2, '<em>Patriarch/Matriarch</em>; a single leader determines doctrine for the entire religion, possibly in consultation wiht other clerics.')
            .with(3..4, '<em>Council</em>; a group of the oldest and most revered clergy determine the course of the faith.')
            .with(5, '<em>Democracy</em>; every member has an equal voice in matters of faith, with doctrine usually decided at regular church-wide councils.')
            .with(6, "No universal leadership; Each region governs itself via [${ -> pick(leaderships_short)}]")

    Map<String, List<String>> origin_traditions = [
        Paganism: 'Druid Witch Priest Shaman Pontifex Flamen Vestal Teacher Gothi Entu Presbyteros'.tokenize(),
        'Roman Catholicism': 'Pope Cardinal Archbishop Bishop Priest Deacon Abbot/Abbess Monk Nun'.tokenize(),
        'Eastern Orthodox Christianity': 'Patriarch Metropolitan Archbishop Bishop Archimandrite Archpriest Priest Deacon Monk Nun'.tokenize(),
        'Protestant Christianity': 'Archbishop Bishop Pastor Preacher Reverend Elder Priest Minister'.tokenize(),
        Buddhism: 'Rinpoche Lama Bhikku/Bhikkuni'.tokenize(),
        Judaism: 'Rabbi Cantor Rebbe'.tokenize(),
        Islam: 'Imam Mufti Qadi Sheikh Mullah Ayatollah Caliph Amir Sayid'.tokenize(),
        Taoism: 'Master Monk Nun Sage'.tokenize(),
        Hinduism: 'Guru Satguru Mahatama Maharishi Swami Yogi/Yogini'.tokenize(),
        Zoroastrianism: [],
        Confucianism: [],
        Ideology: 'Chairman Dear Leader Commissar Professor Teacher President Cadre'.tokenize(),
]



    String generate () {
        // TODO: show titles?
        String origin = pick(origin_traditions.keySet())
        """\
<strong>Religion</strong> &nbsp;&nbsp; <strong><small>Origin Tradition:</small></strong> ${origin}
<br/>
<br/><strong><small>Evolution:</small></strong>
<br/>&nbsp;&nbsp;${pick(evolutions)}
<br/>
<br/><strong><small>Leadership:</small></strong>
<br/>&nbsp;&nbsp;${pick(leaderships)}\
"""
    }
}
