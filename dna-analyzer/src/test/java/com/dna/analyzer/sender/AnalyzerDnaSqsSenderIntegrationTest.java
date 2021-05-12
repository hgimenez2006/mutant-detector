package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class AnalyzerDnaSqsSenderIntegrationTest {

    /**
     * Sends message to dna-queue
     */
    //@Test
    public void testSend() {
        DnaSender analyzedDnaSqsSender = new DnaSqsSender();

        String dna = RandomStringUtils.randomAlphabetic(1000000);
        DnaResult dnaResult = new DnaResult(true, dna);
        analyzedDnaSqsSender.sendAnalyzedDnaToPersister(dnaResult);
    }
}
