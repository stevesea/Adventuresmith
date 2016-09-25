package org.steavesea.rpg_boy2000

import android.app.Application
import dagger.ObjectGraph
import org.steavesea.rpg_boy2000.data.RbgBoyDataModule

public class RpgBoy2000App extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(
                new AndroidModule(this),
                new RpgBoy2000Module(),
                new RbgBoyDataModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
