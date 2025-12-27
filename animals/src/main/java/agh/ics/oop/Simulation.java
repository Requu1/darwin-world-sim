package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.util.AnimalBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                ArrayList<Animal> deadAnimals = new ArrayList<>();
                for (Animal currAnimal : this.animals) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    map.move(currAnimal);
                    if(currAnimal.getEnergy()>)
                        tryToReproduce(map.getAnimals(),currAnimal);
                    }
                    currAnimal.updateEnergy(-dailyEnergyLoss);

                    if (currAnimal.getEnergy() <= 0) {
                        animals.remove(currAnimal);
                        deadAnimals.add(currAnimal);
                    }
                }
                for (int j = 0; j < plantsGrowingDaily; j++) {
                    map.growPlant();
                }

                for (Animal deadAnimal : deadAnimals) {
                    map.removeAnimal(deadAnimal);
                }

            }
        } else {
            System.out.println("No animals to move");
        }
    }

    private boolean tryToReproduce(Map<Vector2d,Animal> animals,Animal animal){
        if()
    }

    private void createNewAnimal(Animal X, Animal Y) {

    }

}
