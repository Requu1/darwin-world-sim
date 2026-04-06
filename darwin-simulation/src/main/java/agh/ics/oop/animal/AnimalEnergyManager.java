package agh.ics.oop.animal;

import agh.ics.oop.other.Vector2d;

import java.util.List;

public class AnimalEnergyManager {
    private final static double COLD_PENALTY = 1.5;
    private final static double ENERGY_LOSS = 2;


    public static int calculateEnergyLoss(Animal animal, double worldTemperature, List<Animal> animals, double warmDist, int mapWidth) {
        double currentLoss = ENERGY_LOSS;
        long neighborsCount = animals.stream()
                .filter(neighbour -> neighbour != animal && wrappedDistance(animal.getPosition(), neighbour.getPosition(), mapWidth) <= warmDist)
                .count();

        if (neighborsCount == 0) {
            double noNeighboursPenalty = Math.abs(worldTemperature) * COLD_PENALTY;
            currentLoss *= noNeighboursPenalty;
        }

        return (int) currentLoss;
    }

    private static double wrappedDistance(Vector2d a, Vector2d b, int mapWidth) {
        double x = Math.abs(a.x() - b.x());
        x = Math.min(x, mapWidth - x);

        double y = Math.abs(a.y() - b.y());

        return Math.sqrt(x * x + y * y);
    }
}
