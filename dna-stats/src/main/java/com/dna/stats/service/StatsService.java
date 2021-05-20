package com.dna.stats.service;

import com.dna.stats.dto.Stats;
import com.dna.stats.repository.DnaRepository;

import javax.inject.Inject;

public class StatsService {
    private DnaRepository dnaRepository;

    @Inject
    public StatsService(final DnaRepository dnaRepository){
        this.dnaRepository = dnaRepository;
    }

    public Stats getStats(){
        long humanCount = dnaRepository.getHumanCount();
        long mutantCount = dnaRepository.getMutantCount();
        float ratio = mutantCount;
        if (humanCount > 0){
            ratio = (float)mutantCount/humanCount;
        }

        Stats stats = Stats.builder()
                .count_human_dna(humanCount)
                .count_mutant_dna(mutantCount)
                .ratio(ratio)
                .build();

        return stats;
    }
}
