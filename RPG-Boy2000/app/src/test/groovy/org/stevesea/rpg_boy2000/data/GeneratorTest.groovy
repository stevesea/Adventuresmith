package org.stevesea.rpg_boy2000.data

import org.junit.Test
import org.steavesea.rpg_boy2000.data.Shuffler
import org.steavesea.rpg_boy2000.data.freebooters_on_the_frontier.FotFSpells
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwPlace
import org.steavesea.rpg_boy2000.data.perilous_wilds.PwRegion

class GeneratorTest {

    Shuffler shuffler = new Shuffler(new Random());

    PwRegion region = new PwRegion(shuffler);
    PwPlace place = new PwPlace(shuffler);
    FotFSpells spells = new FotFSpells(shuffler);

    @Test
    public void test() {
        printf region.generate(4).toString() + "\n"
        printf place.generate(4).toString() + "\n"
        printf spells.generate(10).toString() + "\n"
    }
}
