package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final UUID id;

    protected final Vector2d upperRightCorner;
    protected final Vector2d lowerLeftCorner;

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

    public AbstractWorldMap(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        this.id = UUID.randomUUID();
        this.upperRightCorner = upperRightCorner;
        this.lowerLeftCorner = lowerLeftCorner;
    }

    private void removeAnimalFromPos(Vector2d position) {
        this.animals.remove(position);
    }

    @Override
    public void move(Animal animal) {
        Vector2d preMovePosition = animal.getPosition();
        MapDirection preMoveDirection = animal.getFacingDirection();
        animal.move(this);

        removeAnimalFromPos(preMovePosition);
        this.animals.put(animal.getPosition(), animal);
        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()
                , preMovePosition.getX(), preMovePosition.getY(), preMoveDirection));

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
        return new ArrayList<>(this.animals.values());
    }

    @Override
    public UUID getId() {
        return this.id;
    }

}
