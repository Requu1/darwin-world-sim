package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenomeGenerator {
    private static final int DIRECTIONS_COUNT = 8;
    private static final Random random = new Random();

    public static List<Integer> generateNewGenomeSequence(int genomeLength) {
        ArrayList<Integer> newGenome = new ArrayList<>();
        for (int i = 0; i < genomeLength; i++) {
            newGenome.add(random.nextInt(DIRECTIONS_COUNT));
        }
        return newGenome;
    }

    public static ArrayList<Integer> generateGenomeFromReproducing(Animal animal1, Animal animal2, int minMutations, int maxMutations) {
        int genomeSize = animal1.getGenome().getSequence().size();

        Animal stronger;
        Animal weaker;
        if (animal1.getEnergy() > animal2.getEnergy()) {
            stronger = animal1;
            weaker = animal2;
        } else {
            stronger = animal2;
            weaker = animal1;
        }

        double totalEnergy = animal1.getEnergy() + animal2.getEnergy();
        double strongerRatio = stronger.getEnergy() / totalEnergy;
        int splitPoint = (int) Math.round(strongerRatio * genomeSize);

        boolean strongerOnLeft = random.nextBoolean();
        ArrayList<Integer> childGenome = new ArrayList<>();

        if (strongerOnLeft) {
            childGenome.addAll(stronger.getGenome().getSequence().subList(0, splitPoint));
            childGenome.addAll(weaker.getGenome().getSequence().subList(splitPoint, genomeSize));
        } else {
            childGenome.addAll(weaker.getGenome().getSequence().subList(0, genomeSize - splitPoint));
            childGenome.addAll(stronger.getGenome().getSequence().subList(genomeSize - splitPoint, genomeSize));
        }

        ArrayList<Integer> mutationsIndices = new ArrayList<>();
        for (int i = 0; i < genomeSize; i++) {
            mutationsIndices.add(i);
        }
        Collections.shuffle(mutationsIndices);

        int mutationsCount = random.nextInt(maxMutations - minMutations + 1) + minMutations;

        for (int i = 0; i < mutationsCount && i < genomeSize; i++) {
            int indexToMutate = mutationsIndices.get(i);
            int newValue = random.nextInt(DIRECTIONS_COUNT);
            while (newValue == childGenome.get(indexToMutate)) {
                newValue = random.nextInt(DIRECTIONS_COUNT);
            }
            childGenome.set(indexToMutate, newValue);
        }

        return childGenome;
    }
}
