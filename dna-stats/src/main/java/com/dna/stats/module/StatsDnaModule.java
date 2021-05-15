package com.dna.stats.module;

import com.dna.common.PropertyLoader;
import com.dna.stats.repository.DnaRepository;
import com.dna.stats.repository.MongoDnaRepository;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class StatsDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaRepository.class).to(MongoDnaRepository.class);

        Names.bindProperties(binder(), PropertyLoader.getProperties(this));
    }
}
