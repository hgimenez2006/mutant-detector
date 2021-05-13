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
        charFound = '\u0000';
    }

    public void addCount(){
        count++;
    }

    public boolean isSameCharThanPrevious(char currChar){
        boolean res = this.charFound == currChar;
        if (res){
            //System.out.println("same char found -> " + charFound + "=" + currChar);
        }
        return res;
    }

    // encontre n caracteres seguidos
    public boolean isSequenceFound(){
        return this.count == mutantSequenceSize;
    }
}
