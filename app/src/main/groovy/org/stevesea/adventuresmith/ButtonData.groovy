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

package org.stevesea.adventuresmith

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.StringRes
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator
import org.stevesea.adventuresmith.data.DiceRoller
import org.stevesea.adventuresmith.data.fourth_page.FourthPageArtifact
import org.stevesea.adventuresmith.data.fourth_page.FourthPageCity
import org.stevesea.adventuresmith.data.fourth_page.FourthPageDungeon
import org.stevesea.adventuresmith.data.fourth_page.FourthPageMonster
import org.stevesea.adventuresmith.data.freebooters_on_the_frontier.FotFCharacters
import org.stevesea.adventuresmith.data.freebooters_on_the_frontier.FotFSpells
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsAfflictions
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsCharacter
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsItems
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsMagic
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsMonsters
import org.stevesea.adventuresmith.data.maze_rats.MazeRatsPotionEffects
import org.stevesea.adventuresmith.data.perilous_wilds.PwCreature
import org.stevesea.adventuresmith.data.perilous_wilds.PwDanger
import org.stevesea.adventuresmith.data.perilous_wilds.PwDiscovery
import org.stevesea.adventuresmith.data.perilous_wilds.PwDungeon
import org.stevesea.adventuresmith.data.perilous_wilds.PwDungeonDiscoveryAndDanger
import org.stevesea.adventuresmith.data.perilous_wilds.PwFollower
import org.stevesea.adventuresmith.data.perilous_wilds.PwNPC
import org.stevesea.adventuresmith.data.perilous_wilds.PwNames
import org.stevesea.adventuresmith.data.perilous_wilds.PwPlace
import org.stevesea.adventuresmith.data.perilous_wilds.PwRegion
import org.stevesea.adventuresmith.data.perilous_wilds.PwSteading
import org.stevesea.adventuresmith.data.perilous_wilds.PwTreasure
import org.stevesea.adventuresmith.data.perilous_wilds.PwTreasureGuarded
import org.stevesea.adventuresmith.data.stars_without_number.Culture
import org.stevesea.adventuresmith.data.stars_without_number.SWNAlien
import org.stevesea.adventuresmith.data.stars_without_number.SWNAnimal
import org.stevesea.adventuresmith.data.stars_without_number.SWNWorld
import org.stevesea.adventuresmith.data.stars_without_number.SWNadventure_seed
import org.stevesea.adventuresmith.data.stars_without_number.SWNarchitecture
import org.stevesea.adventuresmith.data.stars_without_number.SWNcorporation
import org.stevesea.adventuresmith.data.stars_without_number.SWNfaction
import org.stevesea.adventuresmith.data.stars_without_number.SWNheresy
import org.stevesea.adventuresmith.data.stars_without_number.SWNnames
import org.stevesea.adventuresmith.data.stars_without_number.SWNnpc
import org.stevesea.adventuresmith.data.stars_without_number.SWNpolitical_party
import org.stevesea.adventuresmith.data.stars_without_number.SWNreligion
import org.stevesea.adventuresmith.data.stars_without_number.SWNroom_dressing

@CompileStatic
@Canonical
class ButtonData {
    @StringRes
    @ButtonId
    int id

    @DrawerItemId
    int drawerId

    AbstractGenerator generator

    String generate() {
        if (generator != null)
            generator.generate()
        else
            'unimplemented!'
    }

    static Map<Integer, List<ButtonData>> drawer2Button = new HashMap<>()
    static Map<Integer, ButtonData> buttons = new HashMap<>()

