package com.dna.persister.repository;


import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.DnaKeyBuilderSHA256;
import org.junit.Assert;
import org.junit.Test;

public class DnaKeyBuilderSHA256Test {

    @Test
    public void buildKey(){
        DnaKeyBuilder dnaKeyBuilder = new DnaKeyBuilderSHA256();
        String key = dnaKeyBuilder.buildKey("AAGTTGTGCCCCGAGA");
        Assert.assertNotNull(key);
    }

}
