package com.dna.analyzer.service.detector;

import java.util.Map;

public class VerticalChecker {
    public static int verifyVertical(Map<Integer,CharCount> verticalMatches, int colIndex, char currChar,
                               int sequenceSize, int mutantSequenceSize){
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
}
