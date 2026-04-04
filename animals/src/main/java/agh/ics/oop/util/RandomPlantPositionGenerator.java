package agh.ics.oop.util;

import agh.ics.oop.other.Vector2d;

import java.util.*;

public class RandomPlantPositionGenerator implements Iterable<Vector2d> {
    private final int plantsCount;
    private final int width, height;
    private final List<Integer> freeJungleIndices = new ArrayList<>();
    private final List<Integer> freeSteppeIndices = new ArrayList<>();
    private final Random random = new Random();

    public RandomPlantPositionGenerator(int width, int height, int plantsCount, Set<Vector2d> usedPlantPositions) {
        this.width = width;
        this.height = height;
        this.plantsCount = plantsCount;

        int lowerBoundY = (int) Math.round(0.4 * height);
        int upperBoundY = (int) Math.round(0.6 * height);

        for (int i = 0; i < width * height; i++) {
            Vector2d pos = new Vector2d(i % width, i / width);

            if (!usedPlantPositions.contains(pos)) {
                int y = pos.y();
                if (y >= lowerBoundY && y <= upperBoundY) {
                    freeJungleIndices.add(i);
                } else {
                    freeSteppeIndices.add(i);
                }
            }
        }

    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
            private int generatedPlants = 0;

            @Override
            public boolean hasNext() {
                return generatedPlants < plantsCount && (!freeJungleIndices.isEmpty() || !freeSteppeIndices.isEmpty());
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) throw new NoSuchElementException();

                int selectedIndex;
                boolean tryJungle = random.nextInt(10) < 8;

                if (tryJungle && !freeJungleIndices.isEmpty()) {
                    selectedIndex = pickAndRemoveRandom(freeJungleIndices);
                } else if (!freeSteppeIndices.isEmpty()) {
                    selectedIndex = pickAndRemoveRandom(freeSteppeIndices);
                } else {
                    selectedIndex = pickAndRemoveRandom(freeJungleIndices);
                }

                generatedPlants++;
                return new Vector2d(selectedIndex % width, selectedIndex / width);
            }

            private int pickAndRemoveRandom(List<Integer> list) {
                int randomIndex = random.nextInt(list.size());
                int lastIndex = list.size() - 1;
                int pickedValue = list.get(randomIndex);
                list.set(randomIndex, list.get(lastIndex));
                list.remove(lastIndex);

                return pickedValue;
            }
        };
    }
}