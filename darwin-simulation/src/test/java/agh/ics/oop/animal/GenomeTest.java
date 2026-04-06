package agh.ics.oop.animal;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    void defaultConstructorCreatesGenomeOfDefaultLengthWithValidGenes() {
        //given
        Genome genome = new Genome();

        //when
        List<Integer> seq = genome.getSequence();

        //then
        assertEquals(10, seq.size());
        for (Integer gene : seq) {
            assertNotNull(gene);
            assertTrue(gene >= 0 && gene <= 7);
        }
    }

    @Test
    void getAndSetCurrGeneOperateOnCurrentIndex() {
        //given
        List<Integer> seq = new ArrayList<>(List.of(1, 2, 3));
        Genome genome = new Genome(seq);

        //when
        int before = genome.getCurrGene();
        genome.setCurrGene(7);
        int after = genome.getCurrGene();

        //then
        assertEquals(1, before);
        assertEquals(7, after);
        assertEquals(7, genome.getSequence().get(0));
    }

    @Test
    void updateCurrGeneCyclesThroughSequence() {
        //given
        Genome genome = new Genome(new ArrayList<>(List.of(0, 4)));

        //when
        int g1 = genome.getCurrGene();
        genome.updateCurrGene();
        int g2 = genome.getCurrGene();
        genome.updateCurrGene();
        int g3 = genome.getCurrGene();

        //then
        assertEquals(0, g1);
        assertEquals(4, g2);
        assertEquals(0, g3);
    }
}
