package agh.ics.oop.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenomeGenerator {
    private static final int DIRECTIONS_COUNT = 8;
    private static final Random generator = new Random();

    public static ArrayList<Integer> generateNewGenome(int genomeLength) {
        ArrayList<Integer> newGenome = new ArrayList<>();
        for (int i = 0; i < genomeLength; i++) {
            newGenome.add(generator.nextInt(DIRECTIONS_COUNT));
        }
        return newGenome;
    }

    public static List<Integer> generateGenomeFromReproducing(ArrayList<Integer> genomX, ArrayList<Integer> genomY) {
        List<Integer> newGenome = List.copyOf(genomX);
        newGenome.addAll(genomY);
        return newGenome;

    }
}
