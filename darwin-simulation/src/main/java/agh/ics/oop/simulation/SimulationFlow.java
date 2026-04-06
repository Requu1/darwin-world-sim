package agh.ics.oop.simulation;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.AnimalBreeder;
import agh.ics.oop.animal.AnimalStatsUpdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static agh.ics.oop.animal.AnimalEnergyManager.calculateEnergyLoss;

public class SimulationFlow {
    private final static double SUMMER_PLANT_COUNT_BOOST = 2;
    private final static double SUMMER_PLANT_ENERGY_BOOST = 1.5;

    private final Simulation simulation;

    public SimulationFlow(Simulation simulation) {
        this.simulation = simulation;
    }

    public void phaseNextSimulationStep(SimulationSteps step) {
        switch (step) {
            case GROWING_PLANTS -> growPlants();
            case MOVING_ANIMALS -> moveAnimals();
            case ANIMALS_REPRODUCTION -> reproduceAnimalsOnTheMap();
            case UPDATE_DAILY_ENERGY_LOSS -> updateDailyEnergyLoss();
            case UPDATE_WEATHER_CONDITIONS -> updateEnergyLossDueToLowTemperature();
            case CHECKING_ANIMALS_HEALTH -> removingAnimalsStep();
            case NEXT_DAY -> animalsNextDayLived();
        }
    }


    private void removingAnimalsStep() {
        simulation.getAllAnimals().forEach(animal -> {
            if (animal.getEnergy() <= 0) {
                simulation.getMap().removeAnimalFromTheMap(animal);
            }
        });
    }


    private void animalsNextDayLived() {
        simulation.getSeason().nextDay();
        simulation.getAliveAnimals().forEach(Animal::nextDayLived);
    }


    private void updateEnergyLossDueToLowTemperature() {
        if (simulation.getSeason().isWinter()) {
            simulation.getAliveAnimals()
                    .forEach(a -> a.subtractEnergy(
                            calculateEnergyLoss(a, simulation.getSeason().getCurrentTemperature(), simulation.getAliveAnimals(),
                                    simulation.getWarmDistance(), simulation.getMap().getCurrentBounds().upperRightCorner().x() + 1)));
        }
    }


    private void growPlants() {
        if (simulation.getSeason().isWinter()) {
            simulation.getMap().growPlants(simulation.getPlantsGrowingDaily());
        } else {
            simulation.getMap().growPlants((int) (simulation.getPlantsGrowingDaily() * SUMMER_PLANT_COUNT_BOOST));
        }
    }

    private void updateDailyEnergyLoss() {
        simulation.getAliveAnimals().forEach(a -> a.subtractEnergy(simulation.getDailyEnergyLoss()));
    }


    private void reproduceAnimalsOnTheMap() {
        for (List<Animal> animalsOnTheSamePos : this.simulation.getMap().getPositionsWithAnimals()) {
            if (animalsOnTheSamePos.size() < 2) {
                continue;
            }
            List<Animal> shuffledAnimals = new ArrayList<>(animalsOnTheSamePos);
            Collections.shuffle(shuffledAnimals);

            Comparator<Animal> animalComparator = Comparator
                    .comparingInt(Animal::getEnergy)
                    .thenComparingInt(a -> a.getStats().getDaysLived())
                    .thenComparingInt(a -> a.getStats().getChildrenCount())
                    .reversed();

            List<Animal> potentialParents = shuffledAnimals.stream()
                    .filter(a -> a.getEnergy() >= this.simulation.getMinimalEnergyForReproduction())
                    .sorted(animalComparator)
                    .limit(2)
                    .toList();

            if (potentialParents.size() == 2) {
                Animal animal1 = potentialParents.get(0);
                Animal animal2 = potentialParents.get(1);
                Animal newBornAnimal = AnimalBreeder.reproduce(animal1, animal2, this.simulation.getUsedEnergyForReproduction(),
                        this.simulation.getMinMutationCount(), this.simulation.getMaxMutationCount());
                addBornAnimal(newBornAnimal);
            }
        }
    }


    private void addBornAnimal(Animal animal) {
        animal.addListener(new AnimalStatsUpdater());
        simulation.getAllAnimals().add(animal);
        simulation.getMap().place(animal);
    }

    private void moveAnimals() {
        for (Animal currAnimal : simulation.getAliveAnimals()) {
            if (!simulation.getSeason().isWinter()) {
                simulation.getMap().move(currAnimal, (int) (simulation.getEnergyRestoredByPlant() * SUMMER_PLANT_ENERGY_BOOST));
            } else {
                simulation.getMap().move(currAnimal, simulation.getEnergyRestoredByPlant());
            }
        }
    }
}
