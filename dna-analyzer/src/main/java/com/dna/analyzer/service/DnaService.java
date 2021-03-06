package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.sender.DnaSender;
import com.dna.common.DnaResult;

import javax.inject.Inject;

/**
 * In charge of analyzing the dna and sending the result to the persister
 */
public class DnaService {
    private final MutantDetector mutantDetector;
    private final DnaSender dnaSender;

    @Inject
    public DnaService(final DnaSender dnaSender,
                      final MutantDetector mutantDetector){
        this.dnaSender = dnaSender;
        this.mutantDetector = mutantDetector;
    }

    public DnaResult analyzeDnaAndSendResult(String[] dna) throws InvalidDnaException {
        DnaResult result = mutantDetector.detectMutant(dna);
        dnaSender.sendAnalyzedDnaToPersister(result);
        return result;
    }
}


