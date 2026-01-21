package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalBuilder;
import agh.ics.oop.model.util.GenomeGenerator;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.util.SimulationSteps.*;

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

    private final SimulationFlow simulationStepper;

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
        simulationStepper = new SimulationFlow(this);
    }

    public WorldMap getMap() {
        return this.map;
    }

    public int getDailyEnergyLoss() {
        return this.dailyEnergyLoss;
    }

    public List<Animal> getAnimals() {
        return this.animals;
    }


    public SeasonManager getSeason() {
        return season;
    }

    public int getStartingAnimalEnergy() {
        return startingAnimalEnergy;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public int getPlantsGrowingDaily() {
        return plantsGrowingDaily;
    }

    public double getWarmDistance() {
        return warmDistance;
    }

    private void initializeAnimals(List<Vector2d> animalsPositions) {
        for (Vector2d pos : animalsPositions) {
            Animal animal = new AnimalBuilder()
                    .withEnergy(startingAnimalEnergy)
                    .withGenome(new Genome(GenomeGenerator.generateNewGenomeSequence(genomeLength)))
                    .withPosition(pos)
                    .createAnimal();
            animal.addListener(new AnimalStatsUpdater());
            map.place(animal);
            this.animals.add(animal);
        }
    }

    @Override
    public void run() {
        int animalsCount = animals.size();
        if (animalsCount > 0) {
            for (int step = 0; step < SIMULATION_STEPS; step++) {

                if (animals.isEmpty()) {
                    break;
                }

                simulationStepper.phaseNextSimulationStep(GROWING_PLANTS);
                delayStep();
                simulationStepper.phaseNextSimulationStep(MOVING_ANIMALS);
                delayStep();
                simulationStepper.phaseNextSimulationStep(ANIMALS_REPRODUCTION);
                delayStep();
                simulationStepper.phaseNextSimulationStep(UPDATE_DAILY_ENERGY_LOSS);
                delayStep();
                simulationStepper.phaseNextSimulationStep(UPDATE_WEATHER_CONDITIONS);
                delayStep();
                simulationStepper.phaseNextSimulationStep(CHECKING_ANIMALS_HEALTH);
                delayStep();
                simulationStepper.phaseNextSimulationStep(NEXT_DAY);
                delayStep();

            }

        } else {
            System.out.println("No animals to move");
        }
    }

    private void delayStep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


