package agh.ics.oop.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeasonManagerTest {

    @Test
    void startsInSummerOnDayOne() {
        //given
        SeasonManager manager = new SeasonManager(10, -10);

        //when
        boolean isWinter = manager.isWinter();
        double temp = manager.getCurrentTemperature();

        //then
        assertFalse(isWinter);
        assertEquals(20.0, temp);
    }

    @Test
    void entersWinterAfterSeasonDurationDays() {
        //given
        SeasonManager manager = new SeasonManager(10, -10);
        for (int i = 0; i < 10; i++) {
            manager.nextDay();
        }

        //when
        boolean isWinter = manager.isWinter();

        //then
        assertTrue(isWinter);
        assertEquals(11, manager.getCurrentDay());
    }

    @Test
    void winterTemperatureFollowsCurrentImplementationShape() {
        //given
        SeasonManager manager = new SeasonManager(10, -10);
        for (int i = 0; i < 10; i++) {
            manager.nextDay();
        }

        //when
        double day11 = manager.getCurrentTemperature();
        manager.nextDay();
        manager.nextDay();
        double day13 = manager.getCurrentTemperature();
        manager.nextDay();
        manager.nextDay();
        manager.nextDay();
        double day16 = manager.getCurrentTemperature();
        manager.nextDay();
        manager.nextDay();
        double day18 = manager.getCurrentTemperature();

        //then
        assertEquals(0.0, day11, 0.0);
        assertEquals(-4.0, day13);
        assertEquals(-10.0, day16);
        assertEquals(-6.0, day18);
    }

    @Test
    void returnsToSummerAfterTwoSeasonDurations() {
        //given
        SeasonManager manager = new SeasonManager(10, -10);
        for (int i = 0; i < 20; i++) {
            manager.nextDay();
        }

        //when
        boolean isWinter = manager.isWinter();
        double temp = manager.getCurrentTemperature();

        //then
        assertFalse(isWinter);
        assertEquals(20.0, temp);
        assertEquals(21, manager.getCurrentDay());
    }
}
