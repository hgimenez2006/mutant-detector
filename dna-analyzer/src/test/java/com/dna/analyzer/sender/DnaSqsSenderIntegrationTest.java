package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

public class DnaSqsSenderIntegrationTest {

    /**
     * Sends message to dna-queue
     */
    //@Test
    public void testSend() {
        AmazonSqsFactory amazonSqsFactory = new AmazonSqsFactory();
        DnaSender analyzedDnaSqsSender = new DnaSqsSender(amazonSqsFactory);

        String dna = RandomStringUtils.randomAlphabetic(1000000);
        DnaResult dnaResult = new DnaResult(true, dna);
        analyzedDnaSqsSender.sendAnalyzedDnaToPersister(dnaResult);
    }
}
