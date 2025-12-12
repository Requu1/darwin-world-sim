package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class GrassField extends AbstractWorldMap implements WorldMap {
    private static final Vector2d LOWER_LEFT_CORNER = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final Vector2d UPPER_RIGHT_CORNER = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final Map<Vector2d, Grass> grasses = new HashMap<>();


    public GrassField(int grassCount) {
        super(LOWER_LEFT_CORNER, UPPER_RIGHT_CORNER);
        this.initializeGrass(grassCount);

    }

    private void initializeGrass(int grassCount) {
        try {
            int grassBound = ((int) Math.sqrt(grassCount * 10)) + 1;
            RandomPositionGenerator generator = new RandomPositionGenerator(grassBound, grassBound, grassCount);
            for (Vector2d grassPosition : generator) {
                this.grasses.put(grassPosition, new Grass(grassPosition));
            }
        } catch (Exception e) {
            System.out.println("Error while initializing grass:" + e);
        }
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement animal = super.objectAt(position);
        if (animal == null) {
            return this.grasses.get(position);
        }
        return animal;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d newUpperRightCorner = LOWER_LEFT_CORNER;
        Vector2d newLowerLeftCorner = UPPER_RIGHT_CORNER;

        ArrayList<WorldElement> elements = this.getElements();
        for (WorldElement element : elements) {
            newLowerLeftCorner = newLowerLeftCorner.lowerLeft(element.getPosition());
            newUpperRightCorner = newUpperRightCorner.upperRight(element.getPosition());
        }

        return new Boundary(newUpperRightCorner, newLowerLeftCorner);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal) && UPPER_RIGHT_CORNER.follows(position) && LOWER_LEFT_CORNER.precedes(position);
    }

    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = super.getElements();
        elements.addAll(new ArrayList<>(grasses.values()));
        return elements;
    }


}
