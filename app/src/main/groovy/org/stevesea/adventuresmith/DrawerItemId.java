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
        DrawerItemId.PwHeader,
        DrawerItemId.PwDiscoveriesAndDangers,
        DrawerItemId.PwNPC,
        DrawerItemId.PwCreature,
        DrawerItemId.PwTreasure,
        DrawerItemId.PwNames,
        DrawerItemId.SwnHeader,
        DrawerItemId.SwnGen,
        DrawerItemId.SwnQuick,
        DrawerItemId.SwnNames,
        DrawerItemId.Other,
        DrawerItemId.MazeRats,
        DrawerItemId.FourthPage,
        DrawerItemId.FotF,
        DrawerItemId.DiceRoller,
        DrawerItemId.Attribution,
        DrawerItemId.About,
})
public @interface DrawerItemId {

    int PwHeader = 1000;
    int PwDiscoveriesAndDangers = 1001;
    int PwNPC = 1002;
    int PwCreature = 1003;
    int PwTreasure = 1004;
    int PwNames = 1005;

    int SwnHeader = 2000;
    int SwnGen = 2001;
    int SwnQuick = 2002;
    int SwnNames = 2003;

    int Other = 3000;
    int MazeRats = 3001;
    int FourthPage = 3002;
    int FotF = 3003;
    int DiceRoller = 3004;

    int Attribution = 9998;
    int About = 9999;

}
