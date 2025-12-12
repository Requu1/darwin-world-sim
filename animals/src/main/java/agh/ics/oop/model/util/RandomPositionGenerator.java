package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int grassCount;
    private final int width, height;
    private final Random random = new Random();

    public RandomPositionGenerator(int width, int height, int grassCount) {
        this.grassCount = grassCount;
        this.width = width;
        this.height = height;
        int positionsForGrass = width * height;
        if (grassCount > positionsForGrass) {
            throw new IllegalArgumentException("Brak miejsca na trawe");
        }
    }

    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
            private final Map<Vector2d, Boolean> usedPositions = new HashMap<>();
            private int generatedGrass = 0;

            @Override
            public boolean hasNext() {
                return generatedGrass < grassCount;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new IllegalStateException("Brak możliwych kolejnych pozycji dla trawy");
                }

                Vector2d newPos;
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                newPos = new Vector2d(x, y);

                while (usedPositions.containsKey(newPos)) {
                    x = random.nextInt(width);
                    y = random.nextInt(height);
                    newPos = new Vector2d(x, y);
                }
                usedPositions.put(newPos, true);
                generatedGrass++;
                return newPos;
            }
        };
    }


}
