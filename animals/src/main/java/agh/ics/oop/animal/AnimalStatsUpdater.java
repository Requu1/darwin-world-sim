package agh.ics.oop.animal;

public class AnimalStatsUpdater implements AnimalChangeListener {

    @Override
    public synchronized void animalChanged(AnimalStatisticsData data, Animal animal) {
        animal.getStats().updateStats(data, animal);
    }
}
