package org.stevesea.rpg_boy2000.data

import dagger.ObjectGraph
import org.junit.Before
import org.junit.Test
import org.junit.Assert
import org.steavesea.rpg_boy2000.data.RpgBoyData
import org.steavesea.rpg_boy2000.data.RpgBoyDataModule

class RpgBoyDataTest {
    private ObjectGraph graph;
    private RpgBoyData data;

    @Before
    void setup() {
        graph = ObjectGraph.create(
                new RpgBoyDataModule()
        )
        data = graph.get(RpgBoyData.class)
    }

    @Test
    void verifyDiscoveredExpectedDatasets() {
        Set<String> expected = new HashSet<>()
        expected.addAll([RpgBoyData.PERILOUS_WILDS, RpgBoyData.FREEBOOTERS, RpgBoyData.MAZERATS])

        Assert.assertEquals(expected, data.getDatasets())
    }

    @Test
    void verifyButtons() {
        Assert.assertEquals(["Character", "Spells", "Items", "Monsters", "Afflictions", "Potion Effects"],
                data.getButtons(RpgBoyData.MAZERATS))
        Assert.assertEquals(["Region", "Place"],
                data.getButtons(RpgBoyData.PERILOUS_WILDS))
        Assert.assertEquals(["Traits", "Spells"],
                data.getButtons(RpgBoyData.FREEBOOTERS))
    }
}
