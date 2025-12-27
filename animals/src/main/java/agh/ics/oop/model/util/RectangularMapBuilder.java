package agh.ics.oop.model.util;

import agh.ics.oop.model.RectangularMap;

public class RectangularMapBuilder {
    private int width;
    private int height;
    private int startingPlantsCount;
    private int energyRestoredByPlant;
    private int minimalEnergyForReproduction;
    private int usedEnergyForReproduction;
    private int minMutationCount;
    private int maxMutationCount;

    public RectangularMapBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    public RectangularMapBuilder withStartingPlantsCount(int startingPlantsCount) {
        this.startingPlantsCount = startingPlantsCount;
        return this;
    }

    public RectangularMapBuilder withHeight(int height) {
        this.height = height;
        return this;
    }

    public RectangularMapBuilder withEnergyRestoredByPlant(int energyRestoredByPlant) {
        this.energyRestoredByPlant = energyRestoredByPlant;
        return this;
    }


    public RectangularMapBuilder withMinimalEnergyForReproduction(int minimalEnergyForReproduction) {
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        return this;
    }

    public RectangularMapBuilder withUsedEnergyForReproduction(int usedEnergyForReproduction) {
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        return this;
    }

    public RectangularMapBuilder withMinMutationsCount(int minMutationCount) {
        this.minMutationCount = minMutationCount;
        return this;
    }

    public RectangularMapBuilder withMaxMutationsCount(int maxMutationCount) {
        this.maxMutationCount = maxMutationCount;
        return this;
    }

    public RectangularMap create() {
        return new RectangularMap(width, height, energyRestoredByPlant, startingPlantsCount,
                minimalEnergyForReproduction, usedEnergyForReproduction, minMutationCount, maxMutationCount);
    }


}
