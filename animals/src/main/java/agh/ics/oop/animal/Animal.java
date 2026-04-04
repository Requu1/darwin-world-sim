package agh.ics.oop.animal;

import agh.ics.oop.map.MapDirection;
import agh.ics.oop.other.Vector2d;
import agh.ics.oop.other.WorldElement;

import java.util.ArrayList;

public class Animal implements WorldElement {
    private final static Vector2d DEFAULT_POS = new Vector2d(2, 2);
    private final static int MAX_ENERGY = 100;
    private final static int DEFAULT_ENERGY = 50;

    private MapDirection facingDirection;
    private Vector2d posVector;
    private int energy;
    private final Genome genome;
    private final AnimalStatistics stats;
    private boolean alive = true;

    private final ArrayList<AnimalChangeListener> animalListeners = new ArrayList<>();

    public void addListener(AnimalChangeListener listener) {
        this.animalListeners.add(listener);
    }

    private void informListeners(AnimalStatisticsData data) {
        for (AnimalChangeListener listener : this.animalListeners) {
            listener.animalChanged(data, this);
        }
    }

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

    public AnimalStatistics getStats() {
        return this.stats;
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

    public String getResourceName() {
        return "animal.png";
    }


    public void move(Vector2d upperRightCorner) {
        for (int i = 0; i < genome.getCurrGene(); i++) {
            this.facingDirection = MapDirection.next(facingDirection);
        }
        Vector2d newPosVector = posVector.add(this.facingDirection.toUnitVector());

        if (newPosVector.y() < 0 || newPosVector.y() > upperRightCorner.y()) {
            newPosVector = posVector.add(MapDirection.opposite(this.facingDirection).toUnitVector());
            this.facingDirection = MapDirection.opposite(facingDirection);
        }

        int width = upperRightCorner.x() + 1;
        if (newPosVector.x() < 0 || newPosVector.x() > upperRightCorner.x()) {
            newPosVector = new Vector2d((((newPosVector.x()) % width) + width) % width, newPosVector.y());
        }

        this.posVector = newPosVector;
        genome.updateCurrGene();
        this.informListeners(AnimalStatisticsData.UPDATE_ACTIVE_GENE);
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public void addEnergy(int amount) {
        if (this.energy + amount > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else {
            this.energy += amount;
        }
        informListeners(AnimalStatisticsData.UPDATE_ANIMAL_ENERGY);
    }

    public void subtractEnergy(int amount) {
        if (this.energy - amount <= 0) {
            this.energy = 0;
            this.alive = false;
            informListeners(AnimalStatisticsData.SET_DAY_OF_DEATH);
        } else {
            this.energy -= amount;
        }
        informListeners(AnimalStatisticsData.UPDATE_ANIMAL_ENERGY);
    }


    public double getEnergyRatio() {
        return (double) this.energy / MAX_ENERGY;
    }

    public boolean isAlive() {
        return alive;
    }

    public void nextDayLived() {
        informListeners(AnimalStatisticsData.ADD_DAYS_LIVED);
    }

    public void eatPlant(int energyRestoredByPlant) {
        this.addEnergy(energyRestoredByPlant);
        informListeners(AnimalStatisticsData.ADD_PLANT_EATEN);
    }

    public void addChildren() {
        informListeners(AnimalStatisticsData.ADD_CHILDREN_COUNT);
    }


}
