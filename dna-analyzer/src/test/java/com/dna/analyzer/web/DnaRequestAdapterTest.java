package com.dna.analyzer.web;

import com.dna.analyzer.exception.InvalidDnaException;
import org.junit.Assert;
import org.junit.Test;

public class DnaRequestAdapterTest {
    @Test
    public void handleRequest_emptyBody(){
        DnaRequestAdapter dnaRequestAdapter = new DnaRequestAdapter("");
        Assert.assertThrows(InvalidDnaException.class, () -> {
            dnaRequestAdapter.getDnaRequest();
        });
    }

    @Test
    public void handleRequest_malformedJson(){
        DnaRequestAdapter dnaRequestAdapter = new DnaRequestAdapter("{xx");
        Assert.assertThrows(InvalidDnaException.class, () -> {
            dnaRequestAdapter.getDnaRequest();
        });
    }
}
