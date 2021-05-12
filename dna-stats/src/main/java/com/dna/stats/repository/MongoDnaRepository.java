package com.dna.stats.repository;

import com.dna.common.Environment;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import javax.inject.Inject;

public class MongoDnaRepository implements DnaRepository{
    private static String DEFAULT_URL = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private static final String DEFAULT_DB_NAME = "dna";

    private MongoDatabase mongoDatabase;

    @Inject
    public MongoDnaRepository() {
        String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
        String dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);

        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        mongoDatabase = mongoClient.getDatabase(dbName);
    }

    public long getHumanCount(){
        return mongoDatabase.getCollection("human").countDocuments();
    }

    public long getMutantCount(){
        return mongoDatabase.getCollection("mutant").countDocuments();
    }

    public MongoDatabase getDatabase(){
        return mongoDatabase;
    }
}
