package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;

public interface MutantDetector {
    DnaResult detectMutant(String[] dna) throws InvalidDnaException;
}
