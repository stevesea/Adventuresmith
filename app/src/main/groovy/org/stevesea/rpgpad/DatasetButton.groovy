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
import org.stevesea.rpgpad.data.perilous_wilds.PwDetails
import org.stevesea.rpgpad.data.perilous_wilds.PwDiscovery
import org.stevesea.rpgpad.data.perilous_wilds.PwNPC
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCFollower
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCRural
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCUrban
import org.stevesea.rpgpad.data.perilous_wilds.PwNPCWilderness
import org.stevesea.rpgpad.data.perilous_wilds.PwPlace
import org.stevesea.rpgpad.data.perilous_wilds.PwRegion
// TODO: seems like this could be data-driven instead of enum. since it's enum, it's hard to extend
//       without changing code... not huge now, but will be bigger deal if ever want to extend to allow
//       user-specified tables
@CompileStatic
public enum DatasetButton {
    FreebooterSpells(Dataset.FreebootersOnTheFrontier, FotFSpells.class,  R.string.FotFSpells, R.integer.numGeneratedMany),
    FreebooterTraits(Dataset.FreebootersOnTheFrontier, FotFTraits.class, R.string.FotFTraits, R.integer.numGeneratedMany),

    PerilousPlaces(Dataset.ThePerilousWilds, PwPlace.class,  R.string.PwPlaces, R.integer.numGeneratedMany),
    PerilousRegions(Dataset.ThePerilousWilds, PwRegion.class, R.string.PwRegions, R.integer.numGeneratedMany),
    PerilousDiscovery(Dataset.ThePerilousWilds, PwDiscovery.class, R.string.PwDiscovery),

    PerilousNPC(Dataset.ThePerilousWilds, PwNPC.class, R.string.PwNPC),
    PerilousNPCWilderness(Dataset.ThePerilousWilds, PwNPCWilderness.class, R.string.PwNPCWilderness),
    PerilousNPCRural(Dataset.ThePerilousWilds, PwNPCRural.class, R.string.PwNPCRural),
    PerilousNPCUrban(Dataset.ThePerilousWilds, PwNPCUrban.class, R.string.PwNPCUrban),

    PerilousNPCFollower(Dataset.ThePerilousWilds, PwNPCFollower.class, R.string.PwNPCFollower),

    PerilousCreature(Dataset.ThePerilousWilds, PwCreature.class, R.string.PwCreature),
    PerilousCreatureB(Dataset.ThePerilousWilds, PwCreatureBeast.class, R.string.PwCreatureBeast),
    PerilousCreatureHuman(Dataset.ThePerilousWilds, PwCreatureHuman.class, R.string.PwCreatureHuman),
    PerilousCreatureHumanoid(Dataset.ThePerilousWilds, PwCreatureHumanoid.class, R.string.PwCreatureHumanoid),
    PerilousCreatureMonster(Dataset.ThePerilousWilds, PwCreatureMonster.class, R.string.PwCreatureMonster),

    PerilousDetails(Dataset.ThePerilousWilds, PwDetails.class, R.string.PwDetails),

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
