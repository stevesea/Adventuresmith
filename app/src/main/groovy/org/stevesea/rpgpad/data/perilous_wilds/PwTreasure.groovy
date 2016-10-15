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
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap
import org.stevesea.rpgpad.data.Shuffler

@CompileStatic
class PwTreasure extends AbstractGenerator{


    public static Map<String, PwTreasure> generators = [
            'item': new PwTreasure() {
                @Override
                String generate() {
                    return pick(getItem())
                }
            },
            'unguarded': new PwTreasure() {
                @Override
                String generate() {
                    List<Integer> rolls = [ roll('1d6'), roll('1d6') ]
                    Integer rollMin = Collections.min(rolls)
                    if (rollMin.equals(6)) {
                        // if not double-sixes, use smaller roll
                        return getTreasure().getAt(rollMin)
                    } else {
                        return getTreasure().getAt(roll('3d6'))
                    }
                }
            },
            ]
    PwDetails pwDetails = new PwDetails()
    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwDetails.withShuffler(shuff)
        this
    }

    RangeMap utility_item = new RangeMap()
            .with(1, 'key/lockpick')
            .with(2, 'potion/food')
            .with(3, 'clothing/cloak')
            .with(4, 'decanter/vessel/cup')
            .with(5, 'cage/box/coffer')
            .with(6, 'instrument/tool')
            .with(7, 'book/scroll')
            .with(8, 'weapon/staff/wand')
            .with(9, 'armor/shield/helm')
            .with(10, 'mirror/hourglass')
            .with(11, 'pet/mount')
            .with(12, 'device/construct')

    RangeMap art_item = new RangeMap()
            .with(1, 'trinket/charm')
            .with(2, 'painting/pottery')
            .with(3, 'ring/gloves')
            .with(4, 'carpet/tapestry')
            .with(5, 'statuette/idol')
            .with(6, 'flag/banner')
            .with(7, 'bracelet/armband')
            .with(8, 'necklace/amulet')
            .with(9, 'belt/harness')
            .with(10, 'hat/mask')
            .with(11, 'orb/sigil/rod')
            .with(12, 'crown/scepter')

    RangeMap item = new RangeMap()
            .with(1..8, "${ -> pick(utility_item)}")
            .with(9..12, "${ -> pick(art_item)}")

    RangeMap treasure = new RangeMap()
            .with(1, """\
A few coins, 2d8 or so
&nbsp;&nbsp;${ -> small('[' + roll('2d8') + ']') }\
""")
            .with(2, """\
A useful item
<br/>&nbsp;&nbsp;${-> small(pick(utility_item))}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(3, """\
Several coins, about 4d10
<br/>&nbsp;&nbsp;${-> small('[' + roll('4d10') + ']')}\
""")
            .with(4, """\
A small valuable (gem, art), worth 2d10x10 coins, 0 weight
<br/>&nbsp;&nbsp;${-> small('[' + (roll('2d10') * 10) + ']')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(5, """\
Some minor magical trinket
<br/>&nbsp;&nbsp;${-> small(pick(item))}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Ability:')} ${-> pwDetails.pickAbility()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Magic Type:')} ${-> pwDetails.pickMagicType()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(6, """
Useful clue (map, note, etc.)
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(7, """\
Bag of coins, 1d4x100, 1 weight per 100
<br/>&nbsp;&nbsp;${-> small('[' + (roll('1d4') * 100) + ']')}\
""")
            .with(8, """\
A small item (gem, art) of great value (2d6x100 coins, 0 weight)
<br/>&nbsp;&nbsp;${-> small(pick(art_item))}
<br/>&nbsp;&nbsp;${-> small('[' + (roll('2d6') * 100) + ']')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(9, """\
A chest of coins and other small valuables. 1 weight, worth 3d6x100 coins.
<br/>&nbsp;&nbsp;${-> small('[' + (roll('3d6') * 100) + ']')}\
""")
            .with(10, """\
A magical item or magical effect
<br/>&nbsp;&nbsp;${-> small(pick(item))}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Ability:')} ${-> pwDetails.pickAbility()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Magic Type:')} ${-> pwDetails.pickMagicType()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(11, """\
Many bags of coins, 2d4x100 or so
<br/>&nbsp;&nbsp;${-> small('[' + (roll('2d4') * 100) + ']')}\
""")
            .with(12, """\
A sign of office (crown, banner) worth at least 3d4x100 coins
<br/>&nbsp;&nbsp;${-> small('[' + (roll('3d4') * 100) + ']')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(13, """\
A large art item (4d4x100 coins, 1 weight)
<br/>&nbsp;&nbsp;${-> small(pick(art_item))}
<br/>&nbsp;&nbsp;${-> small('[' + (roll('4d4') * 100) + ']')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(14, """\
Unique item worth at least 5d4x100 coins
<br/>&nbsp;&nbsp;${-> small(pick(item))}
<br/>&nbsp;&nbsp;${-> small('[' + (roll('5d4') * 100) + ']')}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Adjective:')} ${-> pwDetails.pickAdjective()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}\
""")
            .with(15, """\
Everything needed to learn a new spell.
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Ability:')} ${-> pwDetails.pickAbility()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Magic Type:')} ${-> pwDetails.pickMagicType()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}
<br/>
<br/>${-> pick('1d12', treasure)}\
""")
            .with(16, """\
A portal or secret path (or directions to one)
<br/>
<br/>${ -> pick('1d12', treasure)}\
""")
            .with(17, """\
Something relating to one of the characters
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Age:')} ${-> pwDetails.pickAge()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Aspect:')} ${-> pwDetails.pickAspect()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Element:')} ${-> pwDetails.pickElement()}
<br/>&nbsp;&nbsp;&nbsp;&nbsp;${ssem('Oddity:')} ${-> pwDetails.pickOddity()}
<br/>
<br/>${-> pick('1d12', treasure)}\
""")
            .with(18, """\
A hoard: 1d10x1000 coins and 1d10x10 gems worth 2d6x100 each
<br/>&nbsp;&nbsp; coins: ${-> (roll('1d10') * 1000)}
<br/>&nbsp;&nbsp; gems: ${-> (roll('1d10') * 10) + ' worth ' + (roll('2d6') * 100) + ' each'}\
""")

    @Override
    String generate() {
        pick(treasure)
    }
}
