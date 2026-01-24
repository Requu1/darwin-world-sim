package agh.ics.oop.animal;

import agh.ics.oop.map.MapDirection;
import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void moveWrapsHorizontallyAndAdvancesGenome() {
        //given
        Genome genome = new Genome(new ArrayList<>(List.of(2)));
        Animal animal = new Animal(new Vector2d(4, 2), 50, genome);
        Vector2d upperRight = new Vector2d(4, 4);

        //when
        animal.move(upperRight);

        //then
        assertEquals(new Vector2d(0, 2), animal.getPosition());
        assertEquals(MapDirection.EAST, animal.getFacingDirection());
        assertEquals(2, animal.getGenome().getCurrGene());
    }

    @Test
    void moveBouncesVerticallyAndFlipsDirection() {
        //given
        Genome genome = new Genome(new ArrayList<>(List.of(0)));
        Animal animal = new Animal(new Vector2d(2, 4), 50, genome);
        Vector2d upperRight = new Vector2d(4, 4);

        //when
        animal.move(upperRight);

        //then
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(MapDirection.SOUTH, animal.getFacingDirection());
    }

    @Test
    void addEnergyIsCappedAtMaxEnergy() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 95, new Genome(new ArrayList<>(List.of(0))));

        //when
        animal.addEnergy(20);

        //then
        assertEquals(100, animal.getEnergy());
        assertTrue(animal.isAlive());
    }

    @Test
    void subtractEnergyCanKillAnimalAndSetsEnergyToZero() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 5, new Genome(new ArrayList<>(List.of(0))));

        //when
        animal.subtractEnergy(5);

        //then
        assertEquals(0, animal.getEnergy());
        assertFalse(animal.isAlive());
    }

    @Test
    void calculateEnergyLossIncreasesWhenNoNeighborsAndUpdatesCurrentGeneTowardNearestAnimal() {
        //given
        Genome genome = new Genome(new ArrayList<>(List.of(0)));
        Animal animal = new Animal(new Vector2d(0, 0), 50, genome);
        Animal neighbor = new Animal(new Vector2d(2, 0), 50, new Genome(new ArrayList<>(List.of(0))));
        List<Animal> animals = List.of(animal, neighbor);
        double worldTemperature = -10.0;
        double warmDist = 1.0;
        int mapWidth = 5;

        //when
        int loss = animal.calculateEnergyLoss(worldTemperature, animals, warmDist, mapWidth);

        //then
        assertEquals(30, loss);
        assertEquals(2, animal.getGenome().getCurrGene());
    }

    @Test
    void calculateEnergyLossStaysBaseWhenAtLeastOneNeighborIsWithinWarmDistance() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 50, new Genome(new ArrayList<>(List.of(0))));
        Animal neighbor = new Animal(new Vector2d(1, 0), 50, new Genome(new ArrayList<>(List.of(0))));
        List<Animal> animals = List.of(animal, neighbor);
        double worldTemperature = -10.0;
        double warmDist = 1.0;
        int mapWidth = 5;

        //when
        int loss = animal.calculateEnergyLoss(worldTemperature, animals, warmDist, mapWidth);

        //then
        assertEquals(2, loss);
    }
}
