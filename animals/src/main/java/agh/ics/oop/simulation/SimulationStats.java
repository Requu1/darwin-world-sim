package agh.ics.oop.simulation;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.other.Vector2d;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationStats {
    private final int simulationDay;
    private final int animalsCount;
    private final int plantsCount;
    private final int freePositionsCount;
    private final int avgEnergyLevel;
    private final int avgChildrenCount;
    private final int avgAnimalsLifeSpan;
    private final List<Integer> mostPopularGenome;

    public SimulationStats(Simulation simulation) {
        simulationDay = simulation.getSeason().getCurrentDay();
        animalsCount = simulation.getAliveAnimals().size();
        plantsCount = simulation.getMap().getPlants().size();
        freePositionsCount = calcFreePositionsCount(simulation);
        avgChildrenCount = calcAvgChildrenCount(simulation);
        avgEnergyLevel = calcAvgEnergyLevel(simulation);
        avgAnimalsLifeSpan = calcAnimalAvgLifeSpan(simulation);
        mostPopularGenome = calcMostPopularGenome(simulation);
    }

    private int calcAvgChildrenCount(Simulation simulation) {
        int sumOfChildren = 0;
        for (Animal animal : simulation.getAliveAnimals()) {
            sumOfChildren += animal.getStats().getChildrenCount();
        }
        if (animalsCount == 0) {
            return -1;
        }
        return sumOfChildren / animalsCount;
    }

    private int calcFreePositionsCount(Simulation simulation) {
        int positionsTakenByAnimals = simulation.getMap().getPositionsWithAnimals().size();
        Vector2d upperRightCorner = simulation.getMap().getCurrentBounds().upperRightCorner();
        int allPositions = (upperRightCorner.x() + 1) * (upperRightCorner.y() + 1);
        return allPositions - positionsTakenByAnimals;
    }

    private int calcAvgEnergyLevel(Simulation simulation) {
        int sumEnergy = 0;
        for (Animal animal : simulation.getAliveAnimals()) {
            sumEnergy += animal.getEnergy();
        }
        if (animalsCount == 0) {
            return -1;
        }
        return sumEnergy / animalsCount;
    }

    private int calcAnimalAvgLifeSpan(Simulation simulation) {
        int sumOfLifeSpan = 0;
        int deadCount = 0;
        for (Animal animal : simulation.getAllAnimals()) {
            if (!animal.isAlive()) {
                deadCount++;
                sumOfLifeSpan += animal.getStats().getDaysLived();
            }
        }
        if (deadCount == 0) {
            return -1;
        } else {
            return sumOfLifeSpan / deadCount;
        }
    }

    private List<Integer> calcMostPopularGenome(Simulation simulation) {
        List<Integer> genome =
                simulation.getAllAnimals().stream()
                        .map(a -> a.getGenome().getSequence())
                        .collect(Collectors.groupingBy(sequence -> sequence, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(Collections.emptyList());

        return List.copyOf(genome);
    }

    public int getSimulationDay() {
        return simulationDay;
    }

    public int getAnimalsCount() {
        return animalsCount;
    }

    public int getPlantsCount() {
        return plantsCount;
    }

    public int getFreePositionsCount() {
        return freePositionsCount;
    }

    public int getAvgEnergyLevel() {
        return avgEnergyLevel;
    }

    public int getAvgChildrenCount() {
        return avgChildrenCount;
    }

    public int getAvgAnimalsLifeSpan() {
        return avgAnimalsLifeSpan;
    }

    public List<Integer> getMostPopularGenome() {
        return mostPopularGenome;
    }
}
