package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

abstract class DiagonalDetector implements SequenceDetector{
    private Map<Integer, CharCount> diagonalMatchesCurrRow = new HashMap<>();
    private Map<Integer, CharCount> diagonalMatchesPrevRow = new HashMap<>();
    private int mutantSequenceSize;

    public DiagonalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    abstract boolean isRigthDiagonal();

    public int detect(int colIndex, char currChar, int sequenceSize){
        int index = isRigthDiagonal()? colIndex-1 : colIndex+1;
        CharCount diagonalCount = diagonalMatchesPrevRow.get(index);
        if (diagonalCount != null){
            if (diagonalCount.isSameCharThanPrevious(currChar)){
                diagonalCount.addCount();
                if (diagonalCount.isSequenceFound()) {
                    sequenceSize++;
                    if (sequenceSize == mutantSequenceSize){
                        // we found a mutant sequence : n number of same characters together
                        return sequenceSize;
                    }
                    diagonalCount.reset();
                }
            }
        }
        else{
            diagonalCount = new CharCount(mutantSequenceSize);
        }
        diagonalCount.setCharFound(currChar);
        diagonalMatchesCurrRow.put(colIndex, diagonalCount);
        return sequenceSize;
    }

    public void nextRow(){
        diagonalMatchesPrevRow = diagonalMatchesCurrRow;
        diagonalMatchesCurrRow = new HashMap<>();
    }
}