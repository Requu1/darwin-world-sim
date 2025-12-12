package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    @Test
    void movingAnimalWorks() {
        //given
        Animal animal = new Animal(new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(animal));

        //when
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.RIGHT);

        //then
        assertEquals(MapDirection.EAST, animal.getFacingDirection());
        assertEquals(new Vector2d(2, 3), animal.getPosition());
    }

    @Test
    void placingAnimalWorks() {
        //given
        Animal animal = new Animal(new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        assertDoesNotThrow(() -> map.place(animal));


        //then
        assertEquals(animal, map.objectAt(animal.getPosition()));
    }

    @Test
    void animalsCantMoveBeyondTheBounds() {
        //given
        Animal animal1 = new Animal(new Vector2d(4, 4));
        Animal animal2 = new Animal(new Vector2d(0, 0));
        RectangularMap map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(animal1));
        assertDoesNotThrow(() -> map.place(animal2));


        //when
        map.move(animal1, MoveDirection.FORWARD);
        map.move(animal2, MoveDirection.BACKWARD);

        //then
        assertEquals(new Vector2d(4, 4), animal1.getPosition());
        assertEquals(new Vector2d(0, 0), animal2.getPosition());
    }

    @Test
    void animalsCanNotBePlacedOnEachOther() {
        //given
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        assertDoesNotThrow(() -> map.place(animal1));


        //then
        assertThrowsExactly(IncorrectPositionException.class, () -> map.place(animal2));
    }

    @Test
    void animalsCanNotMoveToTheSamePosition() {
        //given
        RectangularMap rectangularMap = new RectangularMap(4, 4);
        Animal animal1 = new Animal(new Vector2d(2, 3));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        assertDoesNotThrow(() -> rectangularMap.place(animal1));
        assertDoesNotThrow(() -> rectangularMap.place(animal2));

        //when
        rectangularMap.move(animal2, MoveDirection.FORWARD);

        //then
        assertEquals(new Vector2d(2, 2), animal2.getPosition());
    }

}