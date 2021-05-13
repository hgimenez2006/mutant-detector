package com.dna.analyzer.service.detector;

import java.util.Map;

public class DiagonalChecker {
    public static int verifyDiagonal(Map<Integer,CharCount> diagonalMatchesPrevRow,
                               Map<Integer,CharCount> diagonalMatchesCurrRow,
                               int colIndex, char currChar,
                               int sequenceSize, boolean isDiagonal1,
                               int mutantSequenceSize){
        int index = isDiagonal1? colIndex-1 : colIndex+1;
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

}
