package com.dna.analyzer.service.detector;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import com.dna.analyzer.service.MutantDetector;
import com.dna.analyzer.service.detector.sequence.SequenceDetectorFactory;
import com.dna.common.Environment;

import javax.inject.Inject;
import java.util.List;

public class HumbleMutantDetector implements MutantDetector {
    private int mutantSequenceSize; // cantidad de caracteres para ser mutante
    private int mutantSequenceCount; // cantidad de veces que se deben repetir las secuencias
    private List<SequenceDetector> sequenceDetectors;

    @Inject
    public HumbleMutantDetector(final SequenceDetectorFactory sequenceDetectorFactory){
        String mutantSequenceSizeVar = Environment.getInstance().get(Environment.Variable.MUTANT_CHAR, "4");
        String mutantSequenceCountVar = Environment.getInstance().get(Environment.Variable.MUTANT_SEQUENCE, "2");

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
        int sequenceCount=0;

        for (int rowIndex=0; rowIndex<dna.length && sequenceCount<mutantSequenceCount; rowIndex++){
            String row = dna[rowIndex];
            DnaValidator.validateDnaRow(rowSize, row, rowIndex);

            for (int colIndex=0; colIndex<row.length(); colIndex++){
                char currChar = row.charAt(colIndex);
                DnaValidator.validateChar(currChar);

                if (isSequenceCountReached(sequenceCount)){
                    // a mutant was found. we continue just to validate all dna characters
                    break;
                }

                for (SequenceDetector sequenceDetector : sequenceDetectors){
                    sequenceCount = sequenceDetector.detect(colIndex, currChar, sequenceCount);
                    if (isSequenceCountReached(sequenceCount)){
                        break;
                    }
                }
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }
            }
            sequenceDetectors.stream().forEach(detector -> detector.prepareForNextRow());
        }

        boolean isMutant = isSequenceCountReached(sequenceCount);
        String completeDna = String.join("", dna);
        return new DnaResult(isMutant, completeDna);
    }

    private boolean isSequenceCountReached(int sequenceCount){
        return sequenceCount == mutantSequenceCount;
    }
}
