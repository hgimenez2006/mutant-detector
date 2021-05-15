package com.dna.stats.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import javax.inject.Inject;
import javax.inject.Named;

public class MongoDnaRepository implements DnaRepository{
    private static final String MUTANT_COLLECTION = "mutant";
    private static final String HUMAN_COLLECTION = "human";

    private MongoDatabase mongoDatabase;
    private String dbName;
    private String url;

    @Inject
    public MongoDnaRepository( @Named("mongodb_url") final String url,
                               @Named("mongodb_dbName") final String dbName) {
        this.dbName = dbName;
        this.url = url;
        this.mongoDatabase = connectAndGetDatabase();
    }

    private MongoDatabase connectAndGetDatabase(){
        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        return mongoClient.getDatabase(dbName);
    }

    public long getHumanCount(){
        return getCount(HUMAN_COLLECTION);
    }

    public long getMutantCount(){
        return getCount(MUTANT_COLLECTION);
    }

    private long getCount(String collectionName) {
        long count;
        MongoDatabase mongoDatabase = connectAndGetDatabase();
        try {
            count = mongoDatabase.getCollection(collectionName).countDocuments();
        } catch (IllegalStateException e) {
            // connection was closed
            mongoDatabase = connectAndGetDatabase();
            count = mongoDatabase.getCollection(collectionName).countDocuments();
        }
        return count;
    }
}
