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
package org.stevesea.rpgpad.data.perilous_wilds

import dagger.Module
import groovy.transform.CompileStatic

@CompileStatic
@Module(
        injects = [
                PwCreature.class,
                PwCreatureBeast.class,
                PwCreatureHuman.class,
                PwCreatureHumanoid.class,
                PwCreatureMonster.class,
                PwDanger.class,
                PwDetails.class,
                PwDiscovery.class,
                PwDungeon.class,
                PwFollower.class,
                PwNPC.class,
                PwNPCRural.class,
                PwNPCUrban.class,
                PwNPCWilderness.class,
                PwPlace.class,
                PwRegion.class,
                PwSteading.class,
                PwTreasure.class,
                PwTreasureGuarded.class,
                PwTreasureGuarded1Bonus.class,
                PwTreasureGuarded2Bonus.class,
                PwTreasureItem.class,
                PwTreasureUnguarded.class,
        ],
        library = true,
        complete = false
)
class PwDataModule {
}
