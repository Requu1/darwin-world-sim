package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final UUID id;

    protected final Vector2d upperRightCorner;
    protected final Vector2d lowerLeftCorner;

    protected final int energyRestoredByPlant;
    protected final int minimalEnergyForReproduction;
    protected final int usedEnergyForReproduction;
    protected final int minMutationCount;
    protected final int maxMutationCount;

    private final static String ADD_MESSAGE = "Animal has been added to:";
    private final static String MOVE_MESSAGE = "Animal has been moved to:";

    private final ArrayList<MapChangeListener> listeners = new ArrayList<>();

    public void addListener(MapChangeListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        this.listeners.remove(listener);
    }

    private void informListeners(String message) {
        for (MapChangeListener listener : this.listeners) {
            listener.mapChanged(this, message);
        }
    }

    public AbstractWorldMap(Vector2d lowerLeftCorner, Vector2d upperRightCorner, int startingPlantsCount, int energyRestoredByPlant,
                            int minimalEnergyForReproduction, int usedEnergyForReproduction, int minMutationCount, int maxMutationCount) {
        this.id = UUID.randomUUID();
        this.upperRightCorner = upperRightCorner;
        this.lowerLeftCorner = lowerLeftCorner;
        this.energyRestoredByPlant = energyRestoredByPlant;
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        if (minMutationCount > maxMutationCount) {
            throw new IllegalArgumentException("Minimal mutation count cannot be greater than maximal.");
        }
        try {
            initializePlants(startingPlantsCount);
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
    }

    private void initializePlants(int startingPlantsCount) throws IncorrectPositionException {
        for (int i = 0; i < startingPlantsCount; i++) {
            this.growPlant();
        }
    }

    private void removeAnimalFromPos(Vector2d position) {
        this.animals.remove(position);
    }

    @Override
    public void move(Animal animal) {
        Vector2d preMovePosition = animal.getPosition();
        MapDirection preMoveDirection = animal.getFacingDirection();
        animal.move(this);

        if (plants.get(animal.getPosition()) != null) {
            updateEnergy(animal, energyRestoredByPlant);
            this.plants.remove(animal.getPosition());
        }

        removeAnimalFromPos(preMovePosition);
        this.animals.put(animal.getPosition(), animal);
        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()
                , preMovePosition.getX(), preMovePosition.getY(), preMoveDirection));

    }


    @Override
    public void updateEnergy(Animal animal, int amount) {
        animal.updateEnergy(amount);
    }

    @Override
    public void growPlant() {
        Vector2d plantPos = this.generatePlantPos();
        this.plants.put(plantPos, new Plant(plantPos));
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animals.get(position);
    }

    @Override
    public String toString() {
        Boundary currBoundary = this.getCurrentBounds();
        return mapVisualizer.draw(currBoundary.lowerLeftCorner(), currBoundary.upperRightCorner());
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            this.animals.put(animal.getPosition(), animal);
            informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()));
        } else {
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>(this.animals.values());
        elements.addAll(new ArrayList<>(this.plants.values()));
        return elements;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public Vector2d generatePlantPos() {
        Vector2d newPlantPos;
        do {
            newPlantPos = World.generateRandomSinglePos(this.upperRightCorner);
        }
        while (this.plants.get(newPlantPos) != null);

        return newPlantPos;
    }

    @Override
    public void removeAnimal(Animal animal) {
        this.animals.remove(animal.getPosition());
    }


    public Map<Vector2d, Animal> getAnimalsWithPos() {
        return this.animals;
    }
}
