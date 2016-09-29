package org.stevesea.rpg_boy2000.data.perilous_wilds

import dagger.Module
import groovy.transform.CompileStatic

@CompileStatic
@Module(
        injects = [
                PwPlace.class,
                PwRegion.class,
        ],
        library = true,
        complete = false
)
class PwDataModule {
}
