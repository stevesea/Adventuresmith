

package com.neeber.andygen

import android.app.Application
import dagger.ObjectGraph
import groovy.transform.CompileStatic

import javax.inject.Inject

public class AndyGenApp extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(
                new AndroidModule(this),
                new AndyGenModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}