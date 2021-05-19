package com.dna.analyzer.service.detector;


public class CharCountProcessor {
    protected static boolean processCurrentChar(CharCount charCount, char currChar) {
        boolean sequenceDetected = false;
        if (charCount.isSameCharThanPrevious(currChar)) {
            charCount.addCount();
            if (charCount.isSequenceFound()) {
                sequenceDetected = true;
                charCount.reset();
            }
        } else {
            charCount.reset();
            charCount.setCharFound(currChar);
        }
        return sequenceDetected;
    }
}
