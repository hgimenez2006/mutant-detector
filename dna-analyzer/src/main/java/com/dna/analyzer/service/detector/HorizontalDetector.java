package com.dna.analyzer.service.detector;

public class HorizontalDetector implements SequenceDetector{
    private CharCount charCount;
    private int mutantSequenceSize;

    public HorizontalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
        charCount = new CharCount(mutantSequenceSize);
    }

    public int detect(int colIndex, char currChar, int sequenceCount) {
        // TODO: we could have an adapter here
        return detect(currChar, sequenceCount);
    }

    private int detect(char currChar, int sequenceCount){
        if (charCount.isSameCharThanPrevious(currChar)){
            charCount.addCount();
            if (charCount.isSequenceFound()) {
                System.out.println("horizontal -> " + currChar);
                sequenceCount++;
                /*if (sequenceCount == mutantSequenceSize){
                    // we found a mutant sequence : n number of same characters together
                    return sequenceCount;
                }*/
                charCount.reset();
            }
            else {
                charCount.setCharFound(currChar);
            }
        }
        else{
            charCount.reset();
            charCount.setCharFound(currChar);
        }

        return sequenceCount;
    }

    public void nextRow(){
        charCount = new CharCount(mutantSequenceSize);
    }
}
