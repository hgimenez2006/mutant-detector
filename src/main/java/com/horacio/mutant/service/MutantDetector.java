package com.horacio.mutant.service;

import com.horacio.mutant.exception.InvalidDnaException;

public interface MutantDetector {
    DnaResult detectMutant(String[] dna) throws InvalidDnaException;
}
