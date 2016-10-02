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

import javax.inject.Inject

@CompileStatic
class PwSteading extends AbstractGenerator{
    PwCreature pwCreature

    @Inject
    PwSteading(PwCreature pwCreature) {
        super(pwCreature.shuffler)
        this.pwCreature = pwCreature
    }

    @Override
    String generate() {
        generate('1d12')
    }

    String generate(String diceStr) {
        """\
<br/>${strong('Steading')}
<br/>Built by: ${ -> pick('1d4+4', pwCreature.creature_no_tags)}
<br/>
<br/>${ -> pick(diceStr, steadingMap)}\
"""
    }


    RangeMap steadingMap = new RangeMap()
            .with(1..5, "${ -> generateVillage()}")
            .with(6..8, "${ -> generateTown()}")
            .with(9..11, "${ -> generateKeep()}")
            .with(12, "${ -> generateCity()}")

    public static enum Prosperity {
        Dirt,
        Poor,
        Moderate,
        Wealthy ,
        Rich;

        private static Prosperity[] vals = values();
        public Prosperity incr()
        {
            return vals[Math.max(this.ordinal()+1,vals.length-1)];
        }
        public Prosperity decr() {
            return vals[Math.max(this.ordinal()-1,0)];
        }
    }
    public static enum Population {
        Exodus,
        Shrinking,
        Steady,
        Growing,
        Booming;

        private static Population[] vals = values();
        public Population incr()
        {
            return vals[Math.max(this.ordinal()+1,vals.length-1)];
        }
        public Population decr() {
            return vals[Math.max(this.ordinal()-1,0)];
        }
    }
    public static enum Defenses {
        None,
        Militia,
        Watch,
        Guard,
        Garrison,
        Battalion,
        Legion;

        private static Defenses[] vals = values();
        public Defenses incr()
        {
            return vals[Math.max(this.ordinal()+1,vals.length-1)];
        }
        public Defenses decr() {
            return vals[Math.max(this.ordinal()-1,0)];
        }
    }
    static class SteadingInfo {
        Set<String> tags = new TreeSet<>()
        Prosperity prosperity
        Population population
        Defenses defenses
    }
    SteadingInfo info

    String generateVillage() {
        info = new SteadingInfo()
        info.with {
            prosperity = Prosperity.Poor
            population = Population.Steady
            defenses = Defenses.Militia
            tags.add('Resource: ' + pick(PwDiscovery.resource).toString())
            tags.add('Oath: other steading')
        }

        return """\
${strong('Village')}
<br/>${pick(village)}
<br/>${pick(village_problem)}
<br/>
<br/>${ss('Prosperity:')} ${info.prosperity}
<br/>${ss('Population:')} ${info.population}
<br/>${ss('Defenses:')} ${info.defenses}
<br/>
<br/>${ -> info.tags.join('<br/>')}\
"""
    }
    String generateTown() {
        info = new SteadingInfo()
        info.with {
            prosperity = Prosperity.Moderate
            population = Population.Steady
            defenses = Defenses.Watch
            tags.add('Resource: ' + pick(PwDiscovery.resource).toString())
            tags.add('Trade: 2 other steadings')
        }

        return """\
${strong('Town')}
<br/>${pick(town)}
<br/>${pick(town_problem)}
<br/>
<br/>${ss('Prosperity:')} ${info.prosperity}
<br/>${ss('Population:')} ${info.population}
<br/>${ss('Defenses:')} ${info.defenses}
<br/>
<br/>${ -> info.tags.join('<br/>')}\
"""
    }

    String generateKeep() {
        info = new SteadingInfo()
        info.with {
            prosperity = Prosperity.Poor
            population = Population.Shrinking
            defenses = Defenses.Guard
            tags.add('Trade: someplace with supplies')
            tags.add('Need: supplies')
            tags.add('Oath: GM\'s choice')
        }

        return """\
${strong('Keep')}
<br/>${pick(keep)}
<br/>${pick(keep_problem)}
<br/>
<br/>${ss('Prosperity:')} ${info.prosperity}
<br/>${ss('Population:')} ${info.population}
<br/>${ss('Defenses:')} ${info.defenses}
<br/>
<br/>${ -> info.tags.join('<br/>')}\
"""
    }

