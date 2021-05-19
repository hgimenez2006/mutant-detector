package com.dna.analyzer.integration;

import com.dna.analyzer.module.DnaAnalyzerModule;
import com.dna.analyzer.sender.AmazonSqsFactory;
import com.dna.analyzer.sender.DnaSender;
import com.dna.analyzer.sender.DnaSqsSender;
import com.dna.analyzer.service.DnaResult;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class DnaSqsSenderIntegrationTest {

    @Test
    public void sendBigMessageToSqs() {
        Injector injector = Guice.createInjector(new DnaAnalyzerModule());
        DnaSender analyzedDnaSqsSender = injector.getInstance(DnaSender.class);
        String dna = RandomStringUtils.randomAlphabetic(1000000);
        DnaResult dnaResult = new DnaResult(true, dna);
        analyzedDnaSqsSender.sendAnalyzedDnaToPersister(dnaResult);
    }
}
