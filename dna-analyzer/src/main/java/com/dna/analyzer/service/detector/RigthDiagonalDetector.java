package com.dna.analyzer.service.detector;

public class RigthDiagonalDetector extends DiagonalDetector{
    public RigthDiagonalDetector(int mutantSequenceSize){
        super(mutantSequenceSize);
    }

    boolean isRigthDiagonal(){
        return false;
    }
}
