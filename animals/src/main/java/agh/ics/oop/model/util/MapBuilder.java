package agh.ics.oop.model.util;

import agh.ics.oop.model.RectangularMap;

public class MapBuilder {
    private int width;
    private int height;
    private int energyRestoredByPlant;
    private int plantsGrowingDaily;
    private int dailyEnergyLoss;
    private int minimalEnergyForReproduction;
    private int usedEnergyForReproduction;
    private int minMutationCount;
    private int maxMutationCount;

    public MapBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    public MapBuilder withHeight(int height) {
        this.height = height;
        return this;
    }

    public MapBuilder withEnergyRestoredByPlant(int energyRestoredByPlant) {
        this.energyRestoredByPlant = energyRestoredByPlant;
        return this;
    }

    public MapBuilder withPlantsGrowingDaily(int plantsGrowingDaily) {
        this.plantsGrowingDaily = plantsGrowingDaily;
        return this;
    }

    public MapBuilder withDailyEnergyLoss(int dailyEnergyLoss) {
        this.dailyEnergyLoss = dailyEnergyLoss;
        return this;
    }

    public MapBuilder withMinimalEnergyForReproduction(int minimalEnergyForReproduction) {
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        return this;
    }

    public MapBuilder withUsedEnergyForReproduction(int usedEnergyForReproduction) {
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        return this;
    }

    public MapBuilder withMinMutationsCount(int minMutationCount) {
        this.minMutationCount = minMutationCount;
        return this;
    }

    public MapBuilder withMaxMutationsCount(int maxMutationCount) {
        this.maxMutationCount = maxMutationCount;
        return this;
    }

    public RectangularMap create() {
        return new RectangularMap(width, height, energyRestoredByPlant, plantsGrowingDaily, dailyEnergyLoss,
                minimalEnergyForReproduction, usedEnergyForReproduction, minMutationCount, maxMutationCount);
    }


}
