package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalStatisticsData;

import java.util.List;

public class AnimalStatistics {
    private final List<Integer> genomeSequence;
    private int activeGene;
    private int energy;
    private int plantsEaten = 0;
    private int childrenCount = 0;
    private int descendantsCount = 0;
    private int daysLived = 0;
    private int dayOfDeath = -1;

    public AnimalStatistics(Genome genome, int energy) {
        this.genomeSequence = genome.getSequence();
        this.activeGene = genome.getCurrGene();
        this.energy = energy;
    }

    public void updateStats(AnimalStatisticsData data, Animal animal) {
        switch (data) {
            case ADD_DAYS_LIVED -> daysLived++;
            case ADD_CHILDREN_COUNT -> childrenCount++;
            case SET_DAY_OF_DEATH -> dayOfDeath = daysLived;
            case ADD_PLANT_EATEN -> plantsEaten++;
            case UPDATE_ANIMAL_ENERGY -> energy = animal.getEnergy();
            case ADD_DESCENDANT_COUNT -> descendantsCount++;
            case UPDATE_ACTIVE_GENE -> activeGene = animal.getGenome().getCurrGene();

        }
    }

    public List<Integer> getGenomeSequence() {
        return this.genomeSequence;
    }

    public int getActivatedPartOfGenome() {
        return activeGene;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public int getDescendantsCount() {
        return descendantsCount;
    }

    public int getDaysLived() {
        return daysLived;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }


}
