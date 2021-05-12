package com.dna.stats.module;

import com.dna.stats.repository.DnaRepository;
import com.dna.stats.repository.MongoDnaRepository;
import com.google.inject.AbstractModule;

public class StatsDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaRepository.class).to(MongoDnaRepository.class);
    }
}
