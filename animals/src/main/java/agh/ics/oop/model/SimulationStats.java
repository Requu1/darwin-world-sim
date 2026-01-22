package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationStats {
    private final Simulation simulation;

    public SimulationStats(Simulation simulation) {
        this.simulation = simulation;
    }

    public int getAvgAnimalsLifeSpan() {
        int sumOfLifeSpan = 0;
        for (Animal animal : simulation.getAllAnimals()) {
            sumOfLifeSpan += animal.getStats().getDaysLived();
        }

        return sumOfLifeSpan / simulation.getAllAnimals().size();

    }

    public int getAvgChildrenCount() {
        int sumOfChildrenCount = 0;
        for (Animal animal : simulation.getAllAnimals()) {
            sumOfChildrenCount += animal.getStats().getChildrenCount();
        }
        return sumOfChildrenCount / simulation.getAllAnimals().size();
    }

    public int getAvgEnergyLevel() {
        int sumOfEnergy = 0;
        for (Animal animal : simulation.getAllAnimals()) {
            sumOfEnergy += animal.getEnergy();
        }
        return sumOfEnergy / simulation.getAllAnimals().size();
    }

    public List<Integer> getMostPopularGenome() {
        return simulation.getAllAnimals().stream()
                .map(a -> a.getGenome().getSequence())
                .collect(Collectors.groupingBy(sequence -> sequence, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Collections.emptyList());
    }

    public int getFreePositionsCount() {
        int positionsTakenByAnimals = simulation.getMap().getPositionsWithAnimals().size();
        Vector2d upperRightCorner = simulation.getMap().getCurrentBounds().upperRightCorner();
        int allPositions = (upperRightCorner.getX() + 1) * (upperRightCorner.getY() + 1);
        return allPositions - positionsTakenByAnimals;
    }

    public int getPlantsCount() {
        return simulation.getMap().getPlants().size();
    }
}
