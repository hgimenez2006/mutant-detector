package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.detector.SequenceDetector;
import com.dna.analyzer.service.detector.SequenceDetectorFactory;
import com.dna.common.Environment;

import javax.inject.Inject;
import java.util.List;

public class HumbleMutantDetector implements MutantDetector {
    private int mutantSequenceSize; // cantidad de caracteres para ser mutante
    private int mutantSequenceCount; // cantidad de veces que se deben repetir las secuencias
    private List<SequenceDetector> sequenceDetectors;

    @Inject
    public HumbleMutantDetector(final SequenceDetectorFactory sequenceDetectorFactory){
        String mutantSequenceSizeVar = Environment.getInstance()
                .get(Environment.Variable.MUTANT_CHAR, "4");
        String mutantSequenceCountVar = Environment.getInstance()
                .get(Environment.Variable.MUTANT_SEQUENCE, "2");
        try{
           mutantSequenceSize = Integer.valueOf(mutantSequenceSizeVar);
           mutantSequenceCount = Integer.valueOf(mutantSequenceCountVar);
           sequenceDetectors = sequenceDetectorFactory.getSequenceDetectors(mutantSequenceSize);
        }
        catch(NumberFormatException e){
            new RuntimeException(e);
        }
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
                //if (mutantDetected){
                    //break; --> we continue to analyze all dna
                //}
            }
        }

        String completeDna = String.join("", dna);
        return new DnaResult(mutantDetected, completeDna);
    }

}
