package com.dna.analyzer.service.detector;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import com.dna.analyzer.service.MutantDetector;
import com.dna.common.Environment;

import java.util.Arrays;
import java.util.List;

public class HumbleMutantDetector implements MutantDetector {
    private int mutantSequenceSize; // cantidad de caracteres para ser mutante
    private int mutantSequenceCount; // cantidad de veces que se deben repetir las secuencias

    public HumbleMutantDetector(){
        String mutantSequenceSize = Environment.getInstance().get(Environment.Variable.MUTANT_CHAR, "4");
        String mutantSequenceCount = Environment.getInstance().get(Environment.Variable.MUTANT_SEQUENCE, "2");
        try{
           this.mutantSequenceSize = Integer.valueOf(mutantSequenceSize);
           this.mutantSequenceCount = Integer.valueOf(mutantSequenceCount);
        }catch(NumberFormatException e){
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

        HorizontalDetector horizontalDetector = new HorizontalDetector(mutantSequenceSize);
        VerticalDetector verticalDetector = new VerticalDetector(mutantSequenceSize);
        // detector for diagonal in one way
        DiagonalDetector leftDiagonalDetector = new LeftDiagonalDetector(mutantSequenceSize);
        // detector for diagonal in the other way
        DiagonalDetector rigthDiagonalDetector = new RigthDiagonalDetector(mutantSequenceSize);

        List<SequenceDetector> detectors = Arrays.asList(horizontalDetector, verticalDetector,
                leftDiagonalDetector, rigthDiagonalDetector);

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

                /*for (SequenceDetector detector : detectors){
                    sequenceCount = detector.detect(colIndex, currChar, sequenceCount);
                    if (isSequenceCountReached(sequenceCount)){
                        break;
                    }
                }
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }*/

                sequenceCount = horizontalDetector.detect(colIndex, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = verticalDetector.detect(colIndex, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = rigthDiagonalDetector.detect(colIndex, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)) {
                    break;
                }

                sequenceCount = leftDiagonalDetector.detect(colIndex, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }
            }

            //detectors.stream().forEach(detector -> detector.nextRow());
            horizontalDetector.nextRow();
            leftDiagonalDetector.nextRow();
            rigthDiagonalDetector.nextRow();
        }

        boolean isMutant = isSequenceCountReached(sequenceCount);
        String completeDna = String.join("", dna);
        return new DnaResult(isMutant, completeDna);
    }

    private boolean isSequenceCountReached(int sequenceCount){
        return sequenceCount == mutantSequenceCount;
    }
}
