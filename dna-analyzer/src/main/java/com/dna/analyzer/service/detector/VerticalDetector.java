package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

public class VerticalDetector implements SequenceDetector {
    private Map<Integer, CharCount> verticalMatches = new HashMap<>();
    private int mutantSequenceSize;

    public VerticalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize){
        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount == null){
            verticalCount = new CharCount(mutantSequenceSize);
            verticalMatches.put(colIndex, verticalCount);
        }

        boolean sequenceDetected = CharCountProcessor.processCharCount(verticalCount, currChar);
        return sequenceDetected;
    }
}
