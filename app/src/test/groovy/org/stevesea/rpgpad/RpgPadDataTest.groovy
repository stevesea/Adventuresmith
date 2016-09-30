package org.stevesea.rpgpad

import groovy.transform.CompileStatic
import org.junit.Assert
import org.junit.Test

@CompileStatic
class RpgPadDataTest {

    @Test
    void verifyButtonsMr() {
        Assert.assertEquals([DatasetButton.MrCharacters, DatasetButton.MrMonsters, DatasetButton.MrMagic, DatasetButton.MrItems, DatasetButton.MrAfflictions, DatasetButton.MrPotionEffects],
                DatasetButton.getButtonsForDataset(Dataset.MazeRats))
    }

    @Test
    void verifyButtonsPw() {
        Assert.assertEquals([DatasetButton.PerilousPlaces, DatasetButton.PerilousRegions, DatasetButton.PerilousDetails, DatasetButton.PerilousDiscovery],
                DatasetButton.getButtonsForDataset(Dataset.ThePerilousWilds))
    }

    @Test
    void verifyButtonsFotF() {
        Assert.assertEquals([DatasetButton.FreebooterSpells, DatasetButton.FreebooterTraits],
                DatasetButton.getButtonsForDataset(Dataset.FreebootersOnTheFrontier))
    }
}
