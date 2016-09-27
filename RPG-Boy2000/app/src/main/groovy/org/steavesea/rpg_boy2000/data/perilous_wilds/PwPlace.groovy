/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.steavesea.rpg_boy2000.data.perilous_wilds

import org.steavesea.rpg_boy2000.data.AbstractGenerator
import org.steavesea.rpg_boy2000.data.RpgBoyData
import org.steavesea.rpg_boy2000.data.Shuffler

import javax.inject.Inject

class PwPlace extends AbstractGenerator {

    public String getName() {
        return "Place"
    }
    public String getDataset() {
        return RpgBoyData.PERILOUS_WILDS
    }
    static final List<String> adjectives = """
        Ancient Ashen
        Black Bloody Blue Bright Broken Burning
        Clouded Copper Cracked
        Dark Dead Doomed
        Endless
        Fallen Far Fearsome Floating Forbidden Frozen
        Ghostly Gloomy Golden Grim
        Hidden High
        Iron
        Jagged
        Lonely Lost Low
        Near
        Petrified
        Red
        Screaming Sharp Shattered Shifting Shining Shivering Shrouded Silver Stalwart Stoney Sunken
        Thorny Thundering
        White Withered
        """.tokenize()

    static final List<String> features = """
        Barrier Beach Bowl
        Camp Cave Circle City Cliff Crater Crossing Crypt
        Den Ditch
        Falls Fence Field Fort
        Gate Grove
        Hill Hole Hut
        Keep
        Lake
        Marsh Meadow Mountain
        Pit Post
        Ridge Ring Rise Road Rock Ruin
        Shrine Spire Spring Stone
        Tangle Temple Throne Tomb Tower Town Tree
        Vale Valley Village
        Wall
        """.tokenize()

    static final List<String> nouns = """
        Arm Ash
        Blood
        Child Cinder Corpse Crystal
        Dagger Death Demon Devil Doom
        Eye
        Fear Finger Fire Foot
        Ghost Giant Goblin God Gold
        Hand Head Heart Hero Hope
        King Knave Knight
        Muck Mud
        Priest
        Queen
        Sailor Silver Skull Smoke Souls Spear Spirit Stone Sword
        Thief Troll
        Warrior Water Witch Wizard
        """.tokenize()

    @Inject
    PwPlace(Shuffler shuffler) {
        super(shuffler);
    }

    List<GString> getFormatters() {
        return [
                "The ${ -> pick(features)}",
                "The ${ -> pick(adjectives)} ${ -> pick(features)}",
                "The ${ -> pick(features)} of (the) ${ -> pick(nouns)}",
                "The ${ -> pick(nouns)}\'s ${ -> pick(features)}",
                "${ -> pick(features)} of the ${ -> pick(adjectives)} ${ -> pick(nouns)}",
                "The ${ -> pick(adjectives)} ${ -> pick(nouns)}",
        ]
    }

}
