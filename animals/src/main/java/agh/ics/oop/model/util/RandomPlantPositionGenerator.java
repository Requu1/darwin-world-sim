package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPlantPositionGenerator implements Iterable<Vector2d> {
    private final int plantsCount;
    private final int width, height;
    private final int jungleSize;

    public RandomPlantPositionGenerator(int width, int height, int plantsCount) {
        this.width = width;
        this.height = height;
        this.plantsCount = plantsCount;
        if (plantsCount > width * height) {
            throw new IllegalArgumentException("Brak miejsca na rośliny");
        }

        int lowerBoundY = (int) Math.round(0.4 * height);
        int upperBoundY = (int) Math.round(0.6 * height);
        this.jungleSize = width * (upperBoundY - lowerBoundY + 1);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
            private final int[] indices;
            private int currIdx = 0;
            private int usedJungle = 0;
            private int usedSteppe = 0;
            private final int totalPositions = width * height;
            private final int steppeSize = totalPositions - jungleSize;
            private final Random random = new Random();

            {
                indices = new int[totalPositions];
                int jPtr = 0;
                int sPtr = jungleSize;

                int lowerBoundY = (int) Math.round(0.4 * height);
                int upperBoundY = (int) Math.round(0.6 * height);

                for (int i = 0; i < totalPositions; i++) {
                    int y = i / width;
                    if (y >= lowerBoundY && y <= upperBoundY) {
                        indices[jPtr++] = i;
                    } else {
                        indices[sPtr++] = i;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return currIdx < plantsCount;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new IllegalStateException("Brak możliwych następnych pozycji");
                }

                int selectedIndex;
                boolean tryJungle = random.nextInt(10) < 8;

                if (tryJungle && usedJungle < jungleSize) {
                    selectedIndex = shuffleAndPick(0, jungleSize, usedJungle++);
                } else if (usedSteppe < steppeSize) {
                    selectedIndex = shuffleAndPick(jungleSize, totalPositions, usedSteppe++);
                } else {
                    selectedIndex = shuffleAndPick(0, jungleSize, usedJungle++);
                }

                currIdx++;
                return new Vector2d(selectedIndex % width, selectedIndex / width);
            }

            private int shuffleAndPick(int start, int end, int usedCount) {
                int rangeSize = end - start;
                int swapWith = start + usedCount + random.nextInt(rangeSize - usedCount);
                int currentPos = start + usedCount;

                int temp = indices[currentPos];
                indices[currentPos] = indices[swapWith];
                indices[swapWith] = temp;

                return indices[currentPos];
            }
        };
    }
}