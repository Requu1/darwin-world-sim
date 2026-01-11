package agh.ics.oop.model;

import agh.ics.oop.World;

import java.util.List;

public class Animal implements WorldElement {
    private final static Vector2d DEFAULT_POS = new Vector2d(2, 2);
    private final static int DEFAULT_ENERGY = 50;
    private final static int DEFAULT_BODY_TEMPERATURE = 36;
    private final static int MAX_BODY_TEMPERATURE = 37;
    private final static int LOW_BODY_TEMPERATURE = 32;
    private final static double COLD_PENALTY = 1.5;
    private final static double ENERGY_LOSS_FACTOR = 0.5;

    private MapDirection facingDirection;
    private Vector2d posVector;
    private int energy;
    private final Genome genome;
    private int bodyTemperature = DEFAULT_BODY_TEMPERATURE;
    private final AnimalStatistics stats;

    public Animal() {
        this(DEFAULT_POS, DEFAULT_ENERGY, new Genome());
    }

    public Animal(Vector2d posVector, int energy, Genome genome) {
        this.posVector = posVector;
        this.energy = energy;
        this.genome = genome;
        this.facingDirection = MapDirection.NORTH;
        this.stats = new AnimalStatistics(this.genome, this.energy);
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
        for (int i = 0; i < genome.getCurrGenomePart(); i++) {
            this.facingDirection = MapDirection.next(facingDirection);
        }
        Vector2d newPosVector = posVector.add(this.facingDirection.toUnitVector());

        if (newPosVector.getX() < 0 || newPosVector.getX() > upperRightCorner.getX()) {
            newPosVector = posVector.add(this.facingDirection.toUnitVector().opposite());
            this.facingDirection = MapDirection.opposite(facingDirection);
        }

        if (newPosVector.getY() < 0 || newPosVector.getY() > upperRightCorner.getY()) {
            newPosVector = new Vector2d(newPosVector.getX(), (newPosVector.getY() + upperRightCorner.getY()) % upperRightCorner.getY());
        }

        this.posVector = newPosVector;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public void addEnergy(int amount) {
        this.energy += amount;
    }

    public void subtractEnergy(int amount) {
        this.energy -= amount;
    }

    public void increaseBodyTemperature(int amount) {
        if (this.bodyTemperature + amount >= MAX_BODY_TEMPERATURE) {
            this.bodyTemperature = MAX_BODY_TEMPERATURE;
        }
        this.bodyTemperature += amount;
    }

    public void decreaseBodyTemperature(int amount) {
        this.bodyTemperature -= amount;
    }

    public int getBodyTemperature() {
        return this.bodyTemperature;
    }


    public int calculateEnergyLoss(double worldTemperature, List<Animal> animals, double warmDist) {
        double currentLoss = 0;
        if (this.bodyTemperature <= LOW_BODY_TEMPERATURE) {
            currentLoss = ENERGY_LOSS_FACTOR * (LOW_BODY_TEMPERATURE - bodyTemperature);
            long neighborsCount = animals.stream()
                    .filter(animal -> animal != this && World.calcDistance(this, animal) <= warmDist)
                    .count();

            if (neighborsCount == 0) {
                double noNeighboursPenalty = Math.abs(worldTemperature) * COLD_PENALTY;
                currentLoss += noNeighboursPenalty;
            }
        }
        return (int) currentLoss;
    }

}
