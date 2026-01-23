package agh.ics.oop.model;

import java.util.function.Function;

public enum StatisticsType {
    ANIMAL_COUNT("Animals count", SimulationStats::getAnimalsCount),
    AVG_ENERGY("Average energy", SimulationStats::getAvgEnergyLevel);

    private final String label;
    private final Function<SimulationStats, Number> valueExtractor;

    StatisticsType(String label, Function<SimulationStats, Number> valueExtractor) {
        this.label = label;
        this.valueExtractor = valueExtractor;
    }

    public Number apply(SimulationStats stats) {
        return valueExtractor.apply(stats);
    }

    @Override
    public String toString() {
        return label;
    }
}
