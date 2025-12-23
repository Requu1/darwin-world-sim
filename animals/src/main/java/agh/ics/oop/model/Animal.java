package agh.ics.oop.model;

import java.util.ArrayList;

public class Animal implements WorldElement {
    private final static Vector2d DEFAULT_POS = new Vector2d(2, 2);

    private MapDirection facingDirection;
    private Vector2d posVector;
    private int energy;
    private ArrayList<Integer> genome;
    private int currGenomeIdx;

    public Animal(Vector2d posVector, int energy, ArrayList<Integer> genome) {
        this.posVector = posVector;
        this.energy = energy;
        this.genome = genome;
        this.facingDirection = MapDirection.NORTH;
    }


    public MapDirection getFacingDirection() {
        return this.facingDirection;
    }

    @Override
    public Vector2d getPosition() {
        return this.posVector;
    }

    @Override
    public String toString() {
        return switch (this.facingDirection) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case WEST -> "W";
            case EAST -> "E";
            case SOUTH -> "S";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
        };
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.posVector.equals(position);
    }

    void move(MoveValidator validator) {
        for (int i = 0; i < genome.get(currGenomeIdx); i++) {
            this.facingDirection = MapDirection.next(facingDirection);
        }
        this.posVector = posVector.add(this.facingDirection.toUnitVector());
        this.currGenomeIdx = (currGenomeIdx + 1) % genome.size();
    }

}
