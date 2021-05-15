package com.dna.analyzer.service.detector;

public class HorizontalDetector implements SequenceDetector {
    private CharCount horizontalCount;
    private int mutantSequenceSize;

    public HorizontalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
        horizontalCount = new CharCount(mutantSequenceSize);
    }

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize) {
        boolean sequenceDetected = CharCountProcessor.processCurrentChar(horizontalCount, currChar);

        if (colIndex == (rowSize-1)){
            prepareForNextRow();
        }

        return sequenceDetected;
    }

    private void prepareForNextRow(){
        horizontalCount = new CharCount(mutantSequenceSize);
    }
}
