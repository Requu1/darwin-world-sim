package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.util.AnimalBuilder;
import agh.ics.oop.model.util.GenomeGenerator;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final UUID id;
    protected final ArrayList<Animal> bornAnimals = new ArrayList<>();

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
        animal.move(upperRightCorner);

        if (plants.get(animal.getPosition()) != null) {
            updateEnergy(animal, energyRestoredByPlant);
            this.plants.remove(animal.getPosition());
        }

        removeAnimalFromPos(preMovePosition);
        if (animals.get(animal.getPosition()) == null) {
            this.animals.put(animal.getPosition(), List.of(animal));
        } else {
            animalExistsOnTheNewPos(animal);
        }
        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()
                , preMovePosition.getX(), preMovePosition.getY(), preMoveDirection));

    }

    private void animalExistsOnTheNewPos(Animal animal) {
        List<Animal> animalsOnThePos = this.animals.get(animal.getPosition());
        tryToReproduce(animal, animalsOnThePos);
        animalsOnThePos.add(animal);
        this.animals.put(animal.getPosition(), animalsOnThePos);
    }

    private void tryToReproduce(Animal animal, List<Animal> animalsOnThePos) {
        for (Animal animalAlreadyOnThePos : animalsOnThePos) {
            if (animalAlreadyOnThePos.getEnergy() >= minimalEnergyForReproduction) {
                animalAlreadyOnThePos.updateEnergy(usedEnergyForReproduction);
                animal.updateEnergy(usedEnergyForReproduction);
                Animal bornAnimal = reproduce(animal, animalAlreadyOnThePos);
                this.bornAnimals.add(bornAnimal);
                animalsOnThePos.add(bornAnimal);
            }
        }
    }

    @Override
    public void clearBornAnimals() {
        this.bornAnimals.clear();
    }

    @Override
    public ArrayList<Animal> getBornAnimals() {
        return this.bornAnimals;
    }


    private Animal reproduce(Animal animal1, Animal animal2) {
        return new AnimalBuilder()
                .withEnergy(2 * usedEnergyForReproduction)
                .withGenome(GenomeGenerator.generateGenomeFromReproducing(animal1, animal2, minMutationCount, maxMutationCount))
                .withPosition(animal1.getPosition())
                .createAnimal();
    }


    @Override
    public void updateEnergy(Animal animal, int amount) {
        animal.updateEnergy(amount);
    }

    @Override
    public void growPlant() {
        Vector2d plantPos = World.generatePlantPos(upperRightCorner.getX(), upperRightCorner.getY());
        this.plants.put(plantPos, new Plant(plantPos));
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            this.animals.put(animal.getPosition(), List.of(animal));
            informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()));
        } else {
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>();
        for (List<Animal> animalList : animals.values()) {
            elements.addAll(animalList);
        }
        elements.addAll(plants.values());

        return elements;
    }

    @Override
    public UUID getId() {
        return this.id;
    }


    @Override
    public void removeAnimal(Animal animal) {
        this.animals.remove(animal.getPosition());
    }

}
