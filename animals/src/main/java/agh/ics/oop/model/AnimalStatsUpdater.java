package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalChangeListener;
import agh.ics.oop.model.util.AnimalStatisticsData;

public class AnimalStatsUpdater implements AnimalChangeListener {

    @Override
    public synchronized void animalChanged(AnimalStatisticsData data, Animal animal) {
        animal.getStats().updateStats(data, animal);
    }
}
