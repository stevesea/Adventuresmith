package org.stevesea.rpgpad.data

import groovy.transform.CompileStatic
import org.junit.Assert
import org.junit.Test
import org.stevesea.rpgpad.Dataset
import org.stevesea.rpgpad.DatasetButton

@CompileStatic
class RpgBoyDataTest {

    @Test
    void verifyButtonsMr() {
        Assert.assertEquals([DatasetButton.MrCharacters, DatasetButton.MrMonsters, DatasetButton.MrMagic, DatasetButton.MrItems, DatasetButton.MrAfflictions, DatasetButton.MrPotionEffects],
                DatasetButton.getButtonsForDataset(Dataset.MazeRats))
    }
    @Test
    void verifyButtonsPw() {
        Assert.assertEquals([DatasetButton.PerilousPlaces, DatasetButton.PerilousRegions],
                DatasetButton.getButtonsForDataset(Dataset.ThePerilousWilds))
    }
    @Test
    void verifyButtonsFotF() {
        Assert.assertEquals([DatasetButton.FreebooterSpells, DatasetButton.FreebooterTraits],
                DatasetButton.getButtonsForDataset(Dataset.FreebootersOnTheFrontier))
    }
}
