package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.util.AnimalBuilder;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.util.GenomeGenerator.generateNewGenome;

public class Simulation implements Runnable {
    private static final int DAYS = 100;

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    private final int startingAnimalEnergy;
    private final int genomeLength;
    private final int plantsGrowingDaily;
    private final int dailyEnergyLoss;


    public Simulation(
            ArrayList<Vector2d> animalsPositions,
            WorldMap map,
            int startingAnimalEnergy,
            int plantsGrowingDaily,
            int dailyEnergyLoss,
            int genomeLength
    ) {
        this.map = map;
        this.startingAnimalEnergy = startingAnimalEnergy;
        this.plantsGrowingDaily = plantsGrowingDaily;
        this.dailyEnergyLoss = dailyEnergyLoss;
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
            for (int i = 0; i < DAYS; i++) {
                map.clearBornAnimals();
                checkForDeadAnimals();
                growPlants();
                moveAnimals();
                updateBornAnimals(map.getBornAnimals());
                updateDailyEnergyLoss();
            }

        } else {
            System.out.println("No animals to move");
        }
    }

    private void updateBornAnimals(ArrayList<Animal> bornAnimals) {
        this.animals.addAll(bornAnimals);
    }

    private void growPlants() {
        for (int j = 0; j < plantsGrowingDaily; j++) {
            map.growPlant();
        }
    }

    private void updateDailyEnergyLoss() {
        for (Animal animal : animals) {
            animal.updateEnergy(-dailyEnergyLoss);
        }
    }

    private void moveAnimals() {
        for (Animal currAnimal : this.animals) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.move(currAnimal);
        }
    }


    private void checkForDeadAnimals() {
        for (Animal animal : this.animals) {
            if (animal.getEnergy() < 0) {
                this.animals.remove(animal);
                map.removeAnimal(animal);
            }
        }
    }

}


