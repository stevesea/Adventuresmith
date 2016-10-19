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

@CompileStatic
class SWNheresy extends AbstractGenerator {
    List<String> founder = [
            '<em>Defrocked clergy:</em> founded by a cleric outcast from the faith',
            '<em>Frustrated layman:</em> founded by a layman frustrated with the faith\'s decadence',
            '<em>Renegade prophet:</em> founded by a revered holy figure who broke with the faith',
            '<em>High prelate:</em> founded by an important and powerful cleric to convey his or her beliefs',
            '<em>Dissatisfied minor clergy:</em> founded by a minor cleric frustrated with the faith\'s current condition',
            '<em>Outsider:</em> founded by a member of another faith deeply influenced by the parent religion',
            '<em>Academic:</em> founded by a professor or theologian on intellectual grounds',
            '<em>Accidental:</em> the founder never meant their works to be taken that way',
    ]
    List<String> major_heresy = [
            '<em>Manichaeanism:</em> the sect believes in harsh austerities and rejection of matter as something profane and evil',
            '<em>Donatism:</em> the sect believes that clergy must be personally pure and holy in order to be legitimate',
            '<em>Supercessionism:</em> the sect believes the founder or some other source supercedes former scripture or tradition',
            '<em>Antinomianism:</em> the sect believes that their holy persons are above any law and may do as they will',
            '<em>Universal priesthood:</em> the sect believes that there is no distinction between clergy and layman and that all functions of the faith may be performed by all members',
            '<em>Conciliarism:</em> the sect believes that the consensus of believers may correct or command even the clerical leadership of the faith',
            '<em>Ethnocentrism:</em> the sect believes that only a particular ethnicity or nationality can truly belong to the faith',
            '<em>Separatism:</em> the sect believes members should shun involvement with the secular world',
            '<em>Stringency:</em> the sect believes that even minor sins should be punished, and major sins should be capital crimes',
            '<em>Syncretism:</em> the sect has added elements of another native faith to their beliefs',
            '<em>Primitivism:</em> the sect tries to recreate what they imagine was the original community of faith',
            '<em>Conversion by the sword:</em> unbelievers must be brought to obedience to the sect or be granted death',
    ]
    List<String> attitude = [
            '<em>Filial:</em> the sect honors and respects the orthodox faith, but feels it is substantially in error',
            '<em>Anathematic:</em> the orthodox are spiritually worse than infidels, and their ways must be avoided at all costs',
            '<em>Evangelical:</em> the sect feels compelled to teach the orthodox the better truth of their ways',
            '<em>Contemptuous:</em> the orthodox are spiritually lost and ignoble',
            '<em>Aversion:</em> the sect wishes to shun and avoid the orthodox',
            '<em>Hatred:</em> the sect wishes the death or conversion of the orthodox',
            '<em>Indifference:</em> the sect has no particular animus or love for the orthodox',
            '<em>Obedience:</em> the sect feels obligated to obey the orthodox hierarchy in all matters not related to their specific faith',
            '<em>Legitimist:</em> the sect views itself as the "true" orthodox faith and the present orthodox hierarchy as pretenders to their office',
            '<em>Purificationist:</em> the sect\'s austerities, sufferings, and asceticisms are necessary to purify the orthodox',
    ]
    List<String> quirks = """\
Clergy of only one gender
Dietary prohibitions
Characteristic item of clothing or jewelry
Public prayer at set times or places
Forbidden to do something commonly done
Anti-intellectual, deploring secular learning
Mystical, seeking union with God through meditation
Lives in visibly distinct houses or districts
Has a language specific to the sect
Greatly respects learning and education
Favors specific colors or symbols
Has unique purification rituals
Always armed
Forbids marriage or romance outside the sect
Will not eat with people outside the sect
Must donate labor or tithe money to the sect
Special friendliness toward another faith or ethnicity
Favors certain professions for their membership
Vigorous charity work among unbelievers
Forbidden the use of certain technology\
""".readLines()



    String generate () {
        """\
<strong>Heresy</strong>
<br/>
<br/><strong><small>Founder:</small></strong>
<br/>&nbsp;&nbsp;${pick(founder)}
<br/>
<br/><strong><small>Major Heresy:</small></strong>
<br/>&nbsp;&nbsp;${pick(major_heresy)}
<br/>
<br/><strong><small>Attitude towards orthodoxy:</small></strong>
<br/>&nbsp;&nbsp;${pick(attitude)}
<br/>
<br/><strong><small>Quirks:</small></strong>
<br/>&nbsp;&nbsp;${pickN(quirks, roll('1d2+1')).join('<br/>&nbsp;&nbsp;')}
"""
    }
}
