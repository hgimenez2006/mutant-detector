package com.dna.analyzer.service.detector.sequence;

import com.dna.analyzer.service.detector.CharCount;
import com.dna.analyzer.service.detector.SequenceDetector;

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

    public int detect(int colIndex, char currChar, int sequenceCount){
        int index = isRigthDiagonal()? colIndex-1 : colIndex+1;
        CharCount diagonalCount = diagonalMatchesPrevRow.get(index);

        if (diagonalCount != null){
            if (diagonalCount.isSameCharThanPrevious(currChar)){
                diagonalCount.addCount();
                if (diagonalCount.isSequenceFound()) {
                    String tipo = isRigthDiagonal()? "right" : "left";
                    System.out.println("diagonal " + tipo + " -> " + currChar + " [colIndex=" + colIndex + " - index=" + index+ "]");
                    sequenceCount++;
                    /*if (sequenceSize == mutantSequenceSize){
                        // we found a mutant sequence : n number of same characters together
                        return sequenceSize;
                    }*/
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
        //diagonalCount.setCharFound(currChar);
        diagonalMatchesCurrRow.put(colIndex, diagonalCount);
        return sequenceCount;
    }

    public void prepareForNextRow(){
        diagonalMatchesPrevRow = diagonalMatchesCurrRow;
        diagonalMatchesCurrRow = new HashMap<>();
    }
}
