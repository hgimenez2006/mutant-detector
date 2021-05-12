package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.common.Environment;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

//@Log4j2
public class HumbleMutantDetector implements MutantDetector {
    private int mutantSequenceSize;
    private int mutantSequenceCount;

    public HumbleMutantDetector(){
        String mutantChar = Environment.getInstance().get(Environment.Variable.MUTANT_CHAR, "4");
        String mutantSequence = Environment.getInstance().get(Environment.Variable.MUTANT_SEQUENCE, "2");
        try{
           this.mutantSequenceSize = Integer.valueOf(mutantChar);
           this.mutantSequenceCount = Integer.valueOf(mutantSequence);
        }catch(NumberFormatException e){
            new RuntimeException(e);
        }
    }

    @Override
    public DnaResult detectMutant(String[] dna) throws InvalidDnaException {
        validateDna(dna);
        return analyzeDna(dna);
    }

    private void validateDna(String[] dna) throws InvalidDnaException {
        if (dna==null || dna.length == 0){
            throw new InvalidDnaException("dna is null or empty");
        }
    }

    private void validateDnaRow(int rowSize, String dnaRow, int rowIndex){
        if (StringUtils.isEmpty(dnaRow)){
            throw new IllegalArgumentException("dna row " + rowIndex + " is empty");
        }

        if (rowSize != dnaRow.length()){
            throw new IllegalArgumentException("dna row " + rowIndex + " has wrong length");
        }
    }

    /*private void validateChar(char dnaChar){
        if (dnaChar != 'A' && dnaChar != 'T' && dnaChar != 'C' && dnaChar != 'G'){
            throw new IllegalArgumentException("Char not valid: " + dnaChar);
        }
    }*/

    private DnaResult analyzeDna(String[] dna){

        //TODO enable/disble logging
        //log.debug("Analyzind Dna");

        Map<Integer, CharCount> verticalMatches = new HashMap<>(); // key = column, value = match count
        Map<Integer, CharCount> diagonal1MatchesCurrRow = new HashMap<>(); // key = column, value = match count
        Map<Integer, CharCount> diagonal1MatchesPrevRow = new HashMap<>();
        Map<Integer, CharCount> diagonal2MatchesCurrRow = new HashMap<>(); // key = column, value = match count
        Map<Integer, CharCount> diagonal2MatchesPrevRow = new HashMap<>();

        int rowSize = dna[0].length();
        int sequenceCount=0;

        for (int rowIndex=0; rowIndex<dna.length && sequenceCount<mutantSequenceCount; rowIndex++){
            // for each row verify size. should be >=4 and equal to the previous row
            String row = dna[rowIndex];
            validateDnaRow(rowSize, row, rowIndex);

            CharCount horizontalMatches = new CharCount();
            horizontalMatches.charFound =  ' ';

            for (int colIndex=0; colIndex<row.length(); colIndex++){
                char currChar = row.charAt(colIndex);
                //TODO: tiene sentido hacer esto? xq si encuentra 4 letras iguales validas no va a validar el resto
                //validateChar(currChar);

                sequenceCount = verifyHorizontal(horizontalMatches, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = verifyVertical(verticalMatches, colIndex, currChar, sequenceCount);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }

                sequenceCount = verifyDiagonal(diagonal1MatchesPrevRow, diagonal1MatchesCurrRow, colIndex,
                        currChar, sequenceCount, true);
                if (isSequenceCountReached(sequenceCount)) {
                    break;
                }

                sequenceCount = verifyDiagonal(diagonal2MatchesPrevRow, diagonal2MatchesCurrRow, colIndex,
                        currChar, sequenceCount, false);
                if (isSequenceCountReached(sequenceCount)){
                    break;
                }
            }

            diagonal1MatchesPrevRow = diagonal1MatchesCurrRow;
            diagonal1MatchesCurrRow = new HashMap<>();

            diagonal2MatchesPrevRow = diagonal2MatchesCurrRow;
            diagonal2MatchesCurrRow = new HashMap<>();

        }

        //TODO improve this
        String completeDna = String.join("", dna);
        return new DnaResult(sequenceCount==2, completeDna);
    }

    private boolean isSequenceCountReached(int sequenceCount){
        return sequenceCount == mutantSequenceCount;
    }

    private int verifyHorizontal(CharCount horizontalMatches, char currChar, int sequenceCount){
        if (horizontalMatches.isSameCharThanPrevious(currChar)){
            horizontalMatches.addCount();
            if (horizontalMatches.isSequenceFound()) {
                sequenceCount++;
                if (isSequenceCountReached(sequenceCount)){
                    return sequenceCount;
                }
                horizontalMatches.reset();
            }
        }
        else{
            horizontalMatches.reset();
        }
        horizontalMatches.charFound = currChar;
        return sequenceCount;
    }

    private int verifyDiagonal(Map<Integer,CharCount> diagonalMatchesPrevRow,
                               Map<Integer,CharCount> diagonalMatchesCurrRow,
                               int colIndex, char currChar,
                               int sequenceCount, boolean isDiagonal1){
        int index = isDiagonal1? colIndex-1 : colIndex+1;
        CharCount diagonalCount = diagonalMatchesPrevRow.get(index);
        if (diagonalCount != null){
            if (diagonalCount.isSameCharThanPrevious(currChar)){
                diagonalCount.addCount();
                if (diagonalCount.isSequenceFound()) {
                    sequenceCount++;
                    if (isSequenceCountReached(sequenceCount)){
                        return sequenceCount;
                    }
                    diagonalCount.reset();
                }
            }
        }
        else{
            diagonalCount = new CharCount();
        }
        diagonalCount.charFound = currChar;
        diagonalMatchesCurrRow.put(colIndex, diagonalCount);
        return sequenceCount;
    }

    private int verifyVertical(Map<Integer,CharCount> verticalMatches, int colIndex, char currChar,
                                int sequenceCount){
        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount != null){
            if (verticalCount.isSameCharThanPrevious(currChar)){
                verticalCount.addCount();
                if (verticalCount.isSequenceFound()) {
                    sequenceCount++;
                    if (isSequenceCountReached(sequenceCount)){
                        return sequenceCount;
                    }
                    verticalCount.reset();
                }
            }
        }else{
            verticalCount = new CharCount();
        }
        verticalCount.charFound = currChar;
        verticalMatches.put(colIndex, verticalCount);

        return  sequenceCount;
    }

    //TODO: mover clase para afuera?
    private class CharCount{
        private char charFound;
        private int count;

        public CharCount(){
            this.count = 1;
            charFound = ' ';
        }

        private void reset() {
            count = 1;
            charFound = ' ';
        }

        private void addCount(){
            count++;
        }

        private boolean isSameCharThanPrevious(char currChar){
            return this.charFound == currChar;
        }

        private boolean isSequenceFound(){
            //TODO esto que sea una property, y sacarle el 4letters al nombre de esta clase
            return this.count == mutantSequenceSize;
        }
    }
}