    static addButton(ButtonData buttonData) {
        buttons.put(buttonData.id, buttonData)
        if (!drawer2Button.containsKey(buttonData.drawerId)) {
            drawer2Button.put(buttonData.drawerId, new ArrayList<ButtonData>())
        }
        drawer2Button.get(buttonData.drawerId).add(buttonData)
    }
    static addButtons(ButtonData... buttonDatas) {
        for (ButtonData bd : buttonDatas) {
            addButton(bd)
        }
    }
    static {
        addButtons(
                new ButtonData(
                        id: R.string.FotFChars,
                        drawerId: DrawerItemId.FotF,
                        generator: new FotFCharacters(),
                ),
                new ButtonData(
                        id: R.string.FotFSpells,
                        drawerId: DrawerItemId.FotF,
                        generator: new FotFSpells(),
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.PwDiscovery,
                        drawerId: DrawerItemId.PwDiscoveriesAndDangers,
                        generator: new PwDiscovery(),
                ),
                new ButtonData(
                        id: R.string.PwDanger,
                        drawerId: DrawerItemId.PwDiscoveriesAndDangers,
                        generator: new PwDanger(),
                ),
                new ButtonData(
                        id: R.string.PwDungeonExplore,
                        drawerId: DrawerItemId.PwDiscoveriesAndDangers,
                        generator: new PwDungeonDiscoveryAndDanger(),
                )
        )
        addButtons(
                new ButtonData(
                        id: R.string.PwSteading,
                        drawerId: DrawerItemId.PwNames,
                        generator: new PwSteading(),
                ),
                new ButtonData(
                        id: R.string.PwDungeon,
                        drawerId: DrawerItemId.PwNames,
                        generator: new PwDungeon(),
                ),
                new ButtonData(
                        id: R.string.PwNPCFollower,
                        drawerId: DrawerItemId.PwNames,
                        generator: new PwFollower(),
                ),
                new ButtonData(
                        id: R.string.PwNameArpad,
                        drawerId: DrawerItemId.PwNames,
                        generator: PwNames.generators.get("Arpad"),
                ),
                new ButtonData(
                        id: R.string.PwNameOloru,
                        drawerId: DrawerItemId.PwNames,
                        generator: PwNames.generators.get("Oloru"),
                ),
                new ButtonData(
                        id: R.string.PwNameTamanarugan,
                        drawerId: DrawerItemId.PwNames,
                        generator: PwNames.generators.get("Tamanarugan"),
                ),
                new ButtonData(
                        id: R.string.PwNameValkoina,
                        drawerId: DrawerItemId.PwNames,
                        generator: PwNames.generators.get("Valkoina"),
                ),
                new ButtonData(
                        id: R.string.PwPlaces,
                        drawerId: DrawerItemId.PwNames,
                        generator: new PwPlace(),
                ),
                new ButtonData(
                        id: R.string.PwRegions,
                        drawerId: DrawerItemId.PwNames,
                        generator: new PwRegion(),
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.PwTreasureGuarded,
                        drawerId: DrawerItemId.PwTreasure,
                        generator: new PwTreasureGuarded(),
                ),
                new ButtonData(
                        id: R.string.PwTreasureGuarded1,
                        drawerId: DrawerItemId.PwTreasure,
                        generator: PwTreasureGuarded.gg.get("1bonus"),
                ),
                new ButtonData(
                        id: R.string.PwTreasureGuarded2,
                        drawerId: DrawerItemId.PwTreasure,
                        generator: PwTreasureGuarded.gg.get("2bonus"),
                ),
                new ButtonData(
                        id: R.string.PwTreasureUnguarded,
                        drawerId: DrawerItemId.PwTreasure,
                        generator: PwTreasure.generators.get("unguarded"),
                ),
                new ButtonData(
                        id: R.string.PwTreasureItem,
                        drawerId: DrawerItemId.PwTreasure,
                        generator: PwTreasure.generators.get("item"),
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.PwNPC,
                        drawerId: DrawerItemId.PwNPC,
                        generator: new PwNPC(),
                ),
                new ButtonData(
                        id: R.string.PwNPCRural,
                        drawerId: DrawerItemId.PwNPC,
                        generator: PwNPC.generators.get("rural"),
                ),
                new ButtonData(
                        id: R.string.PwNPCUrban,
                        drawerId: DrawerItemId.PwNPC,
                        generator: PwNPC.generators.get("urban"),
                ),
                new ButtonData(
                        id: R.string.PwNPCWilderness,
                        drawerId: DrawerItemId.PwNPC,
                        generator: PwNPC.generators.get("wilderness"),
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.PwCreature,
                        drawerId: DrawerItemId.PwCreature,
                        generator: new PwCreature()
                ),
                new ButtonData(
                        id: R.string.PwCreatureBeast,
                        drawerId: DrawerItemId.PwCreature,
                        generator: PwCreature.generators.get("beast")
                ),
                new ButtonData(
                        id: R.string.PwCreatureHuman,
                        drawerId: DrawerItemId.PwCreature,
                        generator: PwCreature.generators.get("human")
                ),
                new ButtonData(
                        id: R.string.PwCreatureHumanoid,
                        drawerId: DrawerItemId.PwCreature,
                        generator: PwCreature.generators.get("humanoid")
                ),
                new ButtonData(
                        id: R.string.PwCreatureMonster,
                        drawerId: DrawerItemId.PwCreature,
                        generator: PwCreature.generators.get("monster")
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.swn_world,
                        drawerId: DrawerItemId.SwnGen,
                        generator: new SWNWorld()
                ),
                new ButtonData(
                        id: R.string.swn_alien,
                        drawerId: DrawerItemId.SwnGen,
                        generator: new SWNAlien()
                ),
                new ButtonData(
                        id: R.string.swn_animal,
                        drawerId: DrawerItemId.SwnGen,
                        generator: new SWNAnimal()
                ),
                new ButtonData(
                        id: R.string.swn_faction,
                        drawerId: DrawerItemId.SwnGen,
                        generator: new SWNfaction()
                ),
        )

        addButtons(
                new ButtonData(
                        id: R.string.swn_npc,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNnpc()
                ),
                new ButtonData(
                        id: R.string.swn_corporation,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNcorporation()
                ),
                new ButtonData(
                        id: R.string.swn_religion,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNreligion()
                ),
                new ButtonData(
                        id: R.string.swn_heresies,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNheresy()
                ),
                new ButtonData(
                        id: R.string.swn_political,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNpolitical_party()
                ),
                new ButtonData(
                        id: R.string.swn_architecture,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNarchitecture()
                ),
                new ButtonData(
                        id: R.string.swn_room_dressing,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNroom_dressing()
                ),
                new ButtonData(
                        id: R.string.swn_adv_seed,
                        drawerId: DrawerItemId.SwnQuick,
                        generator: new SWNadventure_seed()
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.swn_names_arabic,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Arabic)
                ),
                new ButtonData(
                        id: R.string.swn_names_chinese,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Chinese)
                ),
                new ButtonData(
                        id: R.string.swn_names_english,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.English)
                ),
                new ButtonData(
                        id: R.string.swn_names_indian,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Indian)
                ),
                new ButtonData(
                        id: R.string.swn_names_japanese,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Japanese)
                ),
                new ButtonData(
                        id: R.string.swn_names_nigerian,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Nigerian)
                ),
                new ButtonData(
                        id: R.string.swn_names_russian,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Russian)
                ),
                new ButtonData(
                        id: R.string.swn_names_spanish,
                        drawerId: DrawerItemId.SwnNames,
                        generator: SWNnames.generators.get(Culture.Spanish)
                ),
        )

        addButtons(
                new ButtonData(
                        id: R.string.MrCharacters,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsCharacter()
                ),
                new ButtonData(
                        id: R.string.MrMonsters,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsMonsters()
                ),
                new ButtonData(
                        id: R.string.MrMagic,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsMagic()
                ),
                new ButtonData(
                        id: R.string.MrItems,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsItems()
                ),
                new ButtonData(
                        id: R.string.MrAfflictions,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsAfflictions()
                ),
                new ButtonData(
                        id: R.string.MrPotionEffects,
                        drawerId: DrawerItemId.MazeRats,
                        generator: new MazeRatsPotionEffects()
                ),
        )
        addButtons(
                new ButtonData(
                        id: R.string.dice_roller_1d6,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d6")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d8,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d8")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d10,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d10")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d12,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d12")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d20,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d20")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d30,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d30")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d100,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d100")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d20_adv,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d20adv")
                ),
                new ButtonData(
                        id: R.string.dice_roller_1d20_disadv,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("1d20disadv")
                ),
                new ButtonData(
                        id: R.string.dice_roller_2d6,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("2d6")
                ),
                new ButtonData(
                        id: R.string.dice_roller_3d6,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("3d6")
                ),
                new ButtonData(
                        id: R.string.dice_roller_4d4,
                        drawerId: DrawerItemId.DiceRoller,
                        generator: DiceRoller.generators.get("4d4")
                ),
        )

        addButtons(
                new ButtonData(
                        id: R.string.fourth_page_artifact,
                        drawerId: DrawerItemId.FourthPage,
                        generator: new FourthPageArtifact()
                ),
                new ButtonData(
                        id: R.string.fourth_page_monster,
                        drawerId: DrawerItemId.FourthPage,
                        generator: new FourthPageMonster()
                ),
                new ButtonData(
                        id: R.string.fourth_page_city,
                        drawerId: DrawerItemId.FourthPage,
                        generator: new FourthPageCity()
                ),
                new ButtonData(
                        id: R.string.fourth_page_dungeon,
                        drawerId: DrawerItemId.FourthPage,
                        generator: new FourthPageDungeon()
                ),
        )


    }
    @NonNull
    static List<ButtonData> getButtonsForDrawerItem(@DrawerItemId int id) {
        List<ButtonData> result = drawer2Button.get(id)
        result == null ? new ArrayList<ButtonData>() : result;
    }
    @Nullable
    static ButtonData getButton(@ButtonId int id) {
        return buttons.get(id)
    }
    static ButtonData getButton(long id) {
        return buttons.get(id)
    }


}
