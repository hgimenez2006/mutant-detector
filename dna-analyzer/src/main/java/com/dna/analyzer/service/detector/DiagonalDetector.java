package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

abstract class DiagonalDetector implements SequenceDetector {
    // holds the count of matches for current row
    private Map<Integer, CharCount> diagonalMatchesCurrRow = new HashMap<>();

    // holds the count of matches for previous row
    private Map<Integer, CharCount> diagonalMatchesPrevRow = new HashMap<>();

    private int mutantSequenceSize;

    public DiagonalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    abstract boolean isRigthDiagonal();

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize){
        int index = isRigthDiagonal() ? colIndex-1 : colIndex+1;
        CharCount diagonalCount = diagonalMatchesPrevRow.get(index);
        if (diagonalCount == null){
            diagonalCount = new CharCount(mutantSequenceSize);
        }

        // puts the current char in the column being processed
        diagonalMatchesCurrRow.put(colIndex, diagonalCount);

        boolean sequenceDetected = CharCountProcessor.processCurrentChar(diagonalCount, currChar);

        if (colIndex == (rowSize-1)){
            // end of row reached
            prepareForNextRow();
        }

        return sequenceDetected;
    }

    private void prepareForNextRow(){
        diagonalMatchesPrevRow = diagonalMatchesCurrRow;
        diagonalMatchesCurrRow = new HashMap<>();
    }
}
