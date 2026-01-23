package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public static ArrayList<Vector2d> generatePositions(int positionsCount, int mapWidth, int mapHeight) {
        Random randomPos = new Random();
        ArrayList<Vector2d> positions = new ArrayList<>();

        for (int i = 0; i < positionsCount; i++) {
            positions.add(new Vector2d(randomPos.nextInt(mapWidth + 1), randomPos.nextInt(mapHeight + 1)));
        }
        return positions;
    }

    public static double calcDistance(Animal animal1, Animal animal2) {
        double x = animal1.getPosition().x() - animal2.getPosition().x();
        double y = animal1.getPosition().y() - animal2.getPosition().y();
        return Math.sqrt(x * x + y * y);
    }

}
