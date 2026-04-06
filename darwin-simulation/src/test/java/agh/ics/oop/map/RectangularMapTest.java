package agh.ics.oop.map;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.Genome;
import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    void placeAddsAnimalToPositionAndReturnsCopiesFromAnimalsAtPos() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(1, 1), 50, new Genome(new ArrayList<>(List.of(0))));

        //when
        map.place(animal);
        List<Animal> returned = map.animalsAtPos(new Vector2d(1, 1));
        returned.remove(animal);
        List<Animal> afterModification = map.animalsAtPos(new Vector2d(1, 1));

        //then
        assertNotNull(afterModification);
        assertEquals(1, afterModification.size());
        assertSame(animal, afterModification.get(0));
    }

    @Test
    void moveRelocatesAnimalBetweenCells() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(1, 1), 50, new Genome(new ArrayList<>(List.of(2))));
        map.place(animal);

        //when
        map.move(animal, 10);

        //then
        assertNull(map.animalsAtPos(new Vector2d(1, 1)));
        assertNotNull(map.animalsAtPos(new Vector2d(2, 1)));
        assertEquals(1, map.animalsAtPos(new Vector2d(2, 1)).size());
        assertSame(animal, map.animalsAtPos(new Vector2d(2, 1)).get(0));
    }

    @Test
    void growPlantsCreatesAtMostRequestedNumberOfPlantsWithinBoundsWithoutDuplicates() {
        //given
        RectangularMap map = new RectangularMap(10, 10);
        int plantsToGrow = 25;

        //when
        map.growPlants(plantsToGrow);

        //then
        assertTrue(map.getPlants().size() <= plantsToGrow);
        assertEquals(map.getPlants().size(), map.getPlants().stream().map(p -> p.getPosition()).distinct().count());
        assertTrue(map.getPlants().stream().allMatch(p -> p.getPosition().x() >= 0 && p.getPosition().x() <= 9));
        assertTrue(map.getPlants().stream().allMatch(p -> p.getPosition().y() >= 0 && p.getPosition().y() <= 9));
    }


    @Test
    void getCurrentBoundsReturnsExpectedCorners() {
        //given
        RectangularMap map = new RectangularMap(3, 4);

        //when
        Boundary bounds = map.getCurrentBounds();

        //then
        assertEquals(new Vector2d(2, 3), bounds.upperRightCorner());
        assertEquals(new Vector2d(0, 0), bounds.lowerLeftCorner());
    }
}
