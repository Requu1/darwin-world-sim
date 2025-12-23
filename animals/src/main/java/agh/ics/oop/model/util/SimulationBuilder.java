package agh.ics.oop.model.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;

public class SimulationBuilder {
    private WorldMap map;
    private ArrayList<Vector2d> positions;
    private int startingPlantCount;
    private int energyRestoredByPlant;
    private int plantsGrowingDaily;
    private int startingAnimalCount;
    private int startingAnimalEnergy;
    private int dailyEnergyLoss;
    private int minimalEnergyForReproduction;
    private int usedEnergyForReproduction;
    private int minMutationCount;
    private int maxMutationCount;
    private int genomeLength;

    public SimulationBuilder withMap(WorldMap map) {
        this.map = map;
        return this;
    }

    public SimulationBuilder withPositions(ArrayList<Vector2d> positions) {
        this.positions = positions;
        return this;
    }

    public SimulationBuilder withStartPlantCount(int startingPlantCount) {
        this.startingPlantCount = startingPlantCount;
        return this;
    }

    public SimulationBuilder withEnergyRestoredByPlant(int energyRestoredByPlant) {
        this.energyRestoredByPlant = energyRestoredByPlant;
        return this;
    }

    public SimulationBuilder withPlantsGrowingDaily(int plantsGrowingDaily) {
        this.plantsGrowingDaily = plantsGrowingDaily;
        return this;
    }

    public SimulationBuilder withStartingAnimalCount(int startingAnimalCount) {
        this.startingAnimalCount = startingAnimalCount;
        return this;
    }

    public SimulationBuilder withStartingAnimalEnergy(int startingAnimalEnergy) {
        this.startingAnimalEnergy = startingAnimalEnergy;
        return this;
    }

    public SimulationBuilder withDailyEnergyLoss(int dailyEnergyLoss) {
        this.dailyEnergyLoss = dailyEnergyLoss;
        return this;
    }

    public SimulationBuilder withMinimalEnergyForReproduction(int minimalEnergyForReproduction) {
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        return this;
    }

    public SimulationBuilder withUsedEnergyForReproduction(int usedEnergyForReproduction) {
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        return this;
    }

    public SimulationBuilder withMinMutationCount(int minMutationCount) {
        this.minMutationCount = minMutationCount;
        return this;
    }

    public SimulationBuilder withMaxMutationCount(int maxMutationCount) {
        this.maxMutationCount = maxMutationCount;
        return this;
    }

    public SimulationBuilder withGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
        return this;
    }


    public Simulation build() {
        return new Simulation(
                positions,
                map,
                startingPlantCount,
                energyRestoredByPlant,
                plantsGrowingDaily,
                startingAnimalCount,
                startingAnimalEnergy,
                dailyEnergyLoss,
                minimalEnergyForReproduction,
                usedEnergyForReproduction,
                minMutationCount,
                maxMutationCount,
                genomeLength
        );
    }
}
