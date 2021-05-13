package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

public class VerticalDetector implements SequenceDetector{
    private Map<Integer, CharCount> verticalMatches = new HashMap<>();
    private int mutantSequenceSize;

    public VerticalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    public int detect(int colIndex, char currChar, int sequenceSize){
        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount != null){
            if (verticalCount.isSameCharThanPrevious(currChar)){
                verticalCount.addCount();
                if (verticalCount.isSequenceFound()) {
                    sequenceSize++;
                    if (sequenceSize == mutantSequenceSize){
                        // we found a mutant sequence : n number of same characters together
                        return sequenceSize;
                    }
                    verticalCount.reset();
                }
            }
        }else{
            verticalCount = new CharCount(mutantSequenceSize);
        }
        verticalCount.setCharFound(currChar);
        verticalMatches.put(colIndex, verticalCount);

        return sequenceSize;
    }

    public void nextRow(){}
}