    String generateCity() {
        info = new SteadingInfo()
        info.with {
            prosperity = Prosperity.Moderate
            population = Population.Steady
            defenses = Defenses.Guard
            tags.add('Oath: 2+ steadings of GM\'s choice')
            tags.add('Guild: GM\'s choice')
        }

        return """\
${strong('City')}
<br/>${pick(city)}
<br/>${pick(city_problem)}
<br/>
<br/>${ss('Prosperity:')} ${info.prosperity}
<br/>${ss('Population:')} ${info.population}
<br/>${ss('Defenses:')} ${info.defenses}
<br/>
<br/>${ -> info.tags.join('<br/>')}\
"""
    }
    RangeMap village = new RangeMap()
            .with(1..3, "Natural defenses${ -> info.tags.add('Safe'); info.defenses.decr();''}")
            .with(4..6, "Abundant resources${ -> info.tags.add('Enmity: GM choice'); info.tags.add('Resource: ' + pick(PwDiscovery.resource)); info.prosperity.incr();''}")
            .with(7..8, "Protected by another steading${ -> info.tags.add('Oath: other steading'); info.defenses.incr();''}")
            .with(9..10, "On a major road.${ -> info.tags.add('Trade: ' + pick(PwDiscovery.resource)); info.prosperity.incr();''}")
            .with(11, "Built around a wizard’s tower${ -> info.tags.add('Personage: the wizard'); info.tags.add('Blight: arcane creatures');''}")
            .with(12, "Built on a site of religious significance${ -> info.tags.add('Divine: ' + pick(PwDiscovery.divine)); info.tags.add('History: GM\'s choice');''}")

    RangeMap village_problem = new RangeMap()
            .with(1..2, "Surrounded by arid or uncultivable land${ -> info.tags.add('Need: food');''}")
            .with(3..4, "Dedicated to a deity${ -> info.tags.add('Religious: deity'); info.tags.add('Enmity: steading of opposing deity');''}")
            .with(5..6, "Recently at war: -Prosperity if they fought to the end, -Defenses if they lost${ -> info.population.decr();''}")
            .with(7..8, "Monster problem${ -> info.tags.add('Blight: ' + pwCreature.genMonster()); info.tags.add('Need: adventurers');''}")
            .with(9..10, "Absorbed another village${ -> info.population.incr(); info.tags.add('Lawless');''}")
            .with(11..12, "Remote or unwelcoming${ -> info.prosperity.decr(); info.tags.add('Dwarven, Elven, or other non-human');''}")

    RangeMap town = new RangeMap()
            .with(1, "Booming${ -> info.tags.add('Booming'); info.tags.add('Lawless');''}")
            .with(2..3, "At a crossroads${ -> info.tags.add('Market'); info.prosperity.incr();''}")
            .with(4..5, "Defended by another steading${ -> info.tags.add('Oath: other steading (defender)'); info.defenses.incr();''}")
            .with(6..7, "Built around a church${-> info.tags.add('Power: divine'); ''}")
            .with(8..10, "Built around a craft${-> info.tags.add('Resource: ' + pick(PwDiscovery.resource)); info.tags.add('Craft: (for resource)');''}")
            .with(11..12, "Built around a military post${ -> info.defenses.incr();''}")

