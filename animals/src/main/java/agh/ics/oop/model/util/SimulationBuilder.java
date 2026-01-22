package agh.ics.oop.model.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;

public class SimulationBuilder {
    private RectangularMap map;
    private ArrayList<Vector2d> animalsPositions;
    private int startingAnimalEnergy;
    private int plantsGrowingDaily;
    private int genomeLength;
    private int dailyEnergyLoss;
    private int seasonDuration;
    private double minTemperature;
    private double warmDistance;

    public SimulationBuilder withSeasonDuration(int seasonDuration) {
        this.seasonDuration = seasonDuration;
        return this;
    }

    public SimulationBuilder withMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
        return this;
    }

    public SimulationBuilder withDailyEnergyLoss(int dailyEnergyLoss) {
        this.dailyEnergyLoss = dailyEnergyLoss;
        return this;
    }

    public SimulationBuilder withMap(RectangularMap map) {
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

    public SimulationBuilder withWarmDistance(double warmDistance) {
        this.warmDistance = warmDistance;
        return this;
    }

    public Simulation build() {
        return new Simulation(
                animalsPositions,
                map,
                seasonDuration,
                minTemperature,
                startingAnimalEnergy,
                plantsGrowingDaily,
                dailyEnergyLoss,
                genomeLength,
                warmDistance

        );
    }
}
