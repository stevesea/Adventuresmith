package org.stevesea.rpgpad.data.freebooters_on_the_frontier

import dagger.Module
import groovy.transform.CompileStatic

@CompileStatic
@Module(
        injects = [
                FotFTraits.class,
                FotFSpells.class,
        ],
        library = true,
        complete = false
)
class FotFDataModule {
}
