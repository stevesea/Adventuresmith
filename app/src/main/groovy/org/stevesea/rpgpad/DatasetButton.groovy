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
 */
package org.stevesea.rpgpad

import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d10
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d100
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d12
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d20
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d20Advantage
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d20Disadvantage
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d30
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d6
import org.stevesea.rpgpad.data.dice_roller.DiceRoller1d8
import org.stevesea.rpgpad.data.dice_roller.DiceRoller2d6
import org.stevesea.rpgpad.data.dice_roller.DiceRoller3d6
import org.stevesea.rpgpad.data.dice_roller.DiceRoller4d4
import org.stevesea.rpgpad.data.freebooters_on_the_frontier.FotFSpells
import org.stevesea.rpgpad.data.freebooters_on_the_frontier.FotFTraits
import org.stevesea.rpgpad.data.maze_rats.MazeRatsAfflictions
import org.stevesea.rpgpad.data.maze_rats.MazeRatsCharacter
import org.stevesea.rpgpad.data.maze_rats.MazeRatsItems
import org.stevesea.rpgpad.data.maze_rats.MazeRatsMagic
import org.stevesea.rpgpad.data.maze_rats.MazeRatsMonsters
import org.stevesea.rpgpad.data.maze_rats.MazeRatsPotionEffects
import org.stevesea.rpgpad.data.perilous_wilds.PwCreature
import org.stevesea.rpgpad.data.perilous_wilds.PwCreatureBeast
import org.stevesea.rpgpad.data.perilous_wilds.PwCreatureHuman
import org.stevesea.rpgpad.data.perilous_wilds.PwCreatureHumanoid
import org.stevesea.rpgpad.data.perilous_wilds.PwCreatureMonster
import org.stevesea.rpgpad.data.perilous_wilds.PwDanger
import org.stevesea.rpgpad.data.perilous_wilds.PwDiscovery
import org.stevesea.rpgpad.data.perilous_wilds.PwDungeon
import org.stevesea.rpgpad.data.perilous_wilds.PwDungeonDiscoveryAndDanger
import org.stevesea.rpgpad.data.perilous_wilds.PwFollower
import org.stevesea.rpgpad.data.perilous_wilds.PwNPC
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCRural
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCUrban
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCWilderness
import org.stevesea.rpgpad.data.perilous_wilds.PwPlace
import org.stevesea.rpgpad.data.perilous_wilds.PwRegion
import org.stevesea.rpgpad.data.perilous_wilds.PwSteading
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasure
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded1Bonus
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded2Bonus
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureItem
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureUnguarded
// TODO: seems like this could be data-driven instead of enum. since it's enum, it's hard to extend
//       without changing code... not huge now, but will be bigger deal if ever want to extend to allow
//       user-specified tables
// TODO: having to create a class for each button is kinda dumb. how to just specify a method to run?
@CompileStatic
public enum DatasetButton {
    FreebooterSpells(Dataset.FreebootersOnTheFrontier, FotFSpells.class,  R.string.FotFSpells, R.integer.numGeneratedMany),
    FreebooterTraits(Dataset.FreebootersOnTheFrontier, FotFTraits.class, R.string.FotFTraits, R.integer.numGeneratedSome),

    PerilousDiscovery(Dataset.ThePerilousWilds, PwDiscovery.class, R.string.PwDiscovery, R.integer.numGeneratedSingle),
    PerilousDanger(Dataset.ThePerilousWilds, PwDanger.class, R.string.PwDanger, R.integer.numGeneratedSingle),
    PerilousDungeonExplore(Dataset.ThePerilousWilds, PwDungeonDiscoveryAndDanger.class, R.string.PwDungeonExplore, R.integer.numGeneratedSingle),

    PerilousSteading(Dataset.ThePerilousWildsNames, PwSteading.class, R.string.PwSteading, R.integer.numGeneratedSingle),
    PerilousDungeon(Dataset.ThePerilousWildsNames, PwDungeon.class, R.string.PwDungeon, R.integer.numGeneratedSingle),
    PerilousNPCFollower(Dataset.ThePerilousWildsNames, PwFollower.class, R.string.PwNPCFollower, R.integer.numGeneratedSingle),
    PerilousPlaces(Dataset.ThePerilousWildsNames, PwPlace.class,  R.string.PwPlaces, R.integer.numGeneratedSome),
    PerilousRegions(Dataset.ThePerilousWildsNames, PwRegion.class, R.string.PwRegions, R.integer.numGeneratedSome),

    PerilousTreasure(Dataset.ThePerilousWildsTreasure, PwTreasure.class, R.string.PwTreasure, R.integer.numGeneratedSingle),
    PerilousTreasureItem(Dataset.ThePerilousWildsTreasure, PwTreasureItem.class, R.string.PwTreasureItem, R.integer.numGeneratedSome),
    PerilousTreasureUnguarded(Dataset.ThePerilousWildsTreasure, PwTreasureUnguarded.class, R.string.PwTreasureUnguarded, R.integer.numGeneratedSingle),
    PerilousTreasureGuarded(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded.class, R.string.PwTreasureGuarded, R.integer.numGeneratedSingle),
    PerilousTreasureGuarded1(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded1Bonus.class, R.string.PwTreasureGuarded1, R.integer.numGeneratedSingle),
    PerilousTreasureGuarded2(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded2Bonus.class, R.string.PwTreasureGuarded2, R.integer.numGeneratedSingle),

