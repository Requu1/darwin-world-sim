package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.UUID;


public interface WorldMap {

    void place(Animal animal);

    void move(Animal animal);

    Boundary getCurrentBounds();

    UUID getId();

    void growPlants(int plantsCount);

    void removeAnimal(Animal deadAnimal);

    ArrayList<Animal> getBornAnimals();

    void clearBornAnimals();

    void reproduceAnimals();
}