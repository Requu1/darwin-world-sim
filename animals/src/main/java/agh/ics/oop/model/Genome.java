package agh.ics.oop.model;

import agh.ics.oop.model.util.GenomeGenerator;

import java.util.List;

public class Genome {
    private static final int DEFAULT_GENOME_LENGTH = 10;

    private List<Integer> genomeSequence;
    private int currGenomeIdx = 0;

    public Genome() {
        this(GenomeGenerator.generateNewGenomeSequence(DEFAULT_GENOME_LENGTH));
    }

    public Genome(List<Integer> genomeSequence) {
        this.genomeSequence = genomeSequence;
    }

    public int getCurrGene() {
        return this.genomeSequence.get(currGenomeIdx);
    }

    public void setCurrGene(int gene) {
        this.genomeSequence.set(currGenomeIdx, gene);
    }

    public void updateCurrGene() {
        currGenomeIdx = (currGenomeIdx + 1) % genomeSequence.size();
    }

    public List<Integer> getSequence() {
        return this.genomeSequence;
    }

    public void setGenomeSequence(List<Integer> newGenomeSequence) {
        this.genomeSequence = newGenomeSequence;
    }

}
