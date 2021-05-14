package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;
import lombok.extern.slf4j.Slf4j;

public class DnaSqsMockSender implements DnaSender{
    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        System.out.println("mock sending to sqs - " + detectionResult.toString());
    }
}
