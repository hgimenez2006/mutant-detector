package com.horacio.mutant.service;

import com.horacio.mutant.exception.InvalidDnaException;

import javax.inject.Inject;

//@Log4j2
public class RawDnaService {
    private final MutantDetector mutantDetector;
    private final AnalyzedDnaSender analyzedDnaSender;

    @Inject
    public RawDnaService(final AnalyzedDnaSender analyzedDnaSender,
                         final MutantDetector mutantDetector){
        this.analyzedDnaSender = analyzedDnaSender;
        this.mutantDetector = mutantDetector;
    }

    public DnaResult analyzeDnaAndSendResult(String[] dna) throws InvalidDnaException {
        DnaResult result = mutantDetector.detectMutant(dna);
        analyzedDnaSender.sendAnalyzedDna(result);

        return result;
    }
}


