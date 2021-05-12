package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;

public interface DnaSender {
    void sendAnalyzedDnaToPersister(DnaResult detectionResult);
}
