package com.dna.persister.repository;

import com.dna.persister.service.DnaResult;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

public class MongoDnaRepository implements DnaRepository{
    private static final String MUTANT_COLLECTION = "mutant";
    private static final String HUMAN_COLLECTION = "human";

    private final DnaKeyBuilder dnaKeyBuilder;
    private String url;
    private String dbName;
    private MongoDatabase mongoDatabase;

    @Inject
    public MongoDnaRepository(final DnaKeyBuilder dnaKeyBuilder,
                              @Named("mongodb_url") final String url,
                              @Named("mongodb_dbName") final String dbName) {
        this.dnaKeyBuilder = dnaKeyBuilder;
        this.url = url;
        this.dbName = dbName;
        this.mongoDatabase = connectAndGetDatabase();
    }

    private MongoDatabase connectAndGetDatabase(){
        // TODO: try this
        //MongoClientOptions.builder().maxConnectionIdleTime() // miliseconds
        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        return mongoClient.getDatabase(dbName);
    }

    @Override
    public void insertDnaResult(DnaResult dnaResult) {
        Document document = buildDocument(dnaResult);
        String collectionName = dnaResult.isMutant() ? MUTANT_COLLECTION : HUMAN_COLLECTION;

        try {
            insertDocument(mongoDatabase, collectionName, document);
        } catch (IllegalStateException e) {
            // connection was closed
            mongoDatabase = connectAndGetDatabase();
            insertDocument(mongoDatabase, collectionName, document);
        } catch (com.mongodb.MongoWriteException e) {
            if (ErrorCategory.fromErrorCode(e.getCode()) != ErrorCategory.DUPLICATE_KEY) {
                // TODO: we could search for the doc anyway, just to be 100% percent sure
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

    private void insertDocument(MongoDatabase mongoDatabase, String collectionName, Document document){
        mongoDatabase.getCollection(collectionName).insertOne(document);
    }
}
