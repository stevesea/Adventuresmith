package com.neeber.andygen.data

import javax.inject.Inject

class Shuffler {
    private final Random random;

    @Inject
    Shuffler(Random random) {
        this.random = random
    }

    def pick(List<?> items) {
        return items.get(random.nextInt(items.size()))
    }

    List<?> pick(List<?> items, int num) {
        def local = items.collect()
        Collections.shuffle(local)
        return local.take(num)
    }

    int rollDice(int numDice, int nSides) {
        int sum = 0
        numDice.times {
            sum += random.nextInt(nSides) + 1
        }
        return sum
    }
}
