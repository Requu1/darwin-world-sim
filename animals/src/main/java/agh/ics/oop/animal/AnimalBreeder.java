package agh.ics.oop.animal;

import agh.ics.oop.util.GenomeGenerator;

public class AnimalBreeder {
    public static Animal reproduce(Animal animal1, Animal animal2, int usedEnergyForReproduction, int minMutationCount, int maxMutationCount) {
        Animal newBornAnimal = new AnimalBuilder()
                .withEnergy(2 * usedEnergyForReproduction)
                .withGenome(new Genome(GenomeGenerator.generateGenomeFromReproducing(animal1, animal2, minMutationCount, maxMutationCount)))
                .withPosition(animal1.getPosition())
                .createAnimal();
        animal1.addChildren();
        animal2.addChildren();
        animal1.subtractEnergy(usedEnergyForReproduction);
        animal2.subtractEnergy(usedEnergyForReproduction);
        return newBornAnimal;
    }
}
