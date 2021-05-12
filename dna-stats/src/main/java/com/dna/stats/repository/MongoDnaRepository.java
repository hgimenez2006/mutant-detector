package com.dna.stats.repository;

import com.dna.common.Environment;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import javax.inject.Inject;

public class MongoDnaRepository implements DnaRepository{
    private static String DEFAULT_URL = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private static final String DEFAULT_DB_NAME = "dna";
    private static final String MUTANT_COLLECTION = "mutant";
    private static final String HUMAN_COLLECTION = "human";

    private String dbName;
    private MongoClientURI uri;

    @Inject
    public MongoDnaRepository() {
        String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
        this.dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);
        this.uri = new MongoClientURI(url);
    }

    public long getHumanCount(){
        return getCount(HUMAN_COLLECTION);
    }

    public long getMutantCount(){
        return getCount(MUTANT_COLLECTION);
    }

    private long getCount(String collectionName) {
        long count;
        MongoDatabase mongoDatabase = getMongoDatabase();
        try {
            count = mongoDatabase.getCollection(collectionName).countDocuments();
        } catch (IllegalStateException e) {
            // connection was closed
            mongoDatabase = getMongoDatabase();
            count = mongoDatabase.getCollection(collectionName).countDocuments();
        }
        return count;
    }

    private MongoDatabase getMongoDatabase(){
        // TODO: try this
        //MongoClientOptions.builder().maxConnectionIdleTime() // miliseconds
        MongoClient mongoClient = new MongoClient(uri);
        return mongoClient.getDatabase(dbName);
    }
}
