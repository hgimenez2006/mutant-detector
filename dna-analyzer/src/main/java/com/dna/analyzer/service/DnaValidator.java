package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import org.apache.commons.lang3.StringUtils;

public class DnaValidator {
    public static void validateDna(String[] dna) throws InvalidDnaException {
        if (dna==null || dna.length == 0){
            throw new InvalidDnaException("dna is null or empty");
        }
    }

    public static void validateDnaRowSize(int rowSize, String dnaRow, int rowIndex) throws InvalidDnaException{
        if (StringUtils.isEmpty(dnaRow)){
            throw new InvalidDnaException("dna row " + rowIndex + " is empty");
        }

        if (rowSize != dnaRow.length()){
            throw new InvalidDnaException("dna row " + rowIndex + " has wrong length");
        }
    }

    public static void validateChar(char dnaChar) throws InvalidDnaException {
        if (dnaChar != 'A' && dnaChar != 'T' && dnaChar != 'C' && dnaChar != 'G'){
            throw new InvalidDnaException("Char not valid found : " + dnaChar);
        }
    }
}
