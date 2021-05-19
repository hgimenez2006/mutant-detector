package com.dna.analyzer.module;

import com.dna.analyzer.sender.DnaSender;
import com.dna.analyzer.sender.DnaSqsSender;
import com.dna.analyzer.service.HumbleMutantDetector;
import com.dna.analyzer.service.MutantDetector;
import com.dna.common.PropertyLoader;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Guice configuration module
 */
public class DnaAnalyzerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(DnaSender.class).to(DnaSqsSender.class);

        Names.bindProperties(binder(), PropertyLoader.getProperties(this));
   }
}

