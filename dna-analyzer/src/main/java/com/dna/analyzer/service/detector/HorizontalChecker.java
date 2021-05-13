package com.dna.analyzer.service.detector;

public class HorizontalChecker {
    public static int verifyHorizontal(CharCount horizontalMatches, char currChar, int sequenceSize,
                                       int mutantSequenceSize){
        if (horizontalMatches.isSameCharThanPrevious(currChar)){
            horizontalMatches.addCount();
            if (horizontalMatches.isSequenceFound()) {
                sequenceSize++;
                if (sequenceSize == mutantSequenceSize){
                    // we found a mutant sequence : n number of same characters together
                    return sequenceSize;
                }
                horizontalMatches.reset();
            }
        }
        else{
            horizontalMatches.reset();
        }
        horizontalMatches.setCharFound(currChar);
        return sequenceSize;
    }
}
