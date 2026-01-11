package agh.ics.oop.model;

import java.util.List;

public class AnimalStatistics {
    private final List<Integer> genomeSequence;
    private final int activatedPartOfGenome;
    private int energy;
    private int plantsEaten = 0;
    private int childrenCount = 0;
    private int descendantsCount = 0;
    private int daysLived = 0;
    private int dayOfDeath = -1;

    public AnimalStatistics(Genome genome, int energy) {
        this.genomeSequence = genome.getSequence();
        this.activatedPartOfGenome = genome.getCurrGenomePart();
        this.energy = energy;
    }

    public void addPlantEaten() {
        this.plantsEaten += 1;
    }

    public void updateEnergy(int currEnergy) {
        this.energy = currEnergy;
    }

    public void addChildren() {
        this.childrenCount += 1;
    }

    public void addDescendant() {
        this.descendantsCount += 1;
    }

    public void addDayLived() {
        this.daysLived += 1;
    }

    public void setDayOfDeath(int day) {
        this.dayOfDeath = day;
    }
}
