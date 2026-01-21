package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

public interface AnimalChangeListener {
    void animalChanged(AnimalStatisticsData data, Animal animal);
}
