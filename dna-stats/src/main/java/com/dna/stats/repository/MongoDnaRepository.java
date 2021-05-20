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

    @Inject
    public MongoDnaRepository( @Named("mongodb_url") final String url,
                               @Named("mongodb_dbName") final String dbName) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        this.mongoDatabase = mongoClient.getDatabase(dbName);
    }

    public long getHumanCount(){
        return getCount(HUMAN_COLLECTION);
    }

    public long getMutantCount(){
        return getCount(MUTANT_COLLECTION);
    }

    private long getCount(String collectionName) {
        long count = mongoDatabase.getCollection(collectionName).countDocuments();
        return count;
    }
}
