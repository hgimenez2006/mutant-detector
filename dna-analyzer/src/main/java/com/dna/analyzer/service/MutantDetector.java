package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.common.DnaResult;

public interface MutantDetector {
    DnaResult detectMutant(String[] dna) throws InvalidDnaException;
}
