package agh.ics.oop.simulation;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.RectangularMap;
import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationStatsTest {

    @Test
    void computesAggregatesForAliveAnimalsAndMapState() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        ArrayList<Vector2d> positions = new ArrayList<>(List.of(
                new Vector2d(1, 1),
                new Vector2d(1, 1),
                new Vector2d(2, 2)
        ));
        Simulation simulation = new Simulation(
                positions,
                map,
                10,
                -20.0,
                40,
                0,
                1,
                5,
                1.0,
                10,
                50,
                5,
                0,
                0,
                4
        );
        List<Animal> animals = simulation.getAllAnimals();
        animals.get(0).addEnergy(10);
        animals.get(1).subtractEnergy(10);
        animals.get(0).addChildren();
        animals.get(0).addChildren();
        animals.get(1).addChildren();
        setAllGenes(animals.get(0), 1);
        setAllGenes(animals.get(1), 1);
        setAllGenes(animals.get(2), 2);


        //when
        SimulationStats stats = new SimulationStats(simulation);

        //then
        assertEquals(1, stats.getSimulationDay());
        assertEquals(3, stats.getAnimalsCount());
        assertEquals(4, stats.getPlantsCount());
        assertEquals(23, stats.getFreePositionsCount());
        assertEquals(40, stats.getAvgEnergyLevel());
        assertEquals(1, stats.getAvgChildrenCount());
        assertEquals(-1, stats.getAvgAnimalsLifeSpan());
        assertEquals(List.of(1, 1, 1, 1, 1), stats.getMostPopularGenome());
    }

    @Test
    void computesAverageLifeSpanForDeadAnimals() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        ArrayList<Vector2d> positions = new ArrayList<>(List.of(
                new Vector2d(0, 0),
                new Vector2d(1, 1),
                new Vector2d(1, 1)
        ));
        Simulation simulation = new Simulation(
                positions,
                map,
                10,
                -20.0,
                40,
                0,
                1,
                5,
                1.0,
                10,
                50,
                5,
                0,
                0,
                0
        );
        List<Animal> animals = simulation.getAllAnimals();
        Animal toDie = animals.get(0);
        toDie.nextDayLived();
        toDie.nextDayLived();
        toDie.subtractEnergy(100);
        simulation.getMap().removeAnimalFromTheMap(toDie);
        animals.get(1).subtractEnergy(10);
        animals.get(2).addEnergy(10);
        animals.get(1).addChildren();
        animals.get(2).addChildren();

        //when
        SimulationStats stats = new SimulationStats(simulation);

        //then
        assertEquals(2, stats.getAnimalsCount());
        assertEquals(24, stats.getFreePositionsCount());
        assertEquals(40, stats.getAvgEnergyLevel());
        assertEquals(1, stats.getAvgChildrenCount());
        assertEquals(2, stats.getAvgAnimalsLifeSpan());
    }

    private static void setAllGenes(Animal animal, int value) {
        List<Integer> seq = animal.getGenome().getSequence();
        for (int i = 0; i < seq.size(); i++) {
            seq.set(i, value);
        }
    }
}
