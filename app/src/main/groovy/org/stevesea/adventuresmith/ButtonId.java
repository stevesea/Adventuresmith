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

package org.stevesea.adventuresmith;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        R.string.FotFChars,
        R.string.FotFSpells,
        R.string.FotFTraits,

        R.string.PwDiscovery,
        R.string.PwDanger,
        R.string.PwDungeonExplore,

        R.string.PwSteading,
        R.string.PwDungeon,
        R.string.PwNPCFollower,
        R.string.PwNameArpad,
        R.string.PwNameOloru,
        R.string.PwNameTamanarugan,
        R.string.PwNameValkoina,
        R.string.PwPlaces,
        R.string.PwRegions,

        R.string.PwTreasureGuarded,
        R.string.PwTreasureGuarded1,
        R.string.PwTreasureGuarded2,
        R.string.PwTreasureUnguarded,
        R.string.PwTreasureItem,

        R.string.PwNPC,
        R.string.PwNPCRural,
        R.string.PwNPCUrban,
        R.string.PwNPCWilderness,

        R.string.PwCreature,
        R.string.PwCreatureBeast,
        R.string.PwCreatureHuman,
        R.string.PwCreatureHumanoid,
        R.string.PwCreatureMonster,

        R.string.swn_world,
        R.string.swn_alien,
        R.string.swn_animal,
        R.string.swn_faction,

        R.string.swn_npc,
        R.string.swn_corporation,
        R.string.swn_religion,
        R.string.swn_heresies,
        R.string.swn_political,
        R.string.swn_architecture,
        R.string.swn_room_dressing,
        R.string.swn_adv_seed,

        R.string.swn_names_arabic,
        R.string.swn_names_chinese,
        R.string.swn_names_english,
        R.string.swn_names_indian,
        R.string.swn_names_japanese,
        R.string.swn_names_nigerian,
        R.string.swn_names_russian,
        R.string.swn_names_spanish,

        R.string.MrCharacters,
        R.string.MrMonsters,
        R.string.MrMagic,
        R.string.MrItems,
        R.string.MrAfflictions,
        R.string.MrPotionEffects,

        R.string.dice_roller_1d6,
        R.string.dice_roller_1d8,
        R.string.dice_roller_1d10,
        R.string.dice_roller_1d12,
        R.string.dice_roller_1d20,
        R.string.dice_roller_1d30,
        R.string.dice_roller_1d100,
        R.string.dice_roller_1d20_adv,
        R.string.dice_roller_1d20_disadv,
        R.string.dice_roller_2d6,
        R.string.dice_roller_3d6,
        R.string.dice_roller_4d4,

        R.string.fourth_page_artifact,
        R.string.fourth_page_monster,
        R.string.fourth_page_city,
        R.string.fourth_page_dungeon,

})
public @interface ButtonId {
}
