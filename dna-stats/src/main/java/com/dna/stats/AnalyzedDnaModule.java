package com.dna.stats;

import com.dna.stats.repository.DnaRepository;
import com.dna.stats.repository.MongoDnaRepository;
import com.google.inject.AbstractModule;

public class AnalyzedDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaRepository.class).to(MongoDnaRepository.class);
    }
}
