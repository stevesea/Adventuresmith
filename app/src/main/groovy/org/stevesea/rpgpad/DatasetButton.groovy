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
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureItem
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureUnguarded
// TODO: seems like this could be data-driven instead of enum. since it's enum, it's hard to extend
//       without changing code... not huge now, but will be bigger deal if ever want to extend to allow
//       user-specified tables
@CompileStatic
public enum DatasetButton {
    FreebooterSpells(Dataset.FreebootersOnTheFrontier, FotFSpells.class,  R.string.FotFSpells, R.integer.numGeneratedMany),
    FreebooterTraits(Dataset.FreebootersOnTheFrontier, FotFTraits.class, R.string.FotFTraits, R.integer.numGeneratedMany),

    PerilousDiscovery(Dataset.ThePerilousWilds, PwDiscovery.class, R.string.PwDiscovery, R.integer.numGeneratedSingle),
    PerilousDanger(Dataset.ThePerilousWilds, PwDanger.class, R.string.PwDanger, R.integer.numGeneratedSingle),
    PerilousSteading(Dataset.ThePerilousWilds, PwSteading.class, R.string.PwSteading, R.integer.numGeneratedSingle),
    PerilousPlaces(Dataset.ThePerilousWilds, PwPlace.class,  R.string.PwPlaces, R.integer.numGeneratedMany),
    PerilousRegions(Dataset.ThePerilousWilds, PwRegion.class, R.string.PwRegions, R.integer.numGeneratedMany),

    // TODO: having to create a class for each button is kinda dumb. how to just specify a method to run?
    PerilousTreasure(Dataset.ThePerilousWildsTreasure, PwTreasure.class, R.string.PwTreasure),
    PerilousTreasureItem(Dataset.ThePerilousWildsTreasure, PwTreasureItem.class, R.string.PwTreasureItem),
    PerilousTreasureUnguarded(Dataset.ThePerilousWildsTreasure, PwTreasureUnguarded.class, R.string.PwTreasureUnguarded),
    PerilousTreasureGuarded(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded.class, R.string.PwTreasureGuarded),

    PerilousNPCWilderness(Dataset.ThePerilousWildsNPC, PwNPCWilderness.class, R.string.PwNPCWilderness, R.integer.numGeneratedSingle),
    PerilousNPCRural(Dataset.ThePerilousWildsNPC, PwNPCRural.class, R.string.PwNPCRural, R.integer.numGeneratedSingle),
    PerilousNPCUrban(Dataset.ThePerilousWildsNPC, PwNPCUrban.class, R.string.PwNPCUrban, R.integer.numGeneratedSingle),
    PerilousNPC(Dataset.ThePerilousWildsNPC, PwNPC.class, R.string.PwNPC, R.integer.numGeneratedSingle),
    PerilousNPCFollower(Dataset.ThePerilousWildsNPC, PwFollower.class, R.string.PwNPCFollower, R.integer.numGeneratedSingle),

    PerilousCreatureB(Dataset.ThePerilousWildsCreature, PwCreatureBeast.class, R.string.PwCreatureBeast, R.integer.numGeneratedSingle),
    PerilousCreatureHuman(Dataset.ThePerilousWildsCreature, PwCreatureHuman.class, R.string.PwCreatureHuman, R.integer.numGeneratedSingle),
    PerilousCreatureHumanoid(Dataset.ThePerilousWildsCreature, PwCreatureHumanoid.class, R.string.PwCreatureHumanoid, R.integer.numGeneratedSingle),
    PerilousCreatureMonster(Dataset.ThePerilousWildsCreature, PwCreatureMonster.class, R.string.PwCreatureMonster, R.integer.numGeneratedSingle),
    PerilousCreature(Dataset.ThePerilousWildsCreature, PwCreature.class, R.string.PwCreature, R.integer.numGeneratedSingle),


    MrCharacters(Dataset.MazeRats, MazeRatsCharacter.class, R.string.MrCharacters, R.integer.numGeneratedSome),
    MrMonsters(Dataset.MazeRats, MazeRatsMonsters.class, R.string.MrMonsters, R.integer.numGeneratedMany),
    MrMagic(Dataset.MazeRats, MazeRatsMagic.class, R.string.MrMagic, R.integer.numGeneratedMany),
    MrItems(Dataset.MazeRats, MazeRatsItems.class, R.string.MrItems, R.integer.numGeneratedMany),
    MrAfflictions(Dataset.MazeRats, MazeRatsAfflictions.class, R.string.MrAfflictions, R.integer.numGeneratedMany),
    MrPotionEffects(Dataset.MazeRats, MazeRatsPotionEffects.class, R.string.MrPotionEffects, R.integer.numGeneratedMany),

    Dr1d6(Dataset.DiceRoller, DiceRoller1d6.class, R.string.dice_roller_1d6),
    Dr1d8(Dataset.DiceRoller, DiceRoller1d8.class, R.string.dice_roller_1d8),
    Dr1d10(Dataset.DiceRoller, DiceRoller1d10.class, R.string.dice_roller_1d10),
    Dr1d12(Dataset.DiceRoller, DiceRoller1d12.class, R.string.dice_roller_1d12),
    Dr1d20(Dataset.DiceRoller, DiceRoller1d20.class, R.string.dice_roller_1d20),
    Dr1d30(Dataset.DiceRoller, DiceRoller1d30.class, R.string.dice_roller_1d30),
    Dr1d100(Dataset.DiceRoller, DiceRoller1d100.class, R.string.dice_roller_1d100),
    Dr2d20Adv(Dataset.DiceRoller, DiceRoller1d20Advantage.class, R.string.dice_roller_2d20_adv),
    Dr2d20Disadv(Dataset.DiceRoller, DiceRoller1d20Disadvantage.class, R.string.dice_roller_2d20_disadv),
    Dr2d6(Dataset.DiceRoller, DiceRoller2d6.class, R.string.dice_roller_2d6),
    Dr3d6(Dataset.DiceRoller, DiceRoller3d6.class, R.string.dice_roller_3d6),
    Dr4d4(Dataset.DiceRoller, DiceRoller4d4.class, R.string.dice_roller_4d4),
    ;

    int stringResourceId
    Dataset dataset
    Class<? extends AbstractGenerator> clz
    int numGeneratedId

    DatasetButton(Dataset dataset, Class<? extends AbstractGenerator> clz, int stringResourceId, int numGeneratedId = R.integer.numGeneratedSingle) {
        this.stringResourceId = stringResourceId
        this.dataset = dataset
        this.clz = clz
        this.numGeneratedId = numGeneratedId
    }

    static Collection<DatasetButton> getButtonsForDataset(Dataset dset) {
        if (dset.equals(Dataset.None)) {
            return new ArrayList<>()
        }
        return values().grep{((DatasetButton)it).dataset == dset}
    }
}
