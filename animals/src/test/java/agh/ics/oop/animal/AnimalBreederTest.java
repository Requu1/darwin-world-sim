package agh.ics.oop.animal;

import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalBreederTest {

    @Test
    void reproduceCreatesNewbornWithCorrectAttributes() {
        //given
        Vector2d position = new Vector2d(2, 2);
        Animal parent1 = new Animal(position, 80, new Genome(new ArrayList<>(List.of(0, 1, 2, 3))));
        parent1.addListener(new AnimalStatsUpdater());
        Animal parent2 = new Animal(position, 60, new Genome(new ArrayList<>(List.of(4, 5, 6, 7))));
        parent2.addListener(new AnimalStatsUpdater());

        int usedEnergyForReproduction = 10;
        int minMutationCount = 0;
        int maxMutationCount = 0;

        //when
        Animal child = AnimalBreeder.reproduce(parent1, parent2, usedEnergyForReproduction, minMutationCount, maxMutationCount);

        //then
        assertNotNull(child);
        assertEquals(position, child.getPosition());
        assertEquals(2 * usedEnergyForReproduction, child.getEnergy());
        assertEquals(parent1.getGenome().getSequence().size(), child.getGenome().getSequence().size());

        assertEquals(70, parent1.getEnergy());
        assertEquals(50, parent2.getEnergy());
        assertEquals(1, parent1.getStats().getChildrenCount());
        assertEquals(1, parent2.getStats().getChildrenCount());
    }
}