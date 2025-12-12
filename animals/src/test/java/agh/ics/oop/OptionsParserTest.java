package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class OptionsParserTest {

    @Test
    void noArgs() {
        //given
        String[] args = new String[0];

        //when
        ArrayList<MoveDirection> moves = OptionsParser.parse(args);

        //then
        assertEquals(new ArrayList<MoveDirection>(), moves);

    }


    @Test
    void allWrongArgs() {
        //given
        String[] args = {"123", "c", "m", "-2"};

        //when,then
        assertThrowsExactly(IllegalArgumentException.class, () -> OptionsParser.parse(args));
    }

    @Test
    void normalArgs() {
        //given
        String[] args = {"f", "b", "l", "r"};
        ArrayList<MoveDirection> expectedMoves = new ArrayList<>();
        expectedMoves.add(MoveDirection.FORWARD);
        expectedMoves.add(MoveDirection.BACKWARD);
        expectedMoves.add(MoveDirection.LEFT);
        expectedMoves.add(MoveDirection.RIGHT);

        //when
        ArrayList<MoveDirection> moves = OptionsParser.parse(args);

        //then
        assertEquals(expectedMoves, moves);


    }

    @Test
    void mixedArgs() {
        //given
        String[] args = {"123", "f", "p", "b", "x", "l", "q", "r", "546"};
        ArrayList<MoveDirection> expectedMoves = new ArrayList<>();
        expectedMoves.addLast(MoveDirection.FORWARD);
        expectedMoves.addLast(MoveDirection.BACKWARD);
        expectedMoves.addLast(MoveDirection.LEFT);
        expectedMoves.addLast(MoveDirection.RIGHT);

        //when,then
        assertThrowsExactly(IllegalArgumentException.class, () -> OptionsParser.parse(args));

    }
}