package agh.ics.oop.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void twoVectorsAddedToEachOther() {
        //given
        Vector2d vector1 = new Vector2d(5, 5);
        Vector2d vector2 = new Vector2d(2, 2);

        //when
        Vector2d vector3 = vector1.add(vector2);

        //then
        assertEquals(7, vector3.getX());
        assertEquals(7, vector3.getY());
    }

    @Test
    void twoVectorSubstractedFromEeachOther() {
        //given
        Vector2d vector1 = new Vector2d(5, 4);
        Vector2d vector2 = new Vector2d(2, 3);

        //when
        Vector2d vector2SubstractedFromVector1 = vector1.subtract(vector2);
        Vector2d vector1SubstractedFromVector2 = vector2.subtract(vector1);

        //then
        assertEquals(3, vector2SubstractedFromVector1.getX());
        assertEquals(1, vector2SubstractedFromVector1.getY());

        assertEquals(-3, vector1SubstractedFromVector2.getX());
        assertEquals(-1, vector1SubstractedFromVector2.getY());
    }

    @Test
    void createdTwoEqualVectors() {
        //when
        Vector2d vector1 = new Vector2d(5, 5);
        Vector2d vector2 = new Vector2d(5, 5);

        //then
        assertTrue(vector1.equals(vector2));
    }

    @Test
    void createdTwoDifferentVectors() {
        //when
        Vector2d vector1 = new Vector2d(5, 5);
        Vector2d vector2 = new Vector2d(3, 4);

        //then
        assertFalse(vector1.equals(vector2));
    }

    @Test
    void vectorToString() {
        //given
        Vector2d vector = new Vector2d(3, 4);

        assertEquals("(3,4)", vector.toString());
    }

    @Test
    void oneVectorPrecedesOtherVector() {
        //when
        Vector2d vector1 = new Vector2d(5, 5);
        Vector2d vector2 = new Vector2d(3, 4);

        //then
        assertTrue(vector2.precedes(vector1));
    }

    @Test
    void oneVectorFollowsOtherVector() {
        //when
        Vector2d vector1 = new Vector2d(5, 5);
        Vector2d vector2 = new Vector2d(3, 4);

        //then
        assertTrue(vector1.follows(vector2));
    }

    @Test
    void topRightCornerOfTheRectangleCreatedFromTwoVectors() {
        //given
        Vector2d vector1 = new Vector2d(5, 6);
        Vector2d vector2 = new Vector2d(3, 8);

        //when
        Vector2d vector3 = vector1.upperRight(vector2);

        //then
        assertEquals(5, vector3.getX());
        assertEquals(8, vector3.getY());

    }

    @Test
    void bottomLeftCornerOfTheRectangleCreatedFromTwoVectors() {
        //given
        Vector2d vector1 = new Vector2d(5, 6);
        Vector2d vector2 = new Vector2d(3, 8);

        //when
        Vector2d vector3 = vector1.lowerLeft(vector2);

        //then
        assertEquals(3, vector3.getX());
        assertEquals(6, vector3.getY());
    }

    @Test
    void creatingOppositeVector() {
        //given
        Vector2d vector = new Vector2d(3, 3);

        //when
        Vector2d oppositeVector = vector.opposite();

        //then
        assertEquals(-3, oppositeVector.getX());
        assertEquals(-3, oppositeVector.getY());
    }


}