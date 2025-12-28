package agh.ics.oop;

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


    public static Vector2d generatePlantPos(int mapWidth, int mapHeight) {
        Random random = new Random();

        if (random.nextInt(10) < 8) {
            return generatePosInTheJungle(mapWidth, mapHeight);
        } else {
            return generatePosOutsideTheJungle(mapWidth, mapHeight);
        }
    }

    private static Vector2d generatePosInTheJungle(int mapWidth, int mapHeight) {
        Random random = new Random();
        int upperBoundY = (int) Math.round(0.6 * mapHeight);
        int lowerBoundY = (int) Math.round(0.4 * mapHeight);

        return new Vector2d(random.nextInt(mapWidth), random.nextInt(upperBoundY - lowerBoundY + 1) + lowerBoundY);
    }

    private static Vector2d generatePosOutsideTheJungle(int mapWidth, int mapHeight) {
        Random random = new Random();
        int upperBoundY = (int) Math.round(0.6 * mapHeight);
        int lowerBoundY = (int) Math.round(0.4 * mapHeight);

        return new Vector2d(random.nextInt(mapWidth), (random.nextInt(lowerBoundY + upperBoundY) + upperBoundY) % mapHeight);
    }

}
