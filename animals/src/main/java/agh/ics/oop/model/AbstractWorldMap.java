package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalBuilder;
import agh.ics.oop.model.util.GenomeGenerator;
import agh.ics.oop.model.util.RandomPlantPositionGenerator;

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
        initializePlants(startingPlantsCount);
    }

    private void initializePlants(int startingPlantsCount) {
        this.growPlants(startingPlantsCount);
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
            animal.addEnergy(energyRestoredByPlant);
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
        animalsOnThePos = tryToReproduce(animal, animalsOnThePos);
        animalsOnThePos.add(animal);
        this.animals.put(animal.getPosition(), animalsOnThePos);
        informListeners("Animal on position " + animal.getPosition() + " has been created.");

    }

    private List<Animal> tryToReproduce(Animal animal, List<Animal> animalsOnThePos) {
        List<Animal> animalsOnThePosAfterReproduction = new ArrayList<>(List.copyOf(animalsOnThePos));
        for (Animal animalAlreadyOnThePos : animalsOnThePos) {
            if (animalAlreadyOnThePos.getEnergy() >= minimalEnergyForReproduction) {
                animalAlreadyOnThePos.subtractEnergy(usedEnergyForReproduction);
                animal.subtractEnergy(usedEnergyForReproduction);

                Animal bornAnimal = reproduce(animal, animalAlreadyOnThePos);
                this.bornAnimals.add(bornAnimal);
                animalsOnThePosAfterReproduction.add(bornAnimal);
                break;
            }
        }
        return animalsOnThePosAfterReproduction;
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
                .withGenome(new Genome(GenomeGenerator.generateGenomeFromReproducing(animal1, animal2, minMutationCount, maxMutationCount)))
                .withPosition(animal1.getPosition())
                .createAnimal();
    }


    @Override
    public void growPlants(int plantsCount) {
        RandomPlantPositionGenerator generator = new RandomPlantPositionGenerator(this.upperRightCorner.getX() + 1, this.upperRightCorner.getY() + 1, plantsCount, plants.keySet());
        for (Vector2d plantPosition : generator) {
            this.plants.put(plantPosition, new Plant(plantPosition));
        }
    }

    @Override
    public void place(Animal animal) {
        this.animals.put(animal.getPosition(), List.of(animal));
        informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()));
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

    public List<List<Animal>> getPositionsWithAnimals() {
        List<List<Animal>> animalsOnTheSamePos = new ArrayList<>();
        for (List<Animal> animalList : this.animals.values()) {
            animalsOnTheSamePos.add(animalList);
        }
        return animalsOnTheSamePos;
    }

    public List<Animal> animalsAtPos(Vector2d pos) {
        return this.animals.get(pos);
    }

    public ArrayList<Animal> getAnimals() {
        ArrayList<Animal> animalsArray = new ArrayList<>();
        for (List<Animal> animalList : this.animals.values()) {
            animalsArray.addAll(animalList);
        }
        return animalsArray;
    }

    public ArrayList<Plant> getPlants() {
        ArrayList<Plant> plantsArray = new ArrayList<>();
        plantsArray.addAll(plants.values());
        return plantsArray;
    }

    @Override
    public UUID getId() {
        return this.id;
    }


    @Override
    public void removeAnimal(Animal animal) {
        //informListeners("Animal on position " + animal.getPosition() + " has died");
        if (animals.get(animal.getPosition()).size() == 1) {
            this.animals.remove(animal.getPosition());
        } else {
            List<Animal> animalsAlreadyOnThePos = animals.get(animal.getPosition());
            this.animals.remove(animal.getPosition());

            animalsAlreadyOnThePos.remove(animal);
            this.animals.put(animal.getPosition(), animalsAlreadyOnThePos);

        }
    }

}
