package com.dna.persister.repository;

import com.dna.persister.service.DnaResult;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

public class MongoDnaRepositoryTest {
    //@Mock
    //DnaKeyBuilderSHA256 dnaKeyBuilderSHA256;
    @Spy
    MongoDnaRepository mongoDnaRepository;

    @Test
    public void insertDnaResult(){
        MongoDnaRepository mongoDnaRepository = new MongoDnaRepository(new DnaKeyBuilderSHA256());
        DnaResult dnaResult = DnaResult.builder().mutant(true).dna("AAAA").build();
        mongoDnaRepository.insertDnaResult(dnaResult);
    }

    @Test
    public void buildDocument(){
        DnaKeyBuilder dnaKeyBuilder = new DnaKeyBuilderSHA256();
        String dna = "AAAA";

        DnaResult dnaResult = DnaResult.builder().mutant(true).dna(dna).build();

        MongoDnaRepository mongoDnaRepository = new MongoDnaRepository(dnaKeyBuilder);
        Document document = mongoDnaRepository.buildDocument(dnaResult);

        String key = dnaKeyBuilder.buildKey(dna);
        Assert.assertNotNull(document);
        Assert.assertEquals(document.get("_id"), key);
        Assert.assertEquals(document.get("dna"), dna);
        Assert.assertNotNull(document.get("createdAt"));
    }
}
