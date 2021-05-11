package com.dna.analyzer;

import com.dna.analyzer.sender.AnalyzedDnaSender;
import com.dna.analyzer.sender.AnalyzedDnaSqsSender;
import com.dna.analyzer.service.HumbleMutantDetector;
import com.dna.analyzer.service.MutantDetector;
import com.dna.analyzer.service.RawDnaService;
import com.google.inject.AbstractModule;

public class RawDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RawDnaService.class);
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(AnalyzedDnaSender.class).to(AnalyzedDnaSqsSender.class);

    }
}

