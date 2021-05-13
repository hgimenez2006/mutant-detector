package com.dna.analyzer.service.detector;

import lombok.Data;

@Data
public class CharCount {
    private char charFound;
    private int count;
    private int mutantSequenceSize; // equal characters to be a mutant

    public CharCount(int mutantSequenceSize){
        this.count = 1;
        this.mutantSequenceSize = mutantSequenceSize;
    }

    public void reset() {
        count = 1;
    }

    public void addCount(){
        count++;
    }

    public boolean isSameCharThanPrevious(char currChar){
        return this.charFound == currChar;
    }

    // encontre n caracteres seguidos
    public boolean isSequenceFound(){
        return this.count == mutantSequenceSize;
    }
}
