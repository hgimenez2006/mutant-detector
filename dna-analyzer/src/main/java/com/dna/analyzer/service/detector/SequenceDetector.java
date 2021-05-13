package com.dna.analyzer.service.detector;

public interface SequenceDetector {
    int detect(int colIndex, char currChar, int sequenceSize);
    void nextRow();
}