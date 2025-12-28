package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.UUID;


public interface WorldMap extends MoveValidator {

    void place(Animal animal) throws IncorrectPositionException;

    void move(Animal animal);

    ArrayList<WorldElement> getElements();

    Boundary getCurrentBounds();

    UUID getId();

    void updateEnergy(Animal animal, int amount);

    void growPlant();

    void removeAnimal(Animal deadAnimal);

    ArrayList<Animal> getBornAnimals();

    void clearBornAnimals();
}