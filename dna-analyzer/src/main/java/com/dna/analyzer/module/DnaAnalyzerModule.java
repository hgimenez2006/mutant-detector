package com.dna.analyzer.module;

import com.dna.analyzer.sender.DnaSender;
import com.dna.analyzer.sender.DnaSqsSender;
import com.dna.analyzer.sender.AmazonSqsFactory;
import com.dna.analyzer.service.detector.HumbleMutantDetector;
import com.dna.analyzer.service.MutantDetector;
import com.dna.analyzer.service.DnaService;
import com.google.inject.AbstractModule;

public class DnaAnalyzerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaService.class);
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(DnaSender.class).to(DnaSqsSender.class);
        bind(AmazonSqsFactory.class);
   }
}

