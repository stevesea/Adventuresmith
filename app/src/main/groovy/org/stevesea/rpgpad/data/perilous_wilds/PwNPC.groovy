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
class PwNPC extends AbstractGenerator {

    public static Map<String, PwNPC> generators = [
            'rural': new PwNPC() {
                @Override
                String generate() {
                    return genNpcInfo('Rural', getRural())
                }
            },
            'urban': new PwNPC() {
                @Override
                String generate() {
                    return genNpcInfo('Urban', getUrban())
                }
            },
            'wilderness': new PwNPC() {
                @Override
                String generate() {
                    return genNpcInfo('Wilderness', getWilderness())
                }
            }
    ]
    PwDetails pwDetails = new PwDetails()

    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        pwDetails.withShuffler(shuff)
        this
    }
    @Override
    String generate() {
        pick(context)
    }

    String genNpcInfo(String contextName, RangeMap context) {
        """\
${strong("NPC: ${contextName}")}
<br/>
<br/>${ss('Occupation:')} ${ -> pick(context)}
<br/>${ss('Activity:')} ${ -> pwDetails.pickActivity()}
<br/>${ss('Alignment:')} ${ -> pwDetails.pickAlignment()}
<br/>${ -> genTraits()}\
"""
    }

    RangeMap context = new RangeMap()
            .with(1..3, "${ -> genNpcInfo('Wilderness', wilderness)}")
            .with(4..9, "${ -> genNpcInfo('Rural', rural)}")
            .with(10..12, "${ -> genNpcInfo('Urban', urban)}")

    RangeMap criminal = new RangeMap()
            .with(1, 'bandit/brigand/thug')
            .with(2, 'bandit/brigand/thug')
            .with(3..4, 'thief')
            .with(5..6, 'bodyguard/tough')
            .with(7..8, 'burglar')
            .with(9, 'dealer/fence')
            .with(10, 'racketeer')
            .with(11, 'lieutenant')
            .with(12, 'boss')

    RangeMap commoner = new RangeMap()
            .with(1, 'housewife/husband')
            .with(2..3, 'hunter/gatherer')
            .with(4..6, 'farmer/herder')
            .with(7..8, 'laborer/servant')
            .with(9, 'driver/porter/guide')
            .with(10, 'sailor/soldier/guard')
            .with(11, 'clergy/monk')
            .with(12, 'apprentice/adventurer')


    RangeMap tradesperson = new RangeMap()
            .with(1, 'cobbler/furrier/tailor')
            .with(2, 'weaver/basketmaker')
            .with(3, 'potter/carpenter')
            .with(4, 'mason/baker/chandler')
            .with(5, 'cooper/wheelwright')
            .with(6, 'tanner/ropemaker')
            .with(7, 'smith/tinker')
            .with(8, 'stablekeeper/herbalist')
            .with(9, 'vintner/jeweler')
            .with(10, 'innkeeper/tavernkeeper')
            .with(11, 'artist/actor/minstrel')
            .with(12, 'armorer/weaponsmith')

    RangeMap merchant = new RangeMap()
            .with(1..3, 'general goods/outfitter')
            .with(4, 'raw materials')
            .with(5, 'grain/livestock')
            .with(6, 'ale/wine/spirits')
            .with(7, 'clothing/jewelry')
            .with(8, 'weapons/armor')
            .with(9, 'spices/tobacco')
            .with(10, 'labor/slaves')
            .with(11, 'books/scrolls')
            .with(12, 'magic supplies/items')

    RangeMap specialist = new RangeMap()
            .with(1, 'undertaker')
            .with(2, 'sage/scholar/wizard')
            .with(3, 'writer/illuminator')
            .with(4, 'perfumer')
            .with(5, 'architect/engineer')
            .with(6, 'locksmith/clockmaker')
            .with(7, 'physician/apothecary')
            .with(8, 'navigator/guide')
            .with(9, 'alchemist/astrologer')
            .with(10, 'spy/diplomat')
            .with(11, 'cartographer')
            .with(12, 'inventor')

    RangeMap official = new RangeMap()
            .with(1, 'town crier')
            .with(2, 'tax collector')
            .with(3..4, 'armiger/gentry')
            .with(5, 'reeve/sheriff/constable')
            .with(6, 'mayor/magistrate')
            .with(7, 'priest/bishop/abbot')
            .with(8, 'guildmaster')
            .with(9, 'knight/templar')
            .with(10, 'elder/high priest')
            .with(11, 'noble (baron, etc.)')
            .with(12, 'lord/lady/king/queen')

    RangeMap occupation = new RangeMap()
            .with(1, "Criminal: ${ -> pick(criminal)}")
            .with(2..6, "${ -> pick(commoner)}")
            .with(7..8, "${ -> pick(tradesperson)}")
            .with(9..10, "Merchant: ${ -> pick(merchant)}")
            .with(11, "${ -> pick(specialist)}")
            .with(12, "${ -> pick(official)}")

    RangeMap wilderness = new RangeMap()
            .with(1..2, "Criminal: ${ -> pick('1d8', criminal)}")
            .with(3..4, 'adventurer/explorer')
            .with(5..6, 'hunter/gatherer')
            .with(7..8, "${ -> pick(commoner)}")
            .with(9..10, 'ranger/scout')
            .with(11, 'soldier/mercenary')
            .with(12, "${ -> pick(official)}")

    RangeMap rural = new RangeMap()
            .with(1, 'beggar/urchin')
            .with(2, "Criminal: ${ -> pick('1d11', criminal)}")
            .with(3, 'adventurer/explorer')
            .with(4, 'hunter/gatherer')
            .with(5..8, "${ -> pick(commoner)}")
            .with(9, "${ -> pick(tradesperson)}")
            .with(10, "Merchant: ${ -> pick('1d11', merchant)}")
            .with(11, 'militia/soldier/guard')
            .with(12, "${ -> pick(official)}")

    RangeMap urban = new RangeMap()
            .with(1..2, 'beggar/urchin')
            .with(3, "Criminal: ${ -> pick(criminal)}")
            .with(4..7, "${ -> pick(commoner)}")
            .with(8, "${ -> pick(tradesperson)}")
            .with(9, "Merchant: ${ -> pick(merchant)}")
            .with(10, "${ -> pick(specialist)}")
            .with(11, 'militia/soldier/guard')
            .with(12, "${ -> pick(official)}")


    RangeMap physAppearance = new RangeMap()
            .with(1, 'disfigured (missing teeth, eye, etc.)')
            .with(2, 'lasting injury (bad leg, arm, etc.)')
            .with(3, 'tattooed/pockmarked/scarred')
            .with(4, 'unkempt/shabby/grubby')
            .with(5, 'big/thick/brawny')
            .with(6, 'small/scrawny/emaciated')
            .with(7, 'notable hair (wild, long, none, etc.)')
            .with(8, 'notable nose (big, hooked, etc.)')
            .with(9, 'notable eyes (blue, bloodshot, etc.)')
            .with(10, 'clean/well-dressed/well-groomed')
            .with(11, 'attractive/handsome/stunning')
            .with(12, "they are [${ -> pick('1d11', physAppearance)}] despite [a contradictory detail of your choice]")

    RangeMap personality = new RangeMap()
            .with(1, 'loner/alienated/antisocial')
            .with(2, 'cruel/belligerent/bully')
            .with(3, 'anxious/fearful/cowardly')
            .with(4, 'envious/covetous/greedy')
            .with(5, 'aloof/haughty/arrogant')
            .with(6, 'awkward/shy/self-loathing')
            .with(7, 'orderly/compulsive/controlling')
            .with(8, 'confident/impulsive/reckless')
            .with(9, 'kind/generous/compassionate')
            .with(10, 'easygoing/relaxed/peaceful')
            .with(11, 'cheerful/happy/optimistic')
            .with(12, "they are [${ -> pick('1d11', personality)}] despite [a contradictory detail of your choice]")

    RangeMap quirk = new RangeMap()
            .with(1, 'insecure/racist/xenophobic')
            .with(2, 'addict (sweets, drugs, sex, etc.)')
            .with(3, 'phobia (spiders, fire, darkness, etc.)')
            .with(4, 'allergic/asthmatic/chronically ill')
            .with(5, 'skeptic/paranoid')
            .with(6, 'superstitious/devout/fanatical')
            .with(7, 'miser/pack-rat')
            .with(8, 'spendthrift/wastrel')
            .with(9, 'smart aleck/know-it-all')
            .with(10, 'artistic/dreamer/delusional')
            .with(11, 'naive/idealistic')
            .with(12, "they are [${ -> pick('1d11', quirk)}] despite [a contradictory detail of your choice]")

    RangeMap traitsMap = new RangeMap()
            .with(1..6, "${ss('Physical Appearance:')} ${ -> pick(physAppearance)}")
            .with(7..9, "${ss('Personality:')} ${ -> pick(personality)}")
            .with(10..12, "${ss('Quirk:')} ${ -> pick(quirk)}")

    String genSingleTrait() {
        pick(traitsMap)
    }
    String genTraits() {
        """\
<br/>${ -> traitsMap.get(1)}
<br/>${ -> traitsMap.get(7)}
<br/>${ -> traitsMap.get(10)}\
"""
    }
}
