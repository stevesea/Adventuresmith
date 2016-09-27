/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.steavesea.rpg_boy2000.data

import groovy.transform.CompileStatic

import javax.inject.Inject
import javax.inject.Singleton;

@CompileStatic
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

    public Set<String> getDatasets() {
        return buttonLists.keySet()
    }

}
