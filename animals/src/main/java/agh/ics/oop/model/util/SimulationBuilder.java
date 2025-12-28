package agh.ics.oop.model.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;

public class SimulationBuilder {
    private WorldMap map;
    private ArrayList<Vector2d> animalsPositions;
    private int startingAnimalEnergy;
    private int plantsGrowingDaily;
    private int genomeLength;
    private int dailyEnergyLoss;

    public SimulationBuilder withDailyEnergyLoss(int dailyEnergyLoss) {
        this.dailyEnergyLoss = dailyEnergyLoss;
        return this;
    }

    public SimulationBuilder withMap(WorldMap map) {
        this.map = map;
        return this;
    }

    public SimulationBuilder withAnimalsPositions(ArrayList<Vector2d> animalsPositions) {
        this.animalsPositions = animalsPositions;
        return this;
    }


    public SimulationBuilder withStartingAnimalEnergy(int startingAnimalEnergy) {
        this.startingAnimalEnergy = startingAnimalEnergy;
        return this;
    }

    public SimulationBuilder withGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
        return this;
    }

    public SimulationBuilder withPlantsGrowingDaily(int plantsGrowingDaily) {
        this.plantsGrowingDaily = plantsGrowingDaily;
        return this;
    }


    public Simulation build() {
        return new Simulation(
                animalsPositions,
                map,
                startingAnimalEnergy,
                plantsGrowingDaily,
                dailyEnergyLoss,
                genomeLength
        );
    }
}
