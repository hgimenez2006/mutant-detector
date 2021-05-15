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
        boolean sequenceDetected = false;

        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount == null){
            verticalCount = new CharCount(mutantSequenceSize);
        }

        if (verticalCount.isSameCharThanPrevious(currChar)){
            verticalCount.addCount();
            if (verticalCount.isSequenceFound()) {
                sequenceDetected = true;
                verticalCount.reset();
            }
        }
        else{
            verticalCount.reset();
            verticalCount.setCharFound(currChar);
        }

        verticalMatches.put(colIndex, verticalCount);

        return sequenceDetected;
    }
}
