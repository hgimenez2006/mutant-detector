package com.dna.stats.service;

import com.dna.stats.dto.Stats;
import com.dna.stats.repository.DnaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatsServiceTest {
    @Mock
    DnaRepository dnaRepository;
    @InjectMocks
    StatsService statsService;

    @Test
    public void getStats(){
        long humanCount = 100;
        long mutantCount = 40;
        float ratio = 0.4F;

        Mockito.when(dnaRepository.getHumanCount()).thenReturn(humanCount);
        Mockito.when(dnaRepository.getMutantCount()).thenReturn(mutantCount);
        Stats stats = statsService.getStats();
        Assert.assertNotNull(stats);
        assertEquals(humanCount, stats.getCount_human_dna());
        assertEquals(mutantCount, stats.getCount_mutant_dna());
        assertEquals(ratio, stats.getRatio(), 0);
    }
}
