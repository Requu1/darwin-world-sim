package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap implements WorldMap {
    private final static Vector2d BOTTOM_LEFT_CORNER = new Vector2d(0, 0);

    private final int energyRestoredByPlant;
    private final int plantsGrowingDaily;
    private final int dailyEnergyLoss;
    private final int minimalEnergyForReproduction;
    private final int usedEnergyForReproduction;
    private final int minMutationCount;
    private final int maxMutationCount;


    public RectangularMap(int width, int height, int energyRestoredByPlant, int plantsGrowingDaily, int dailyEnergyLoss,
                          int minimalEnergyForReproduction, int usedEnergyForReproduction, int minMutationCount, int maxMutationCount) {
        super(BOTTOM_LEFT_CORNER, new Vector2d(width, height));
        this.energyRestoredByPlant = energyRestoredByPlant;
        this.plantsGrowingDaily = plantsGrowingDaily;
        this.dailyEnergyLoss = dailyEnergyLoss;
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        if (minMutationCount > maxMutationCount) {
            throw new IllegalArgumentException("Minimal mutation count cannot be greater than maximal.");
        }
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
