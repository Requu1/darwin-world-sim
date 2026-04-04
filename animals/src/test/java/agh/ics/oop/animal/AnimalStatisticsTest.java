package agh.ics.oop.animal;

import agh.ics.oop.other.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalStatisticsTest {

    @Test
    void updateStatsIncrementsCounters() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 10, new Genome(new ArrayList<>(List.of(0, 0))));
        AnimalStatistics stats = animal.getStats();

        //when
        stats.updateStats(AnimalStatisticsData.ADD_DAYS_LIVED, animal);
        stats.updateStats(AnimalStatisticsData.ADD_DAYS_LIVED, animal);
        stats.updateStats(AnimalStatisticsData.ADD_CHILDREN_COUNT, animal);
        stats.updateStats(AnimalStatisticsData.ADD_PLANT_EATEN, animal);

        //then
        assertEquals(2, stats.getDaysLived());
        assertEquals(1, stats.getChildrenCount());
        assertEquals(1, stats.getPlantsEaten());
    }

    @Test
    void updateStatsCanSetDayOfDeathToDaysLived() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 10, new Genome(new ArrayList<>(List.of(0, 0))));
        AnimalStatistics stats = animal.getStats();
        stats.updateStats(AnimalStatisticsData.ADD_DAYS_LIVED, animal);
        stats.updateStats(AnimalStatisticsData.ADD_DAYS_LIVED, animal);
        stats.updateStats(AnimalStatisticsData.ADD_DAYS_LIVED, animal);

        //when
        stats.updateStats(AnimalStatisticsData.SET_DAY_OF_DEATH, animal);

        //then
        assertEquals(3, stats.getDayOfDeath());
    }

    @Test
    void updateStatsSynchronizesEnergyAndActiveGeneFromAnimal() {
        //given
        Animal animal = new Animal(new Vector2d(0, 0), 20, new Genome(new ArrayList<>(List.of(0))));
        AnimalStatistics stats = animal.getStats();
        animal.addEnergy(10);
        animal.getGenome().setCurrGene(5);

        //when
        stats.updateStats(AnimalStatisticsData.UPDATE_ANIMAL_ENERGY, animal);
        stats.updateStats(AnimalStatisticsData.UPDATE_ACTIVE_GENE, animal);

        //then
        assertEquals(30, stats.getEnergy());
        assertEquals(5, stats.getActiveGene());
    }
}
