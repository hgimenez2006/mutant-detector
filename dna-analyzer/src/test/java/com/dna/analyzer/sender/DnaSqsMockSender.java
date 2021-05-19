package com.dna.analyzer.sender;

import com.dna.common.DnaResult;

public class DnaSqsMockSender implements DnaSender{
    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        System.out.println("mock sending to sqs - " + detectionResult.toString());
    }
}
