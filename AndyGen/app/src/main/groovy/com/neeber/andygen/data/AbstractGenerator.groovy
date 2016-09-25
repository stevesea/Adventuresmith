package com.neeber.andygen.data

public abstract class AbstractGenerator {

    protected final Shuffler shuffler;

    AbstractGenerator(Shuffler shuffler) {
        this.shuffler = shuffler
    }

    def pick(List<?> items) {
        return shuffler.pick(items)
    }

    List<?> pick(List<?> items, int num) {
        return shuffler.pick(items, num)
    }

    abstract List<GString> getFormatters()

    String[] generate(int num) {
        List<GString> formatters = getFormatters()

        def strings = []
        num.times {
            strings << pick(formatters)
        }
        return strings as String[]
    }
}
