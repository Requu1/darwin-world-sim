package agh.ics.oop.model;

import agh.ics.oop.model.util.*;

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

    private void removeAnimalFromPos(Animal animal) {
        Vector2d position = animal.getPosition();
        if (this.animals.get(position).size() == 1) {
            this.animals.remove(position);
        } else {
            List<Animal> animalsOnThePos = animals.get(position);
            animalsOnThePos.remove(animal);
        }
    }

    @Override
    public void move(Animal animal) {
        Vector2d preMovePosition = animal.getPosition();
        MapDirection preMoveDirection = animal.getFacingDirection();
        removeAnimalFromPos(animal);
        animal.move(upperRightCorner);

        if (plants.get(animal.getPosition()) != null) {
            animal.addEnergy(energyRestoredByPlant);
            animal.informListeners(AnimalStatisticsData.ADD_PLANT_EATEN);
            this.plants.remove(animal.getPosition());
        }

        if (animals.get(animal.getPosition()) == null) {
            this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
        } else {
            List<Animal> animalsOnThePos = this.animals.get(animal.getPosition());
            animalsOnThePos.add(animal);
        }
        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()
                , preMovePosition.getX(), preMovePosition.getY(), preMoveDirection));

    }

    public void reproduceAnimals() {
        for (Vector2d pos : this.animals.keySet()) {
            if (animals.get(pos).size() < 2) {
                continue;
            }
            List<Animal> potentialParents = animals.get(pos).stream()
                    .filter(a -> a.getEnergy() >= minimalEnergyForReproduction)
                    .sorted(Comparator.comparingInt(Animal::getEnergy).reversed())
                    .limit(2)
                    .toList();
            if (potentialParents.size() == 2) {
                Animal animal1 = potentialParents.get(0);
                Animal animal2 = potentialParents.get(1);
                reproduce(animal1, animal2);
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


    private void reproduce(Animal animal1, Animal animal2) {
        Animal newBornAnimal = new AnimalBuilder()
                .withEnergy(2 * usedEnergyForReproduction)
                .withGenome(new Genome(GenomeGenerator.generateGenomeFromReproducing(animal1, animal2, minMutationCount, maxMutationCount)))
                .withPosition(animal1.getPosition())
                .createAnimal();
        List<Animal> animalsOnThePos = this.animals.get(newBornAnimal.getPosition());
        animalsOnThePos.add(newBornAnimal);
        this.bornAnimals.add(newBornAnimal);
        informListeners("New animal has been reproduced");
        animal1.informListeners(AnimalStatisticsData.ADD_CHILDREN_COUNT);
        animal2.informListeners(AnimalStatisticsData.ADD_CHILDREN_COUNT);
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
        this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
        informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()));
    }


    public List<List<Animal>> getPositionsWithAnimals() {
        List<List<Animal>> animalsOnTheSamePos = new ArrayList<>();
        for (List<Animal> animalList : this.animals.values()) {
            animalsOnTheSamePos.add(new ArrayList<>(animalList));
        }
        return animalsOnTheSamePos;
    }

    public List<Animal> animalsAtPos(Vector2d pos) {
        return this.animals.get(pos);
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
        //informMapListeners("Animal on position " + animal.getPosition() + " has died");
        if (animals.get(animal.getPosition()).size() == 1) {
            this.animals.remove(animal.getPosition());
        } else {
            List<Animal> animalsAlreadyOnThePos = animals.get(animal.getPosition());
            animalsAlreadyOnThePos.remove(animal);

        }
    }

}
