package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.sender.DnaSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DnaServiceTest {
    @Mock
    private DnaSender dnaSender;
    @Mock
    private MutantDetector mutantDetector;
    @InjectMocks
    private DnaService dnaService;

    @Test
    public void analyzeAndSendResult() throws InvalidDnaException {
        String[] dna = null;
        DnaResult dnaResult = DnaResult.builder().mutant(true).dna("AAAAGGGG").build();
        when(mutantDetector.detectMutant(dna)).thenReturn(dnaResult);
        dnaService.analyzeDnaAndSendResult(dna);
        verify(dnaSender).sendAnalyzedDnaToPersister(dnaResult);
    }
}
