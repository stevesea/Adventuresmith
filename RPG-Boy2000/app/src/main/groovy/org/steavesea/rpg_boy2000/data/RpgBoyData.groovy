package org.steavesea.rpg_boy2000.data

import javax.inject.Inject
import javax.inject.Singleton;

@Singleton
public class RpgBoyData {
    Map<String, List<String>> buttonLists = new HashMap<>()
    Map<String, Map<String, AbstractGenerator>> generatorMap = new HashMap<>()

    public static final String PERILOUS_WILDS = 'The Perilous Wilds'
    public static final String FREEBOOTERS = 'Freebooters on the Frontier'
    public static final String MAZERATS = 'Maze Rats'
    public static final String DEFAULT = MAZERATS

    @Inject
    RpgBoyData(List<AbstractGenerator> generators) {
        for (AbstractGenerator g : generators) {
            String dataset = g.getDataset()
            String name = g.getName()
            if (!generatorMap.containsKey(dataset)) {
                generatorMap.put(dataset, new HashMap<String, AbstractGenerator>());
                buttonLists.put(dataset, new ArrayList<String>())
            }
            generatorMap.get(dataset).put(name, g)
            buttonLists.get(dataset).add(name)

        }
    }

    public List<String> getButtons(String key) {
        return buttonLists.get(key, [])
    }

    public List<String> runGenerator(String dataset, String name) {
        if (!generatorMap.containsKey(dataset))
            return []
        if (!generatorMap.get(dataset).containsKey(name))
            return []
        return generatorMap.get(dataset).get(name).generate(5).toList()
    }

}
