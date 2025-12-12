package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {
    @Test
    void correctAmountOfGrassIsBeingAddedToTheMap() {
        //given
        GrassField grassField = new GrassField(10);

        //when,then
        assertEquals(10, grassField.getElements().size());
    }

    @Test
    void animalHasHigherPriorityThanGrass() {
        //given
        GrassField grassField = new GrassField(10);

        //when
        Vector2d grassPos = grassField.getElements().getFirst().getPosition();
        Animal animal = new Animal(grassPos);
        assertDoesNotThrow(() -> grassField.place(animal));

        //then
        assertEquals(animal, grassField.objectAt(grassPos));
    }

    @Test
    void animalsCanNotMoveToTheSamePosition() {
        //given
        GrassField grassField = new GrassField(10);
        Animal animal1 = new Animal(new Vector2d(2, 3));
        Animal animal2 = new Animal(new Vector2d(2, 2));


        assertDoesNotThrow(() -> grassField.place(animal1));
        assertDoesNotThrow(() -> grassField.place(animal2));


        //when
        grassField.move(animal2, MoveDirection.FORWARD);

        //then
        assertEquals(new Vector2d(2, 2), animal2.getPosition());
    }

    @Test
    void animalsCanNotBePlacedOnEachOther() {
        //given
        GrassField grassField = new GrassField(0);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));

        //when
        assertDoesNotThrow(() -> grassField.place(animal1));


        //then
        assertThrowsExactly(IncorrectPositionException.class, () -> grassField.place(animal2));

    }

}