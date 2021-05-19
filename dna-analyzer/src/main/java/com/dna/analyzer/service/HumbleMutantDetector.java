package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.detector.SequenceDetector;
import com.dna.analyzer.service.detector.SequenceDetectorFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * In charge of detecting a mutant.
 * Iterates the dna sequence char by char, one time. Iterates it till the end to verify that the sequence
 * is valida.
 * Future improvement: in case of validating the whole sequence was not necessary, have a property to stop
 * the iteration once a mutant was found.
 */
public class HumbleMutantDetector implements MutantDetector {
    private int mutantSequenceSize; // number of chars that make a mutant sequence
    private int mutantSequenceCount; // number of sequences that make a mutant dna
    private SequenceDetectorFactory sequenceDetectorFactory;

    @Inject
    public HumbleMutantDetector(@Named("mutant_seq_size") final int mutantSequenceSize,
                                @Named("mutant_seq_count") final int mutantSequenceCount,
                                final SequenceDetectorFactory sequenceDetectorFactory){
        if (mutantSequenceSize <= 0 || mutantSequenceCount <=0){
            throw new IllegalArgumentException("mutant_seq_size and mutant_seq_count should be greater than zero");
        }
        this.mutantSequenceCount = mutantSequenceCount;
        this.mutantSequenceSize = mutantSequenceSize;
        this.sequenceDetectorFactory = sequenceDetectorFactory;
    }

    @Override
    public DnaResult detectMutant(String[] dna) throws InvalidDnaException {
        DnaValidator.validateDna(dna);
        return analyzeDna(dna);
    }

    private DnaResult analyzeDna(String[] dna) throws InvalidDnaException {
        int rowSize = dna[0].length();
        int sequenceCount = 0;
        boolean mutantDetected = false;

        List<SequenceDetector> sequenceDetectors = sequenceDetectorFactory
                .getSequenceDetectors(mutantSequenceSize);

        for (int rowIndex=0; rowIndex<dna.length; rowIndex++){
            String row = dna[rowIndex];
            DnaValidator.validateDnaRowSize(rowSize, row, rowIndex);

            for (int colIndex=0; colIndex<row.length(); colIndex++){
                char currChar = row.charAt(colIndex);
                DnaValidator.validateChar(currChar);

                // if a mutant was found we continue iterating just to validate all dna characters
                if (!mutantDetected) {
                    for (SequenceDetector sequenceDetector : sequenceDetectors) {
                        boolean sequenceDetected = sequenceDetector.detect(colIndex, currChar, rowSize);
                        if (sequenceDetected) {
                            sequenceCount++;
                        }
                        if (sequenceCount == mutantSequenceCount) {
                            mutantDetected = true;
                            break;
                        }
                    }
                }
            }
        }

        String completeDna = String.join("", dna);
        return new DnaResult(mutantDetected, completeDna);
    }

}
