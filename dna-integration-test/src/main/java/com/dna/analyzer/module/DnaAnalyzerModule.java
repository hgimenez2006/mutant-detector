package com.dna.analyzer.module;

import com.dna.analyzer.sender.DnaSender;
import com.dna.analyzer.service.HumbleMutantDetector;
import com.dna.analyzer.service.MutantDetector;
import com.dna.common.PropertyLoader;
import com.dna.analyzer.sender.MockedDnaSqsSender;
import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.DnaKeyBuilderSHA256;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.repository.MongoDnaRepository;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

// Overrides the guice module definition for dna-analyzer to bind dna-analyzer with
// dna-persisted through MockedDnaSqsSender
public class DnaAnalyzerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(DnaSender.class).to(MockedDnaSqsSender.class);

        bind(DnaKeyBuilder.class).to(DnaKeyBuilderSHA256.class);
        bind(DnaRepository.class).to(MongoDnaRepository.class);

        Names.bindProperties(binder(), PropertyLoader.getProperties(this));
   }
}

