package com.dna.analyzer.service.detector;

import lombok.Data;

@Data
public class CharCount {
    private char charFound;
    private int count; // count of same characters found in sequence
    private int mutantSequenceSize; // amount of characters to be a mutant

    public CharCount(int mutantSequenceSize){
        this.count = 1;
        this.mutantSequenceSize = mutantSequenceSize;
    }

    public void reset() {
        count = 1;
        charFound = '\u0000';
    }

    public void addCount(){
        count++;
    }

    public boolean isSameCharThanPrevious(char currChar){
        return this.charFound == currChar;
    }

    public boolean isSequenceFound(){
        return this.count == mutantSequenceSize;
    }
}
