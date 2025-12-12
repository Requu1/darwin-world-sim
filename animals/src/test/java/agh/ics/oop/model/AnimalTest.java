package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {
    @Test
    void animalHasCorrectFacingDirection() {
        //given
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();
        RectangularMap map = new RectangularMap(4, 4);

        //when
        animal1.move(MoveDirection.LEFT, map);
        animal2.move(MoveDirection.RIGHT, map);

        // then
        assertEquals(MapDirection.WEST, animal1.getFacingDirection());
        assertEquals(MapDirection.EAST, animal2.getFacingDirection());
    }

    @Test
    void animalMovesToCorrectPositions() {
        //given
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        Animal animal3 = new Animal(new Vector2d(2, 2));
        Animal animal4 = new Animal(new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        animal1.move(MoveDirection.FORWARD, map);

        animal2.move(MoveDirection.BACKWARD, map);

        animal3.move(MoveDirection.LEFT, map);
        animal3.move(MoveDirection.FORWARD, map);

        animal4.move(MoveDirection.RIGHT, map);
        animal4.move(MoveDirection.BACKWARD, map);
        //then
        assertTrue(animal1.isAt(new Vector2d(2, 3)));
        assertTrue(animal2.isAt(new Vector2d(2, 1)));
        assertTrue(animal3.isAt(new Vector2d(1, 2)));
        assertTrue(animal4.isAt(new Vector2d(1, 2)));
    }

    @Test
    void animalDoesNotGoOutsideTheMap() {
        //given
        Animal animal1 = new Animal(new Vector2d(4, 4));
        Animal animal2 = new Animal(new Vector2d(0, 0));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        animal1.move(MoveDirection.FORWARD, map);
        animal2.move(MoveDirection.BACKWARD, map);

        //then
        assertTrue(animal1.isAt(new Vector2d(4, 4)));
        assertTrue(animal2.isAt(new Vector2d(0, 0)));
    }

}