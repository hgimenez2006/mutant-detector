package com.dna.analyzer.service.detector;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import com.dna.analyzer.service.MutantDetector;
import com.dna.common.Environment;

import java.util.HashMap;
import java.util.Map;

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
        // The key of all this maps is the column number
        Map<Integer, CharCount> verticalMatches = new HashMap<>();
        Map<Integer, CharCount> diagonal1MatchesCurrRow = new HashMap<>();
        Map<Integer, CharCount> diagonal1MatchesPrevRow = new HashMap<>();
        Map<Integer, CharCount> diagonal2MatchesCurrRow = new HashMap<>();
        Map<Integer, CharCount> diagonal2MatchesPrevRow = new HashMap<>();

        int rowSize = dna[0].length();
        int sequenceCount=0;

        for (int rowIndex=0; rowIndex<dna.length && sequenceCount<mutantSequenceCount; rowIndex++){
            // for each row verify size. should be equals to the first row
            String row = dna[rowIndex];
            DnaValidator.validateDnaRow(rowSize, row, rowIndex);

            CharCount horizontalMatches = new CharCount(mutantSequenceSize);

            for (int colIndex=0; colIndex<row.length(); colIndex++){
                char currChar = row.charAt(colIndex);
                DnaValidator.validateChar(currChar);

                if (isSequenceCountReached(sequenceCount)){
                    // a mutant was found. we continue just to validate all dna characters
                    break;
                }

                sequenceCount = HorizontalChecker.verifyHorizontal(horizontalMatches, currChar, sequenceCount,
                        mutantSequenceSize);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = VerticalChecker.verifyVertical(verticalMatches, colIndex, currChar, sequenceCount,
                        mutantSequenceSize);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = DiagonalChecker.verifyDiagonal(diagonal1MatchesPrevRow, diagonal1MatchesCurrRow, colIndex,
                        currChar, sequenceCount, true, mutantSequenceSize);
                if (isSequenceCountReached(sequenceCount)) {
                    break;
                }

                sequenceCount = DiagonalChecker.verifyDiagonal(diagonal2MatchesPrevRow, diagonal2MatchesCurrRow, colIndex,
                        currChar, sequenceCount, false, mutantSequenceSize);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }
            }

            diagonal1MatchesPrevRow = diagonal1MatchesCurrRow;
            diagonal1MatchesCurrRow = new HashMap<>();

            diagonal2MatchesPrevRow = diagonal2MatchesCurrRow;
            diagonal2MatchesCurrRow = new HashMap<>();
        }

        boolean isMutant = isSequenceCountReached(sequenceCount);
        String completeDna = String.join("", dna);
        return new DnaResult(isMutant, completeDna);
    }

    private boolean isSequenceCountReached(int sequenceCount){
        return sequenceCount == mutantSequenceCount;
    }
}
