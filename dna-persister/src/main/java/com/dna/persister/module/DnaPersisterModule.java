package com.dna.persister.module;

import com.dna.common.PropertyLoader;
import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.DnaKeyBuilderSHA256;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.repository.MongoDnaRepository;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Guice configuration module
 */
public class DnaPersisterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaKeyBuilder.class).to(DnaKeyBuilderSHA256.class);
        bind(DnaRepository.class).to(MongoDnaRepository.class);

        Names.bindProperties(binder(), PropertyLoader.getProperties(this));
    }
}
