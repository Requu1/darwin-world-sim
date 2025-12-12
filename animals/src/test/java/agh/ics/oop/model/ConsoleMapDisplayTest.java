package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleMapDisplayTest {

    @Test
    void amountOfUpdatesIsCorrect() {
        //given
        RectangularMap map = new RectangularMap(4, 4);
        ConsoleMapDisplay listener = new ConsoleMapDisplay();
        map.addListener(listener);
        Animal animal1 = new Animal(new Vector2d(1, 2));
        Animal animal2 = new Animal(new Vector2d(1, 2));

        //when

        assertDoesNotThrow(() -> map.place(animal1));
        map.move(animal1, MoveDirection.RIGHT);
        assertThrowsExactly(IncorrectPositionException.class, () -> map.place(animal2));


        //then
        assertEquals(2, listener.getCountUpdates());

    }

}