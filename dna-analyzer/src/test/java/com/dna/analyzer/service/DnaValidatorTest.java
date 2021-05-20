package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import org.junit.Assert;
import org.junit.Test;

public class DnaValidatorTest {

    @Test
    public void rowSize_success() throws InvalidDnaException {
        DnaValidator.validateDnaRowSize(4,"AAAA",1);
    }

    @Test
    public void rowSize_error(){
        Assert.assertThrows(InvalidDnaException.class, () -> {
            DnaValidator.validateDnaRowSize(8, "AAAA", 1);
        });
    }

    @Test
    public void rowSizeEmpty_error(){
        Assert.assertThrows(InvalidDnaException.class, () -> {
            DnaValidator.validateDnaRowSize(8, "", 1);
        });
    }

    @Test
    public void dna_success() throws InvalidDnaException {
        DnaValidator.validateDna(new String[4]);
    }

    @Test
    public void dna_error(){
        Assert.assertThrows(InvalidDnaException.class, () -> {
            DnaValidator.validateDna(null);
        });
    }

    @Test
    public void char_success() throws InvalidDnaException {
        DnaValidator.validateChar('G');
    }

}
