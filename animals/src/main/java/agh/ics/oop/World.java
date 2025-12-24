package agh.ics.oop;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public static ArrayList<Vector2d> generatePositions(int positionsCount) {
        Random randomPos = new Random();
        ArrayList<Vector2d> positions = new ArrayList<>();

        for (int i = 0; i < positionsCount; i++) {
            positions.add(new Vector2d(randomPos.nextInt(10), randomPos.nextInt(10)));
        }
        return positions;
    }
}
