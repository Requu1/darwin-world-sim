package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalBuilder;
import agh.ics.oop.model.util.GenomeGenerator;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final static int SIMULATION_STEPS = 100;

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    private final int startingAnimalEnergy;
    private final int genomeLength;
    private final int plantsGrowingDaily;
    private final int dailyEnergyLoss;
    private final double warmDistance;
    private final SeasonManager season;


    public Simulation(
            ArrayList<Vector2d> animalsPositions,
            WorldMap map,
            int seasonDuration,
            double minTemperature,
            int startingAnimalEnergy,
            int plantsGrowingDaily,
            int dailyEnergyLoss,
            int genomeLength,
            double warmDistance
    ) {
        this.map = map;
        this.startingAnimalEnergy = startingAnimalEnergy;
        this.plantsGrowingDaily = plantsGrowingDaily;
        this.dailyEnergyLoss = dailyEnergyLoss;
        this.genomeLength = genomeLength;
        this.warmDistance = warmDistance;
        this.season = new SeasonManager(seasonDuration, minTemperature);
        initializeAnimals(animalsPositions);
    }


    private void initializeAnimals(List<Vector2d> animalsPositions) {
        for (Vector2d pos : animalsPositions) {
            Animal animal = new AnimalBuilder()
                    .withEnergy(startingAnimalEnergy)
                    .withGenome(new Genome(GenomeGenerator.generateNewGenomeSequence(genomeLength)))
                    .withPosition(pos)
                    .createAnimal();
            map.place(animal);
            this.animals.add(animal);
        }
    }

    @Override
    public void run() {
        int animalsCount = animals.size();
        if (animalsCount > 0) {
            for (int step = 0; step < SIMULATION_STEPS; step++) {
                map.clearBornAnimals();
                animals.removeIf(animal -> {
                    if (animal.getEnergy() < 0) {
                        map.removeAnimal(animal);
                        return true;
                    }
                    return false;
                });

                if (animals.isEmpty()) {
                    break;
                }


                growPlants();
                moveAnimals();
                updateBornAnimals(map.getBornAnimals());

                this.animals.forEach(animal -> {
                    updateDailyEnergyLoss(animal);
                    updateEnergyLossDueToLowTemperature(animal);
                });


                season.nextDay();
            }

        } else {
            System.out.println("No animals to move");
        }
    }


    private void updateEnergyLossDueToLowTemperature(Animal animal) {
        if (season.isWinter()) {
            animal.subtractEnergy(animal.calculateEnergyLoss(season.getCurrentTemperature(), this.animals, warmDistance));
        }
    }


    private void updateBornAnimals(ArrayList<Animal> bornAnimals) {
        this.animals.addAll(bornAnimals);
    }

    private void growPlants() {
        map.growPlants(plantsGrowingDaily);
    }

    private void updateDailyEnergyLoss(Animal animal) {
        animal.subtractEnergy(dailyEnergyLoss);
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


}


