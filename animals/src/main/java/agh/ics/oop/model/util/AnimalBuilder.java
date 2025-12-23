package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;

public class AnimalBuilder {
    private int energy;
    private ArrayList<Integer> genome;
    private Vector2d posVector;

    public AnimalBuilder withEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public AnimalBuilder withGenome(ArrayList<Integer> genome) {
        this.genome = genome;
        return this;
    }

    public AnimalBuilder withPosition(Vector2d posVector) {
        this.posVector = posVector;
        return this;
    }


    public Animal createAnimal() {
        return new Animal(posVector, energy, genome);
    }
}

