package agh.ics.oop.model.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;

public class SimulationBuilder {
    private WorldMap map;
    private ArrayList<Vector2d> animalsPositions;
    private ArrayList<Vector2d> plantsPositions;
    private int startingPlantCount;
    private int startingAnimalCount;
    private int startingAnimalEnergy;
    private int genomeLength;

    public SimulationBuilder withMap(WorldMap map) {
        this.map = map;
        return this;
    }

    public SimulationBuilder withAnimalsPositions(ArrayList<Vector2d> animalsPositions) {
        this.animalsPositions = animalsPositions;
        return this;
    }

    public SimulationBuilder withPlantsPositions(ArrayList<Vector2d> plantsPositions) {
        this.plantsPositions = plantsPositions;
        return this;
    }

    public SimulationBuilder withStartPlantCount(int startingPlantCount) {
        this.startingPlantCount = startingPlantCount;
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

    public SimulationBuilder withGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
        return this;
    }


    public Simulation build() {
        return new Simulation(
                animalsPositions,
                plantsPositions,
                map,
                startingPlantCount,
                startingAnimalCount,
                startingAnimalEnergy,
                genomeLength
        );
    }
}
