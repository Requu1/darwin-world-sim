package agh.ics.oop;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimulationTest {

    @Test
    void directionsAreIntreprettedCorrectlyForSingleAnimal() {
        //given
        String[] args = {"f", "r", "f", "l", "b"};
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);


        //when
        Simulation simulation = new Simulation(positions, OptionsParser.parse(args), map);
        simulation.run();

        //then
        Vector2d expectedEndPositionOfTheAnimal = new Vector2d(3, 2);
        MapDirection expectedEndDirectionOfTheAnimal = MapDirection.NORTH;

        assertEquals(expectedEndPositionOfTheAnimal, simulation.getAnimals().getFirst().getPosition());
        assertEquals(expectedEndDirectionOfTheAnimal, simulation.getAnimals().getFirst().getFacingDirection());
    }

    @Test
    void directionsAreInterprettedCorrectlyForMultipleAnimals() {
        //given
        String[] args = {"f", "r", "l", "b"};
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        Simulation simulation = new Simulation(positions, OptionsParser.parse(args), map);
        simulation.run();

        //then
        Vector2d expectedEndPositionOfTheFirstAnimal = new Vector2d(2, 3);
        Vector2d expectedEndPositionOfTheSecondAnimal = new Vector2d(2, 4);

        assertEquals(expectedEndPositionOfTheFirstAnimal, simulation.getAnimals().get(0).getPosition());
        assertEquals(expectedEndPositionOfTheSecondAnimal, simulation.getAnimals().get(1).getPosition());

        MapDirection expectedEndDirectionOfTheFirstAnimal = MapDirection.WEST;
        MapDirection expectedEndDirectionOfTheSecondAnimal = MapDirection.EAST;

        assertEquals(expectedEndDirectionOfTheFirstAnimal, simulation.getAnimals().get(0).getFacingDirection());
        assertEquals(expectedEndDirectionOfTheSecondAnimal, simulation.getAnimals().get(1).getFacingDirection());


    }

    @Test
    void animalsCantBePlacedOnTheSamePosition() {
        //given
        String[] args = {"f", "r", "l", "f"};
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        Simulation simulation = new Simulation(positions, OptionsParser.parse(args), map);

        //then
        assertEquals(1, simulation.getAnimals().size());
    }

    @Test
    void animalsOnTheMapCanNotMoveToTheOccupiedPosition() {
        //given
        String[] args = {"f"};
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(2, 3));
        RectangularMap map = new RectangularMap(4, 4);

        //when
        Simulation simulation = new Simulation(positions, OptionsParser.parse(args), map);
        simulation.run();

        //then
        assertEquals(new Vector2d(2, 2), simulation.getAnimals().get(0).getPosition());
    }

}