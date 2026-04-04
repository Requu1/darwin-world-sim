package agh.ics.oop.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void toStringFormatsCoordinates() {
        //given
        Vector2d v = new Vector2d(2, -3);

        //when
        String s = v.toString();

        //then
        assertEquals("(2,-3)", s);
    }

    @Test
    void precedesAndFollowsWorkAsExpected() {
        //given
        Vector2d a = new Vector2d(1, 2);
        Vector2d b = new Vector2d(3, 4);
        Vector2d c = new Vector2d(0, 5);

        //when
        boolean aPrecedesB = a.precedes(b);
        boolean bFollowsA = b.follows(a);
        boolean aPrecedesC = a.precedes(c);

        //then
        assertTrue(aPrecedesB);
        assertTrue(bFollowsA);
        assertFalse(aPrecedesC);
    }

    @Test
    void addSubtractUpperRightLowerLeftOpposite() {
        //given
        Vector2d a = new Vector2d(2, 3);
        Vector2d b = new Vector2d(-5, 7);

        //when
        Vector2d add = a.add(b);
        Vector2d sub = a.subtract(b);
        Vector2d ur = a.upperRight(b);
        Vector2d ll = a.lowerLeft(b);
        Vector2d opp = a.opposite();

        //then
        assertEquals(new Vector2d(-3, 10), add);
        assertEquals(new Vector2d(7, -4), sub);
        assertEquals(new Vector2d(2, 7), ur);
        assertEquals(new Vector2d(-5, 3), ll);
        assertEquals(new Vector2d(-2, -3), opp);
    }

    @Test
    void equalsConsidersCoordinates() {
        //given
        Vector2d a1 = new Vector2d(1, 1);
        Vector2d a2 = new Vector2d(1, 1);
        Vector2d b = new Vector2d(1, 2);

        //when
        boolean eqSame = a1.equals(a2);
        boolean eqDifferent = a1.equals(b);
        boolean eqOtherType = a1.equals("(1,1)");

        //then
        assertTrue(eqSame);
        assertFalse(eqDifferent);
        assertFalse(eqOtherType);
    }
}
