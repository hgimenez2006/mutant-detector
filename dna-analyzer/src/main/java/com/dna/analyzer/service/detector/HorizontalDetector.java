package com.dna.analyzer.service.detector;

public class HorizontalDetector implements SequenceDetector{
    private CharCount charCount;
    private int mutantSequenceSize;

    public HorizontalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
        charCount = new CharCount(mutantSequenceSize);
    }

    public int detect(int colIndex, char currChar, int sequenceSize) {
        // TODO: we could have an adapter here
        return detect(currChar, sequenceSize);
    }

    private int detect(char currChar, int sequenceSize){
        if (charCount.isSameCharThanPrevious(currChar)){
            charCount.addCount();
            if (charCount.isSequenceFound()) {
                sequenceSize++;
                if (sequenceSize == mutantSequenceSize){
                    // we found a mutant sequence : n number of same characters together
                    return sequenceSize;
                }
                charCount.reset();
            }
        }
        else{
            charCount.reset();
        }
        charCount.setCharFound(currChar);
        return sequenceSize;
    }

    public void nextRow(){
        charCount = new CharCount(mutantSequenceSize);
    }
}