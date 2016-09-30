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
import org.stevesea.rpgpad.data.freebooters_on_the_frontier.FotFSpells
import org.stevesea.rpgpad.data.freebooters_on_the_frontier.FotFTraits
import org.stevesea.rpgpad.data.maze_rats.MazeRatsAfflictions
import org.stevesea.rpgpad.data.maze_rats.MazeRatsCharacter
import org.stevesea.rpgpad.data.maze_rats.MazeRatsItems
import org.stevesea.rpgpad.data.maze_rats.MazeRatsMagic
import org.stevesea.rpgpad.data.maze_rats.MazeRatsMonsters
import org.stevesea.rpgpad.data.maze_rats.MazeRatsPotionEffects
import org.stevesea.rpgpad.data.perilous_wilds.PwDetails
import org.stevesea.rpgpad.data.perilous_wilds.PwDiscovery
import org.stevesea.rpgpad.data.perilous_wilds.PwPlace
import org.stevesea.rpgpad.data.perilous_wilds.PwRegion

@CompileStatic
public enum DatasetButton {
    FreebooterSpells(Dataset.FreebootersOnTheFrontier, FotFSpells.class,  R.string.FotFSpells),
    FreebooterTraits(Dataset.FreebootersOnTheFrontier, FotFTraits.class, R.string.FotFTraits),

    PerilousPlaces(Dataset.ThePerilousWilds, PwPlace.class,  R.string.PwPlaces),
    PerilousRegions(Dataset.ThePerilousWilds, PwRegion.class, R.string.PwRegions),
    PerilousDetails(Dataset.ThePerilousWilds, PwDetails.class, R.string.PwDetails),
    PerilousDiscovery(Dataset.ThePerilousWilds, PwDiscovery.class, R.string.PwDiscovery),

    MrCharacters(Dataset.MazeRats, MazeRatsCharacter.class, R.string.MrCharacters),
    MrMonsters(Dataset.MazeRats, MazeRatsMonsters.class, R.string.MrMonsters),
    MrMagic(Dataset.MazeRats, MazeRatsMagic.class, R.string.MrMagic),
    MrItems(Dataset.MazeRats, MazeRatsItems.class, R.string.MrItems),
    MrAfflictions(Dataset.MazeRats, MazeRatsAfflictions.class, R.string.MrAfflictions),
    MrPotionEffects(Dataset.MazeRats, MazeRatsPotionEffects.class, R.string.MrPotionEffects),
    ;

    int stringResourceId
    Dataset dataset
    Class<? extends AbstractGenerator> clz;

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
}
