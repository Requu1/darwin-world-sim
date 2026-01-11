package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Genome;
import agh.ics.oop.model.Vector2d;

public class AnimalBuilder {
    private int energy;
    private Genome genome;
    private Vector2d posVector;

    public AnimalBuilder withEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public AnimalBuilder withGenome(Genome genome) {
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

