package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap implements WorldMap {
    private final static Vector2d BOTTOM_LEFT_CORNER = new Vector2d(0, 0);


    public RectangularMap(int width, int height, int startingPlantsCount, int energyRestoredByPlant,
                          int minimalEnergyForReproduction, int usedEnergyForReproduction, int minMutationCount, int maxMutationCount) {
        super(BOTTOM_LEFT_CORNER, new Vector2d(width, height), startingPlantsCount, energyRestoredByPlant, minimalEnergyForReproduction,
                usedEnergyForReproduction, minMutationCount, maxMutationCount);

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && super.lowerLeftCorner.precedes(position) && super.upperRightCorner.follows(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(super.upperRightCorner, BOTTOM_LEFT_CORNER);
    }

}