    PerilousNPC(Dataset.ThePerilousWildsNPC, PwNPC.class, R.string.PwNPC, R.integer.numGeneratedSingle),
    PerilousNPCWilderness(Dataset.ThePerilousWildsNPC, PwNPCWilderness.class, R.string.PwNPCWilderness, R.integer.numGeneratedSingle),
    PerilousNPCRural(Dataset.ThePerilousWildsNPC, PwNPCRural.class, R.string.PwNPCRural, R.integer.numGeneratedSingle),
    PerilousNPCUrban(Dataset.ThePerilousWildsNPC, PwNPCUrban.class, R.string.PwNPCUrban, R.integer.numGeneratedSingle),

    PerilousCreature(Dataset.ThePerilousWildsCreature, PwCreature.class, R.string.PwCreature, R.integer.numGeneratedSingle),
    PerilousCreatureB(Dataset.ThePerilousWildsCreature, PwCreatureBeast.class, R.string.PwCreatureBeast, R.integer.numGeneratedSingle),
    PerilousCreatureHuman(Dataset.ThePerilousWildsCreature, PwCreatureHuman.class, R.string.PwCreatureHuman, R.integer.numGeneratedSingle),
    PerilousCreatureHumanoid(Dataset.ThePerilousWildsCreature, PwCreatureHumanoid.class, R.string.PwCreatureHumanoid, R.integer.numGeneratedSingle),
    PerilousCreatureMonster(Dataset.ThePerilousWildsCreature, PwCreatureMonster.class, R.string.PwCreatureMonster, R.integer.numGeneratedSingle),


    MrCharacters(Dataset.MazeRats, MazeRatsCharacter.class, R.string.MrCharacters, R.integer.numGeneratedSingle),
    MrMonsters(Dataset.MazeRats, MazeRatsMonsters.class, R.string.MrMonsters, R.integer.numGeneratedSome),
    MrMagic(Dataset.MazeRats, MazeRatsMagic.class, R.string.MrMagic, R.integer.numGeneratedSome),
    MrItems(Dataset.MazeRats, MazeRatsItems.class, R.string.MrItems, R.integer.numGeneratedSome),
    MrAfflictions(Dataset.MazeRats, MazeRatsAfflictions.class, R.string.MrAfflictions, R.integer.numGeneratedSome),
    MrPotionEffects(Dataset.MazeRats, MazeRatsPotionEffects.class, R.string.MrPotionEffects, R.integer.numGeneratedSome),

    Dr1d6(Dataset.DiceRoller, DiceRoller1d6.class, R.string.dice_roller_1d6, R.integer.numGeneratedSingle),
    Dr1d8(Dataset.DiceRoller, DiceRoller1d8.class, R.string.dice_roller_1d8, R.integer.numGeneratedSingle),
    Dr1d10(Dataset.DiceRoller, DiceRoller1d10.class, R.string.dice_roller_1d10, R.integer.numGeneratedSingle),
    Dr1d12(Dataset.DiceRoller, DiceRoller1d12.class, R.string.dice_roller_1d12, R.integer.numGeneratedSingle),
    Dr1d20(Dataset.DiceRoller, DiceRoller1d20.class, R.string.dice_roller_1d20, R.integer.numGeneratedSingle),
    Dr1d30(Dataset.DiceRoller, DiceRoller1d30.class, R.string.dice_roller_1d30, R.integer.numGeneratedSingle),
    Dr1d100(Dataset.DiceRoller, DiceRoller1d100.class, R.string.dice_roller_1d100, R.integer.numGeneratedSingle),
    Dr2d20Adv(Dataset.DiceRoller, DiceRoller1d20Advantage.class, R.string.dice_roller_2d20_adv, R.integer.numGeneratedSingle),
    Dr2d20Disadv(Dataset.DiceRoller, DiceRoller1d20Disadvantage.class, R.string.dice_roller_2d20_disadv, R.integer.numGeneratedSingle),
    Dr2d6(Dataset.DiceRoller, DiceRoller2d6.class, R.string.dice_roller_2d6, R.integer.numGeneratedSingle),
    Dr3d6(Dataset.DiceRoller, DiceRoller3d6.class, R.string.dice_roller_3d6, R.integer.numGeneratedSingle),
    Dr4d4(Dataset.DiceRoller, DiceRoller4d4.class, R.string.dice_roller_4d4, R.integer.numGeneratedSingle),
    ;

    int stringResourceId
    Dataset dataset
    Class<? extends AbstractGenerator> clz
    int numGeneratedId
    int helpTextId

    DatasetButton(Dataset dataset, Class<? extends AbstractGenerator> clz, int stringResourceId, int numGeneratedId, int helpTextId = 0) {
        this.stringResourceId = stringResourceId
        this.dataset = dataset
        this.clz = clz
        this.numGeneratedId = numGeneratedId
        this.helpTextId = helpTextId
    }

    static Collection<DatasetButton> getButtonsForDataset(Dataset dset) {
        if (dset.equals(Dataset.None)) {
            return new ArrayList<>()
        }
        return values().grep{((DatasetButton)it).dataset == dset}
    }
}
