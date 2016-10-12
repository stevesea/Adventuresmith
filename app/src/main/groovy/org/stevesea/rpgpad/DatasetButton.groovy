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
import org.stevesea.rpgpad.data.fourth_page.FourthPageArtifact
import org.stevesea.rpgpad.data.fourth_page.FourthPageCity
import org.stevesea.rpgpad.data.fourth_page.FourthPageDungeon
import org.stevesea.rpgpad.data.fourth_page.FourthPageMonster
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
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded1Bonus
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureGuarded2Bonus
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureItem
import org.stevesea.rpgpad.data.perilous_wilds.PwTreasureUnguarded

@CompileStatic
public enum DatasetButton {
    FreebooterSpells(Dataset.FreebootersOnTheFrontier, FotFSpells.class,  R.string.FotFSpells),
    FreebooterTraits(Dataset.FreebootersOnTheFrontier, FotFTraits.class, R.string.FotFTraits),

    PerilousDiscovery(Dataset.ThePerilousWilds, PwDiscovery.class, R.string.PwDiscovery),
    PerilousDanger(Dataset.ThePerilousWilds, PwDanger.class, R.string.PwDanger),
    PerilousDungeonExplore(Dataset.ThePerilousWilds, PwDungeonDiscoveryAndDanger.class, R.string.PwDungeonExplore),

    PerilousSteading(Dataset.ThePerilousWildsNames, PwSteading.class, R.string.PwSteading),
    PerilousDungeon(Dataset.ThePerilousWildsNames, PwDungeon.class, R.string.PwDungeon),
    PerilousNPCFollower(Dataset.ThePerilousWildsNames, PwFollower.class, R.string.PwNPCFollower),
    PerilousPlaces(Dataset.ThePerilousWildsNames, PwPlace.class,  R.string.PwPlaces),
    PerilousRegions(Dataset.ThePerilousWildsNames, PwRegion.class, R.string.PwRegions),

    PerilousTreasureGuarded(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded.class, R.string.PwTreasureGuarded),
    PerilousTreasureGuarded1(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded1Bonus.class, R.string.PwTreasureGuarded1),
    PerilousTreasureGuarded2(Dataset.ThePerilousWildsTreasure, PwTreasureGuarded2Bonus.class, R.string.PwTreasureGuarded2),
    PerilousTreasureUnguarded(Dataset.ThePerilousWildsTreasure, PwTreasureUnguarded.class, R.string.PwTreasureUnguarded),
    PerilousTreasureItem(Dataset.ThePerilousWildsTreasure, PwTreasureItem.class, R.string.PwTreasureItem),

    PerilousNPC(Dataset.ThePerilousWildsNPC, PwNPC.class, R.string.PwNPC),
    PerilousNPCWilderness(Dataset.ThePerilousWildsNPC, PwNPCWilderness.class, R.string.PwNPCWilderness),
    PerilousNPCRural(Dataset.ThePerilousWildsNPC, PwNPCRural.class, R.string.PwNPCRural),
    PerilousNPCUrban(Dataset.ThePerilousWildsNPC, PwNPCUrban.class, R.string.PwNPCUrban),

    PerilousCreature(Dataset.ThePerilousWildsCreature, PwCreature.class, R.string.PwCreature),
    PerilousCreatureB(Dataset.ThePerilousWildsCreature, PwCreatureBeast.class, R.string.PwCreatureBeast),
    PerilousCreatureHuman(Dataset.ThePerilousWildsCreature, PwCreatureHuman.class, R.string.PwCreatureHuman),
    PerilousCreatureHumanoid(Dataset.ThePerilousWildsCreature, PwCreatureHumanoid.class, R.string.PwCreatureHumanoid),
    PerilousCreatureMonster(Dataset.ThePerilousWildsCreature, PwCreatureMonster.class, R.string.PwCreatureMonster),


    MrCharacters(Dataset.MazeRats, MazeRatsCharacter.class, R.string.MrCharacters),
    MrMonsters(Dataset.MazeRats, MazeRatsMonsters.class, R.string.MrMonsters),
    MrMagic(Dataset.MazeRats, MazeRatsMagic.class, R.string.MrMagic),
    MrItems(Dataset.MazeRats, MazeRatsItems.class, R.string.MrItems),
    MrAfflictions(Dataset.MazeRats, MazeRatsAfflictions.class, R.string.MrAfflictions),
    MrPotionEffects(Dataset.MazeRats, MazeRatsPotionEffects.class, R.string.MrPotionEffects),

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


    FpArtifacts(Dataset.TheFourthPage, FourthPageArtifact.class, R.string.fourth_page_artifact),
    FpMonster(Dataset.TheFourthPage, FourthPageMonster.class, R.string.fourth_page_monster),
    FpCity(Dataset.TheFourthPage, FourthPageCity.class, R.string.fourth_page_city),
    FpDungeon(Dataset.TheFourthPage, FourthPageDungeon.class, R.string.fourth_page_dungeon),
    ;

    int stringResourceId
    Dataset dataset
    Class<? extends AbstractGenerator> clz

    DatasetButton(Dataset dataset, Class<? extends AbstractGenerator> clz, int stringResourceId) {
        this.stringResourceId = stringResourceId
        this.dataset = dataset
        this.clz = clz
    }

    static Collection<DatasetButton> getButtonsForDataset(Dataset dset) {
        if (dset.equals(Dataset.None)) {
            return new ArrayList<>()
        }
        return values().grep{((DatasetButton)it).dataset == dset}
    }

    String generate() {
        final AbstractGenerator generator = clz.newInstance()
        return generator.generate()
    }
}
