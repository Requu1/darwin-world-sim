package agh.ics.oop.simulation;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.AnimalStatsUpdater;
import agh.ics.oop.animal.Genome;
import agh.ics.oop.map.RectangularMap;
import agh.ics.oop.other.*;
import agh.ics.oop.animal.AnimalBuilder;
import agh.ics.oop.util.GenomeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static agh.ics.oop.simulation.SimulationSteps.*;

public class Simulation implements Runnable {

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final RectangularMap map;
    private final int startingAnimalEnergy;
    private final int genomeLength;
    private final int plantsGrowingDaily;
    private final int dailyEnergyLoss;
    private final double warmDistance;
    private final int energyRestoredByPlant;
    private final int minimalEnergyForReproduction;
    private final int usedEnergyForReproduction;
    private final int minMutationCount;
    private final int maxMutationCount;

    private final SeasonManager season;
    private final List<SimulationStats> statsHistory = new ArrayList<>();
    private final SimulationFlow simulationStepper;

    private final List<SimulationChangeListener> listeners = new ArrayList<>();

    private void informListeners() {
        for (SimulationChangeListener listener : listeners) {
            listener.simulationChanged(this);
        }
    }

    public void addListener(SimulationChangeListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(SimulationChangeListener listener) {
        this.listeners.remove(listener);
    }

    private boolean paused = false;
    private final Object pauseLock = new Object();

    public void togglePause() {
        paused = !paused;
        if (!paused) {
            synchronized (pauseLock) {
                pauseLock.notifyAll();
            }
        }
    }

    public Simulation(
            ArrayList<Vector2d> animalsPositions,
            RectangularMap map,
            int seasonDuration,
            double minTemperature,
            int startingAnimalEnergy,
            int plantsGrowingDaily,
            int dailyEnergyLoss,
            int genomeLength,
            double warmDistance,
            int energyRestoredByPlant,
            int minimalEnergyForReproduction,
            int usedEnergyForReproduction,
            int minMutationCount,
            int maxMutationCount,
            int plantsStartingCount
    ) {
        this.map = map;
        this.startingAnimalEnergy = startingAnimalEnergy;
        this.plantsGrowingDaily = plantsGrowingDaily;
        this.dailyEnergyLoss = dailyEnergyLoss;
        this.genomeLength = genomeLength;
        this.warmDistance = warmDistance;
        this.energyRestoredByPlant = energyRestoredByPlant;
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        this.season = new SeasonManager(seasonDuration, minTemperature);
        initializeAnimals(animalsPositions);
        map.growPlants(plantsStartingCount);
        simulationStepper = new SimulationFlow(this);
        updateStats();
    }

    private void initializeAnimals(List<Vector2d> animalsPositions) {
        for (Vector2d pos : animalsPositions) {
            Animal animal = new AnimalBuilder()
                    .withEnergy(startingAnimalEnergy)
                    .withGenome(new Genome(GenomeGenerator.generateNewGenomeSequence(genomeLength)))
                    .withPosition(pos)
                    .createAnimal();
            animal.addListener(new AnimalStatsUpdater());
            this.animals.add(animal);
            map.place(animal);
        }
    }

    @Override
    public void run() {
        int animalsCount = animals.size();
        if (animalsCount > 0) {
            while (!allAnimalsDead()) {

                synchronized (pauseLock) {
                    while (paused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                simulationStepper.phaseNextSimulationStep(GROWING_PLANTS);
                simulationStepper.phaseNextSimulationStep(MOVING_ANIMALS);
                simulationStepper.phaseNextSimulationStep(ANIMALS_REPRODUCTION);
                simulationStepper.phaseNextSimulationStep(UPDATE_DAILY_ENERGY_LOSS);
                simulationStepper.phaseNextSimulationStep(UPDATE_WEATHER_CONDITIONS);
                simulationStepper.phaseNextSimulationStep(CHECKING_ANIMALS_HEALTH);
                updateStats();
                informListeners();
                if (allAnimalsDead()) {
                    break;
                }
                delay();
                simulationStepper.phaseNextSimulationStep(NEXT_DAY);
            }

        } else {
            System.out.println("No animals to move");
        }
    }

    private void delay() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateStats() {
        SimulationStats newStats = new SimulationStats(this);
        statsHistory.add(newStats);
    }

    private boolean allAnimalsDead() {
        for (Animal animal : animals) {
            if (animal.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public List<SimulationStats> getStatsHistory() {
        return List.copyOf(this.statsHistory);
    }

    public SimulationStats getLastSimulationStats() {
        if (statsHistory.isEmpty()) {
            return null;
        } else {
            return statsHistory.get(statsHistory.size() - 1);
        }
    }

    public int getEnergyRestoredByPlant() {
        return energyRestoredByPlant;
    }

    public int getMinimalEnergyForReproduction() {
        return minimalEnergyForReproduction;
    }

    public int getUsedEnergyForReproduction() {
        return usedEnergyForReproduction;
    }

    public int getMinMutationCount() {
        return minMutationCount;
    }

    public int getMaxMutationCount() {
        return maxMutationCount;
    }

    public RectangularMap getMap() {
        return this.map;
    }

    public int getDailyEnergyLoss() {
        return this.dailyEnergyLoss;
    }

    public List<Animal> getAllAnimals() {
        return this.animals;
    }

    public List<Animal> getAliveAnimals() {
        return this.animals.stream()
                .filter(Animal::isAlive)
                .collect(Collectors.toList());
    }

    public SeasonManager getSeason() {
        return season;
    }

    public int getPlantsGrowingDaily() {
        return plantsGrowingDaily;
    }

    public double getWarmDistance() {
        return warmDistance;
    }


}


