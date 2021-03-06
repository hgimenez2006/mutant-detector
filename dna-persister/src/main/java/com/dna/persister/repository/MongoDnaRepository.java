package com.dna.persister.repository;

import com.dna.common.DnaResult;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

public class MongoDnaRepository implements DnaRepository{
    public static final String MUTANT_COLLECTION = "mutant";
    public static final String HUMAN_COLLECTION = "human";

    private final DnaKeyBuilder dnaKeyBuilder;
    private MongoDatabase mongoDatabase;

    @Inject
    public MongoDnaRepository(final DnaKeyBuilder dnaKeyBuilder,
                              @Named("mongodb_url") final String url,
                              @Named("mongodb_dbName") final String dbName) {
        this.dnaKeyBuilder = dnaKeyBuilder;

        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        this.mongoDatabase = mongoClient.getDatabase(dbName);
    }

    // For testing
    public MongoDnaRepository(final DnaKeyBuilder dnaKeyBuilder, final MongoClient mongoClient, final String dbName){
        this.dnaKeyBuilder = dnaKeyBuilder;
        this.mongoDatabase = mongoClient.getDatabase(dbName);
    }

    @Override
    public void insertDnaResult(DnaResult dnaResult) {
        Document document = buildDocument(dnaResult);
        String collectionName = dnaResult.isMutant() ? MUTANT_COLLECTION : HUMAN_COLLECTION;

        try {
            mongoDatabase.getCollection(collectionName).insertOne(document);
        }
        catch (com.mongodb.MongoWriteException e) {
            // check if the error is because duplicated dna or not
            if (ErrorCategory.fromErrorCode(e.getCode()) != ErrorCategory.DUPLICATE_KEY) {
                throw e;
            }
        }
    }

    protected Document buildDocument(DnaResult dnaResult) {
        String id = dnaKeyBuilder.buildKey(dnaResult.getDna());

        Document document = new Document();
        document.append("_id", id);
        document.append("createdAt", new Date());
        document.append("dna", dnaResult.getDna());

        return document;
    }
}
