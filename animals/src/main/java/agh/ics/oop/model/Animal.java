package agh.ics.oop.model;

import agh.ics.oop.model.util.GenomeGenerator;

import java.util.ArrayList;

public class Animal implements WorldElement {
    private final static int DEFAULT_GENOME_LENGTH = 10;
    private final static Vector2d DEFAULT_POS = new Vector2d(2, 2);
    private final static int DEFAULT_ENERGY = 100;
    private final static ArrayList<Integer> DEFAULT_GENOME = GenomeGenerator.generateNewGenome(DEFAULT_GENOME_LENGTH);

    private MapDirection facingDirection;
    private Vector2d posVector;
    private int energy;
    private final ArrayList<Integer> genome;
    private int currGenomeIdx;

    public Animal() {
        this(DEFAULT_POS, DEFAULT_ENERGY, DEFAULT_GENOME);
    }

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

    void move(Vector2d upperRightCorner) {
        for (int i = 0; i < genome.get(currGenomeIdx); i++) {
            this.facingDirection = MapDirection.next(facingDirection);
        }
        Vector2d newPosVector = posVector.add(this.facingDirection.toUnitVector());

        if (newPosVector.getX() < 0 || newPosVector.getX() > upperRightCorner.getX()) {
            newPosVector = posVector.add(this.facingDirection.toUnitVector().opposite());
        }

        if (newPosVector.getY() < 0 || newPosVector.getY() > upperRightCorner.getY()) {
            newPosVector = new Vector2d(newPosVector.getX(), newPosVector.getY() % upperRightCorner.getY());
        }

        this.posVector = newPosVector;
        this.currGenomeIdx = (currGenomeIdx + 1) % genome.size();
    }

    public int getEnergy() {
        return this.energy;
    }

    public ArrayList<Integer> getGenome() {
        return this.genome;
    }

    public void updateEnergy(int amount) {
        this.energy += amount;
    }

}
