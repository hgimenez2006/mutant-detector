package com.dna.analyzer.module;

import com.dna.analyzer.sender.DnaSender;
import com.dna.analyzer.sender.DnaSqsMockSender;
import com.dna.analyzer.sender.DnaSqsSender;
import com.dna.analyzer.service.HumbleMutantDetector;
import com.dna.analyzer.service.MutantDetector;
import com.google.inject.AbstractModule;

public class DnaAnalyzerModuleMock extends AbstractModule {
    @Override
    protected void configure() {
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(DnaSender.class).to(DnaSqsMockSender.class);
   }
}

