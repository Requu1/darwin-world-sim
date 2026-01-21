package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.util.AnimalStatisticsData;
import agh.ics.oop.model.util.SimulationSteps;

import java.util.ArrayList;

public class SimulationFlow {
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
        simulation.getAnimals().removeIf(animal -> {
            if (animal.getEnergy() <= 0) {
                simulation.getMap().removeAnimal(animal);
                return true;
            }
            return false;
        });
    }


    private void animalsNextDayLived() {
        simulation.getSeason().nextDay();
        simulation.getAnimals().forEach(a -> a.informListeners(AnimalStatisticsData.ADD_DAYS_LIVED));
    }


    private void updateEnergyLossDueToLowTemperature() {
        if (simulation.getSeason().isWinter()) {
            simulation.getAnimals()
                    .forEach(a -> a.subtractEnergy(a
                            .calculateEnergyLoss(simulation.getSeason().getCurrentTemperature(), simulation.getAnimals(), simulation.getWarmDistance())));
        }
    }


    private void growPlants() {
        simulation.getMap().growPlants(simulation.getPlantsGrowingDaily());
    }

    private void updateDailyEnergyLoss() {
        simulation.getAnimals().forEach(a -> a.subtractEnergy(simulation.getDailyEnergyLoss()));
    }


    private void reproduceAnimalsOnTheMap() {
        simulation.getMap().reproduceAnimals();
        updateBornAnimals(simulation.getMap().getBornAnimals());
    }

    private void updateBornAnimals(ArrayList<Animal> bornAnimals) {
        bornAnimals.forEach(a -> a.addListener(new AnimalStatsUpdater()));
        simulation.getAnimals().addAll(bornAnimals);
        simulation.getMap().clearBornAnimals();
    }

    private void moveAnimals() {
        for (Animal currAnimal : simulation.getAnimals()) {
            simulation.getMap().move(currAnimal);
        }
    }
}
