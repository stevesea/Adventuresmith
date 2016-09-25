package org.neeber.andygen.data

import org.neeber.andygen.data.freebooters_on_the_frontier.FotFSpells
import org.neeber.andygen.data.perilous_wilds.PwPlace
import org.neeber.andygen.data.perilous_wilds.PwRegion
import org.junit.Test;

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
