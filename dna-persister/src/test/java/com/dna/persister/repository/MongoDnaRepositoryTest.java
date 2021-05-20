package com.dna.persister.repository;

import com.dna.common.DnaResult;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MongoDnaRepositoryTest {
    @Mock
    MongoClient mongoClient;
    @Mock
    DnaKeyBuilder dnaKeyBuilder;
    @Mock
    MongoDatabase mongoDatabase;
    @Mock
    MongoCollection mongoCollection;
    @Captor
    ArgumentCaptor<Document> documentCaptor;

    @Test
    public void insertDna_human() {
        insertDna("AAATGCGC", false, MongoDnaRepository.HUMAN_COLLECTION);
    }

    @Test
    public void insertDna_mutant() {
        insertDna("AAAAGGGG", true, MongoDnaRepository.MUTANT_COLLECTION);
    }

    private void insertDna(String dna, boolean mutant, String collectionName){
        String dbName = "dna";
        String dnaKey = "dnakey";

        DnaResult dnaResult = DnaResult.builder().mutant(mutant).dna(dna).build();

        Mockito.when(dnaKeyBuilder.buildKey(dna)).thenReturn(dnaKey);
        Mockito.when(mongoClient.getDatabase(dbName)).thenReturn(mongoDatabase);
        Mockito.when(mongoDatabase.getCollection(collectionName)).thenReturn(mongoCollection);

        MongoDnaRepository mongoDnaRepository =
                new MongoDnaRepository(dnaKeyBuilder, mongoClient, dbName);

        mongoDnaRepository.insertDnaResult(dnaResult);

        Mockito.verify(mongoCollection).insertOne(documentCaptor.capture());
        Document document = documentCaptor.getValue();
        Assert.assertNotNull(document);
        Assert.assertEquals(dnaKey, document.get("_id"));
        Assert.assertEquals(dna, document.get("dna"));
        Assert.assertNotNull(document.get("createdAt"));
    }
}
