package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

abstract class DiagonalDetector implements SequenceDetector {
    private Map<Integer, CharCount> diagonalMatchesCurrRow = new HashMap<>();
    private Map<Integer, CharCount> diagonalMatchesPrevRow = new HashMap<>();
    private int mutantSequenceSize;

    public DiagonalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    abstract boolean isRigthDiagonal();

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize){
        int index = isRigthDiagonal()? colIndex-1 : colIndex+1;
        CharCount diagonalCount = diagonalMatchesPrevRow.get(index);
        boolean sequenceDetected=false;

        if (diagonalCount != null){
            if (diagonalCount.isSameCharThanPrevious(currChar)){
                diagonalCount.addCount();
                if (diagonalCount.isSequenceFound()) {
                    sequenceDetected = true;
                    diagonalCount.reset();
                }
            }
            else{
                diagonalCount = new CharCount(mutantSequenceSize);
                diagonalCount.setCharFound(currChar);
            }
        }
        else{
            diagonalCount = new CharCount(mutantSequenceSize);
            diagonalCount.setCharFound(currChar);
        }
        diagonalMatchesCurrRow.put(colIndex, diagonalCount);

        if (colIndex == (rowSize-1)){
            prepareForNextRow();
        }

        return sequenceDetected;
    }

    private void prepareForNextRow(){
        diagonalMatchesPrevRow = diagonalMatchesCurrRow;
        diagonalMatchesCurrRow = new HashMap<>();
    }
}
