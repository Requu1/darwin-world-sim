package agh.ics.oop.map;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.other.Plant;
import agh.ics.oop.other.Vector2d;
import agh.ics.oop.util.RandomPlantPositionGenerator;

import java.util.*;

public class RectangularMap {
    private final static Vector2d BOTTOM_LEFT_CORNER = new Vector2d(0, 0);
    private final static String ADD_MESSAGE = "Animal has been added to:";
    private final static String MOVE_MESSAGE = "Animal has been moved to:";

    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final UUID id;
    private final Vector2d upperRightCorner;
    private final Vector2d lowerLeftCorner = BOTTOM_LEFT_CORNER;

    private final ArrayList<MapChangeListener> mapListeners = new ArrayList<>();

    public void addListener(MapChangeListener listener) {
        this.mapListeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        this.mapListeners.remove(listener);
    }

    private void informListeners(String message) {
        for (MapChangeListener listener : this.mapListeners) {
            listener.mapChanged(this, message);
        }
    }

    public RectangularMap(int width, int height) {
        this.id = UUID.randomUUID();
        this.upperRightCorner = new Vector2d(width - 1, height - 1);
    }


    private void removeAnimalFromPreviousPos(Animal animal) {
        Vector2d position = animal.getPosition();
        synchronized (animals) {
            List<Animal> animalsOnThePos = animals.get(position);
            if (animalsOnThePos == null) {
                return;
            }
            animalsOnThePos.remove(animal);
            if (animalsOnThePos.isEmpty()) {
                this.animals.remove(position);
            }

        }
    }

    public void move(Animal animal, int energyRestoredByPlant) {
        Vector2d preMovePosition = animal.getPosition();
        MapDirection preMoveDirection = animal.getFacingDirection();
        synchronized (animals) {
            removeAnimalFromPreviousPos(animal);
            animal.move(upperRightCorner);
            if (animals.get(animal.getPosition()) == null) {
                this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            } else {
                this.animals.get(animal.getPosition()).add(animal);

            }
        }
        synchronized (plants) {
            if (plants.get(animal.getPosition()) != null) {
                this.plants.remove(animal.getPosition());
                animal.eatPlant(energyRestoredByPlant);
            }
        }

        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().x(), animal.getPosition().y(), animal.getFacingDirection()
                , preMovePosition.x(), preMovePosition.y(), preMoveDirection));

    }


    public void growPlants(int plantsCount) {
        RandomPlantPositionGenerator generator = new RandomPlantPositionGenerator(this.upperRightCorner.x() + 1, this.upperRightCorner.y() + 1, plantsCount, plants.keySet());
        synchronized (plants) {
            for (Vector2d plantPosition : generator) {
                this.plants.put(plantPosition, new Plant(plantPosition));
            }
        }
        informListeners("New plants have been created");
    }

    public void place(Animal animal) {
        synchronized (animals) {
            if (animals.get(animal.getPosition()) == null) {
                this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            } else {
                animals.get(animal.getPosition()).add(animal);
            }
        }
        informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().x(), animal.getPosition().y(), animal.getFacingDirection()));
    }


    public List<List<Animal>> getPositionsWithAnimals() {
        synchronized (animals) {
            List<List<Animal>> animalsOnTheSamePos = new ArrayList<>();
            for (List<Animal> animalList : this.animals.values()) {
                animalsOnTheSamePos.add(new ArrayList<>(animalList));
            }
            return animalsOnTheSamePos;
        }
    }

    public List<Animal> animalsAtPos(Vector2d pos) {
        if (this.animals.get(pos) == null) {
            return null;
        }
        return new ArrayList<>(this.animals.get(pos));
    }

    public List<Plant> getPlants() {
        synchronized (plants) {
            return new ArrayList<>(plants.values());
        }
    }

    public UUID getId() {
        return this.id;
    }

    public void removeAnimalFromTheMap(Animal animal) {
        synchronized (animals) {
            if (animals.get(animal.getPosition()) == null) {
                return;
            }
            animals.get(animal.getPosition()).remove(animal);
            if (animals.get(animal.getPosition()).isEmpty()) {
                this.animals.remove(animal.getPosition());
            }
        }
    }

    public Boundary getCurrentBounds() {
        return new Boundary(upperRightCorner, lowerLeftCorner);
    }

}
