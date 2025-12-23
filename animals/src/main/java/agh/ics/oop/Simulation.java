package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalBuilder;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.util.GenomeGenerator.generateNewGenome;

public class Simulation implements Runnable {
    private static final int DAYS = 100;

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final WorldMap map;

    private final int startingPlantCount;
    private final int energyRestoredByPlant;
    private final int plantsGrowingDaily;
    private final int startingAnimalCount;
    private final int startingAnimalEnergy;
    private final int dailyEnergyLoss;
    private final int minimalEnergyForReproduction;
    private final int usedEnergyForReproduction;
    private final int minMutationCount;
    private final int maxMutationCount;
    private final int genomeLength;


    public Simulation(
            ArrayList<Vector2d> animalsPositions,
            WorldMap map,
            int startingPlantCount,
            int energyRestoredByPlant,
            int plantsGrowingDaily,
            int startingAnimalCount,
            int startingAnimalEnergy,
            int dailyEnergyLoss,
            int minimalEnergyForReproduction,
            int usedEnergyForReproduction,
            int minMutationCount,
            int maxMutationCount,
            int genomeLength
    ) {
        this.map = map;
        this.startingPlantCount = startingPlantCount;
        this.energyRestoredByPlant = energyRestoredByPlant;
        this.plantsGrowingDaily = plantsGrowingDaily;
        this.startingAnimalCount = startingAnimalCount;
        this.startingAnimalEnergy = startingAnimalEnergy;
        this.dailyEnergyLoss = dailyEnergyLoss;
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        if (minMutationCount > maxMutationCount) {
            throw new IllegalArgumentException("Minimal mutation count cannot be greater than maximal.");
        }
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        this.genomeLength = genomeLength;
        initializeAnimals(animalsPositions);
    }


    private void initializeAnimals(List<Vector2d> animalsPositions) {
        for (Vector2d pos : animalsPositions) {
            try {
                Animal animal = new AnimalBuilder()
                        .withEnergy(startingAnimalEnergy)
                        .withGenome(generateNewGenome(genomeLength))
                        .withPosition(pos)
                        .createAnimal();
                map.place(animal);
                this.animals.add(animal);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int animalsCount = animals.size();

        if (animalsCount > 0) {
            int currAnimalIndex = 0;
            for (int i = 0; i < DAYS; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.move(this.animals.get(currAnimalIndex));
                currAnimalIndex = (currAnimalIndex + 1) % animalsCount;
            }
        } else {
            System.out.println("No animals to move");
        }
    }

    public ArrayList<Animal> getAnimals() {
        return this.animals;
    }
}
