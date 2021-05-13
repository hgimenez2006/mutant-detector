package com.dna.analyzer.service.detector;

public class HorizontalDetector implements SequenceDetector {
    private CharCount charCount;
    private int mutantSequenceSize;

    public HorizontalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
        charCount = new CharCount(mutantSequenceSize);
    }

    @Override
    public int detect(int colIndex, char currChar, int sequenceCount, int rowSize) {
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

        if (colIndex == (rowSize-1)){
            prepareForNextRow();
        }

        return sequenceCount;
    }

    private void prepareForNextRow(){
        charCount = new CharCount(mutantSequenceSize);
    }
}
