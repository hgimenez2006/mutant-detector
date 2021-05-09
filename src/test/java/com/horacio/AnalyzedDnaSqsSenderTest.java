package com.horacio;

import com.horacio.mutant.service.AnalyzedDnaSender;
import com.horacio.mutant.service.AnalyzedDnaSqsSender;
import com.horacio.mutant.service.DnaResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class AnalyzedDnaSqsSenderTest {

   // @Test
    public void testSend() {
        AnalyzedDnaSender analyzedDnaSqsSender = new AnalyzedDnaSqsSender();

        String dna = RandomStringUtils.randomAlphabetic(1000000);
        DnaResult dnaResult = new DnaResult(true, dna);
        analyzedDnaSqsSender.sendAnalyzedDna(dnaResult);
    }
}
