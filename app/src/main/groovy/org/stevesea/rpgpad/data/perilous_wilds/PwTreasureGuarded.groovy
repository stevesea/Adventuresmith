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

package org.stevesea.rpgpad.data.perilous_wilds

import groovy.transform.CompileStatic

import javax.inject.Inject;

@CompileStatic
class PwTreasureGuarded extends PwTreasure {
    @Inject
    PwTreasureGuarded(PwDetails pwDetails) {
        super(pwDetails)
    }

    @Override
    String generate() {
        generateForBonus(0)
    }
    Integer rollWithBonus(String damageDice, Integer bonus) {
        int result = roll(damageDice)
        if (bonus > 0) {
            result += roll(bonus + 'd4')
        }
        return result
    }

    String generateForBonus(Integer bonuses) {
        """\
${strong('damage die - D4' + "${bonuses == 0 ? '' : ' + ' + bonuses + 'd4'}")}
<br/>${ -> treasure.get(rollWithBonus('1d4', bonuses))}
<br/>
<br/>${strong('damage die - D6' + "${bonuses == 0 ? '' : ' + ' + bonuses + 'd4'}")}
<br/>${ -> treasure.get(rollWithBonus('1d6', bonuses))}
<br/>
<br/>${strong('damage die - D8' + "${bonuses == 0 ? '' : ' + ' + bonuses + 'd4'}")}
<br/>${ -> treasure.get(rollWithBonus('1d8', bonuses))}
<br/>
<br/>${strong('damage die - D10' + "${bonuses == 0 ? '' : ' + ' + bonuses + 'd4'}")}
<br/>${ -> treasure.get(rollWithBonus('1d10', bonuses))}
<br/>
<br/>${strong('damage die - D12' + "${bonuses == 0 ? '' : ' + ' + bonuses + 'd4'}")}
<br/>${ -> treasure.get(rollWithBonus('1d12', bonuses))}\
"""
    }
}
