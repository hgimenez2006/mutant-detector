package com.horacio.integration;

import com.horacio.mutant.service.AnalyzedDnaSender;
import com.horacio.mutant.sqs.AnalyzedDnaSqsSender;
import com.horacio.mutant.service.DnaResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class AnalyzedDnaSqsSenderTest {

    /**
     * Sends message to dna-queue
     */
    //@Test
    public void testSend() {
        AnalyzedDnaSender analyzedDnaSqsSender = new AnalyzedDnaSqsSender();

        String dna = RandomStringUtils.randomAlphabetic(1000000);
        DnaResult dnaResult = new DnaResult(true, dna);
        analyzedDnaSqsSender.sendAnalyzedDna(dnaResult);
    }
}