    RangeMap town_problem = new RangeMap()
            .with(1..2, "Outgrowing a vital resource${-> info.tags.add('Need: that resource'); info.tags.add('Trade: a steading with that resource');''}")
            .with(3..4, "Offers defense to others${ -> info.defenses.decr(); info.tags.add('Oath: GM choice');''}")
            .with(5..6, "Outlaw rumored to live there${-> info.tags.add('Personage: the outloaw'); info.tags.add('Enmity: preyed upon by outlaw');''}")
            .with(7..8, "Controls a good/service${-> info.tags.add('Exotic: that good/services'); info.tags.add('Enmity: steading with ambition');''}")
            .with(9..10, "Suffers from disease${ -> info.population.decr();''}")
            .with(11..12, "Popular meeting place${ -> info.population.incr(); info.tags.add('Lawless');''}")

    RangeMap keep = new RangeMap()
            .with(1..2, "Belongs to a noble family${ -> info.prosperity.incr(); info.tags.add('Power: political');''}")
            .with(3..4, "Run by a skilled commander${ -> info.defenses.incr(); info.tags.add('Personage: the commander');''}")
            .with(5..6, "Stands watch over a trade road${ -> info.prosperity.incr(); info.tags.add('Guild: trade');''}")
            .with(7..8, "Used to train special troops${ -> info.population.decr(); info.tags.add('Arcane');''}")
            .with(9..10, "Surrounded by fertile land${ -> info.tags.remove('Need: Supplies');''}")
            .with(11..12, "Stands on a border${ -> info.defenses.incr(); info.tags.add('Enmity: steading on the other side of the border');''}")

    RangeMap keep_problem = new RangeMap()
            .with(1..3, "Built on a naturally defensible position${ -> info.population.decr(); info.tags.add('Safe');''}")
            .with(4, "Formerly occupied by another power${ -> info.tags.add('Enmity: steadings of former occupier power');''}")
            .with(5, "Safe haven for brigands${ -> info.tags.add('Lawless');''}")
            .with(6, "Built to defend from a specific threat${ -> info.tags.add('Blight: that threat');''}")
            .with(7, "Has seen horrible bloody war${ -> info.tags.add('Blight: restless spirits'); info.tags.add('History: battle');''}")
            .with(8, "Is given the worst of the worst${ -> info.tags.add('Need: skilled recruits');''}")
            .with(9..10, "Suffers from disease${ -> info.population.decr(); ''}")
            .with(11..12, "Popular meeting place${ -> info.population.incr(); info.tags.add('-Law');''}")

    RangeMap city = new RangeMap()
            .with(1..3, "Permanent defenses, such as walls${ -> info.defenses.incr(); info.tags.add('Oath: GM choice'); ''}")
            .with(4..6, "Ruled by a single individual${ -> info.tags.add('Personage: the ruler'); info.tags.add('Power: political');''}")
            .with(7, "Diverse${ -> info.tags.add('Dwarven or Elven or both'); ''}")
            .with(8..10, "Trade hub${ -> info.prosperity.incr(); info.tags.add('Trade: every nearby steading'); ''}")
            .with(11, "Ancient, built on top of its own ruins${ -> info.tags.add('History: GM choice'); info.tags.add('Divine');''}")
            .with(12, "Center of learning${ -> info.tags.add('Power: arcane'); info.tags.add('Arcane');info.tags.add('Craft: GM choice');''}")

    RangeMap city_problem = new RangeMap()
            .with(1..3, "Outgrown its resources${ -> info.population.incr(); info.tags.add('Need: food'); ''}")
            .with(4..6, "Designs on nearby territory${ -> info.defenses.incr(); info.tags.add('Enmity: nearby steadings'); ''}")
            .with(7..8, "Ruled by a theocracy${ -> info.defenses.decr(); info.tags.add('Power: divine'); ''}")
            .with(9..10, "Ruled by the people${ -> info.defenses.decr(); info.population.incr(); ''}")
            .with(11, "Supernatural defenses${ -> info.defenses.incr(); info.tags.add('Blight: related supernatural creatures'); ''}")
            .with(12, "Occupies a place of power${ -> info.tags.add('Blight: arcane creatures'); info.tags.add('Arcane');info.tags.add('Personage: whoever watches the place of power');''}")

}
