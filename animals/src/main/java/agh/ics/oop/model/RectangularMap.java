package agh.ics.oop.model;

import agh.ics.oop.model.util.*;

import java.util.*;

public class RectangularMap {
    private final static Vector2d BOTTOM_LEFT_CORNER = new Vector2d(0, 0);
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final UUID id;
    protected final ArrayList<Animal> bornAnimals = new ArrayList<>();

    protected final Vector2d upperRightCorner;
    protected final Vector2d lowerLeftCorner = BOTTOM_LEFT_CORNER;

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

    public RectangularMap(int width, int height, int energyRestoredByPlant, int startingPlantsCount,
                          int minimalEnergyForReproduction, int usedEnergyForReproduction, int minMutationCount, int maxMutationCount) {
        this.id = UUID.randomUUID();
        this.upperRightCorner = new Vector2d(width - 1, height - 1);
        this.energyRestoredByPlant = energyRestoredByPlant;
        this.minimalEnergyForReproduction = minimalEnergyForReproduction;
        this.usedEnergyForReproduction = usedEnergyForReproduction;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        initializePlants(startingPlantsCount);
    }

    private void initializePlants(int startingPlantsCount) {
        this.growPlants(startingPlantsCount);
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

    public void move(Animal animal) {
        Vector2d preMovePosition = animal.getPosition();
        MapDirection preMoveDirection = animal.getFacingDirection();
        synchronized (animals) {
            removeAnimalFromPreviousPos(animal);
            animal.move(upperRightCorner);
            if (animals.get(animal.getPosition()) == null) {
                this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            } else {
                List<Animal> animalsOnThePos = this.animals.get(animal.getPosition());
                animalsOnThePos.add(animal);
            }
        }
        synchronized (plants) {
            if (plants.get(animal.getPosition()) != null) {
                this.plants.remove(animal.getPosition());
                animal.addEnergy(energyRestoredByPlant);
                animal.informListeners(AnimalStatisticsData.ADD_PLANT_EATEN);
            }
        }

        informListeners(String.format("%s ((%d,%d), %s) from: ((%d,%d), %s):",
                MOVE_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()
                , preMovePosition.getX(), preMovePosition.getY(), preMoveDirection));

    }

    public void reproduceAnimals() {
        synchronized (animals) {
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
                    animal1.subtractEnergy(usedEnergyForReproduction);
                    animal2.subtractEnergy(usedEnergyForReproduction);
                }
            }
        }
    }


    public void clearBornAnimals() {
        this.bornAnimals.clear();
    }

    public ArrayList<Animal> getBornAnimals() {
        return new ArrayList<>(this.bornAnimals);
    }


    private void reproduce(Animal animal1, Animal animal2) {
        Animal newBornAnimal = new AnimalBuilder()
                .withEnergy(2 * usedEnergyForReproduction)
                .withGenome(new Genome(GenomeGenerator.generateGenomeFromReproducing(animal1, animal2, minMutationCount, maxMutationCount)))
                .withPosition(animal1.getPosition())
                .createAnimal();
        place(newBornAnimal);
        this.bornAnimals.add(newBornAnimal);
        animal1.informListeners(AnimalStatisticsData.ADD_CHILDREN_COUNT);
        animal2.informListeners(AnimalStatisticsData.ADD_CHILDREN_COUNT);
        informListeners("New animal has been reproduced");
    }


    public void growPlants(int plantsCount) {
        RandomPlantPositionGenerator generator = new RandomPlantPositionGenerator(this.upperRightCorner.getX() + 1, this.upperRightCorner.getY() + 1, plantsCount, plants.keySet());
        synchronized (plants) {
            for (Vector2d plantPosition : generator) {
                this.plants.put(plantPosition, new Plant(plantPosition));
            }
        }
    }

    public void place(Animal animal) {
        synchronized (animals) {
            if (animals.get(animal.getPosition()) == null) {
                this.animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            } else {
                List<Animal> animalsOnThePoss = animals.get(animal.getPosition());
                animalsOnThePoss.add(animal);
            }
        }
        informListeners(String.format("%s ((%d,%d),%s)", ADD_MESSAGE, animal.getPosition().getX(), animal.getPosition().getY(), animal.getFacingDirection()));
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
            List<Animal> animalsOnThePos = animals.get(animal.getPosition());
            animalsOnThePos.remove(animal);
            if (animalsOnThePos.isEmpty()) {
                this.animals.remove(animal.getPosition());
            }
        }
    }


    public Boundary getCurrentBounds() {
        return new Boundary(upperRightCorner, lowerLeftCorner);
    }


}
