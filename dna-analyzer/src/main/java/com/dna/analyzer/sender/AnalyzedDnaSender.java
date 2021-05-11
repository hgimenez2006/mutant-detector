package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;

public interface AnalyzedDnaSender {
    void sendAnalyzedDna(DnaResult detectionResult);
}
