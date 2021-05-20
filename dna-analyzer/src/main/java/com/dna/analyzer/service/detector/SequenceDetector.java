package com.dna.analyzer.service.detector;

public interface SequenceDetector {
    boolean detect(int colIndex, char currChar, int rowSize);
}
