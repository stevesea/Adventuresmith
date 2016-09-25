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

    def pick(List<?> items, int num) {
        def local = items.collect()
        Collections.shuffle(local)
        return local.take(num)
    }

}
