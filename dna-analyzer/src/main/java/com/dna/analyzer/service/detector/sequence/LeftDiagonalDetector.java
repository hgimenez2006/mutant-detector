package com.dna.analyzer.service.detector.sequence;

public class LeftDiagonalDetector extends DiagonalDetector{
    public LeftDiagonalDetector(int mutantSequenceSize){
        super(mutantSequenceSize);
    }

    boolean isRigthDiagonal(){
        return true;
    }
}
