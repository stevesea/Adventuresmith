package org.stevesea.rpg_boy2000.data

import dagger.ObjectGraph
import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test
import org.junit.Assert

@CompileStatic
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
        Set<Dataset> expected = new HashSet<>()
        expected.addAll(Dataset.values())

        Assert.assertEquals(expected, data.getDatasets())
    }

    @Test
    void verifyButtons() {
        Assert.assertEquals(["Character", "Spells", "Items", "Monsters", "Afflictions", "Potion Effects"],
                data.getButtons(Dataset.MazeRats))
        Assert.assertEquals(["Region", "Place"],
                data.getButtons(Dataset.ThePerilousWilds))
        Assert.assertEquals(["Traits", "Spells"],
                data.getButtons(Dataset.FreebootersOnTheFrontier))
    }
}
