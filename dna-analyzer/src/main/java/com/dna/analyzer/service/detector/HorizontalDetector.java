package com.dna.analyzer.service.detector;

public class HorizontalDetector implements SequenceDetector {
    private CharCount charCount;
    private int mutantSequenceSize;

    public HorizontalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
        charCount = new CharCount(mutantSequenceSize);
    }

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize) {
        boolean sequenceDetected=false;
        if (charCount.isSameCharThanPrevious(currChar)){
            charCount.addCount();
            if (charCount.isSequenceFound()) {
                sequenceDetected = true;
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

        return sequenceDetected;
    }

    private void prepareForNextRow(){
        charCount = new CharCount(mutantSequenceSize);
    }
}
