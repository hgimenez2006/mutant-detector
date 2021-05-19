package com.dna.analyzer.sender;

import com.dna.common.DnaResult;

public interface DnaSender {
    void sendAnalyzedDnaToPersister(DnaResult detectionResult);
}
