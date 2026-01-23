package agh.ics.oop.simulation;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.AnimalStatsUpdater;

import java.util.ArrayList;

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
                    .forEach(a -> a.subtractEnergy(a
                            .calculateEnergyLoss(simulation.getSeason().getCurrentTemperature(), simulation.getAliveAnimals(),
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
        simulation.getMap()
                .reproduceAnimals(simulation.getMinimalEnergyForReproduction(), simulation.getUsedEnergyForReproduction(),
                        simulation.getMinMutationCount(), simulation.getMaxMutationCount());
        updateBornAnimals(simulation.getMap().getBornAnimals());
    }

    private void updateBornAnimals(ArrayList<Animal> bornAnimals) {
        bornAnimals.forEach(a -> a.addListener(new AnimalStatsUpdater()));
        simulation.getAllAnimals().addAll(bornAnimals);
        simulation.getMap().clearBornAnimals();
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
