package agh.ics.oop.map;

import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void toStringReturnsPolishDirectionNames() {
        //given
        MapDirection north = MapDirection.NORTH;
        MapDirection southWest = MapDirection.SOUTH_WEST;

        //when
        String n = MapDirection.toString(north);
        String sw = MapDirection.toString(southWest);

        //then
        assertEquals("Północ", n);
        assertEquals("Południowy-Zachód", sw);
    }

    @Test
    void nextAndPreviousCreateAConsistentCycle() {
        //given
        MapDirection start = MapDirection.NORTH;

        //when
        MapDirection after8 = start;
        for (int i = 0; i < 8; i++) {
            after8 = MapDirection.next(after8);
        }
        MapDirection next = MapDirection.next(start);
        MapDirection back = MapDirection.previous(next);

        //then
        assertEquals(start, after8);
        assertEquals(start, back);
    }

    @Test
    void oppositeIsHalfTurnInTheDirectionCycle() {
        //given
        MapDirection d1 = MapDirection.NORTH;
        MapDirection d2 = MapDirection.NORTH_EAST;

        //when
        MapDirection opp1 = MapDirection.opposite(d1);
        MapDirection opp2 = MapDirection.opposite(d2);

        //then
        assertEquals(MapDirection.SOUTH, opp1);
        assertEquals(MapDirection.SOUTH_WEST, opp2);
    }

    @Test
    void toUnitVectorReturnsExpectedVectors() {
        //given
        MapDirection ne = MapDirection.NORTH_EAST;
        MapDirection w = MapDirection.WEST;

        //when
        Vector2d neVec = ne.toUnitVector();
        Vector2d wVec = w.toUnitVector();

        //then
        assertEquals(new Vector2d(1, 1), neVec);
        assertEquals(new Vector2d(-1, 0), wVec);
    }
}
