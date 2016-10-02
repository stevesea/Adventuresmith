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
        Assert.assertEquals(
                [
                        DatasetButton.PerilousPlaces,
                        DatasetButton.PerilousRegions,
                        DatasetButton.PerilousDiscovery,
                        DatasetButton.PerilousNPC,
                        DatasetButton.PerilousNPCWilderness,
                        DatasetButton.PerilousNPCRural,
                        DatasetButton.PerilousNPCUrban,
                        DatasetButton.PerilousNPCFollower,
                        DatasetButton.PerilousDetails,
                ],
                DatasetButton.getButtonsForDataset(Dataset.ThePerilousWilds))
    }

    @Test
    void verifyButtonsFotF() {
        Assert.assertEquals([DatasetButton.FreebooterSpells, DatasetButton.FreebooterTraits],
                DatasetButton.getButtonsForDataset(Dataset.FreebootersOnTheFrontier))
    }

    @Test
    void verifyDatasetLookupByMenuItem() {
        Assert.assertEquals(Dataset.MazeRats, Dataset.lookupDatasetForNavItem(R.id.nav_mr))
        Assert.assertEquals(Dataset.DiceRoller, Dataset.lookupDatasetForNavItem(R.id.nav_dice))
        Assert.assertEquals(Dataset.ThePerilousWilds, Dataset.lookupDatasetForNavItem(R.id.nav_pw))
        Assert.assertEquals(Dataset.FreebootersOnTheFrontier, Dataset.lookupDatasetForNavItem(R.id.nav_fotf))
    }
}
