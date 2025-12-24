package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalBuilder;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.util.GenomeGenerator.generateNewGenome;

public class Simulation implements Runnable {
    private static final int DAYS = 100;

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final ArrayList<Plant> plant = new ArrayList<>();
    private final WorldMap map;

    private final int startingPlantCount;
    private final int startingAnimalCount;
    private final int startingAnimalEnergy;
    private final int genomeLength;


    public Simulation(
            ArrayList<Vector2d> animalsPositions,
            ArrayList<Vector2d> plantsPositions,
            WorldMap map,
            int startingPlantCount,
            int startingAnimalCount,
            int startingAnimalEnergy,
            int genomeLength
    ) {
        this.map = map;
        this.startingPlantCount = startingPlantCount;
        this.startingAnimalCount = startingAnimalCount;
        this.startingAnimalEnergy = startingAnimalEnergy;
        this.genomeLength = genomeLength;
        initializeAnimalsAndPlants(animalsPositions, plantsPositions);
    }


    private void initializeAnimalsAndPlants(List<Vector2d> animalsPositions, List<Vector2d> plantsPositions) {
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

        for (Vector2d pos : plantsPositions) {
            try {
                map.growPlant(new Plant(pos));
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
                Animal currAnimal = this.animals.get(currAnimalIndex);
                map.move(currAnimal);
                map.updateEnergy(currAnimal);
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
