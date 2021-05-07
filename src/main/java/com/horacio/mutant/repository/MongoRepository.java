package com.horacio.mutant.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoRepository {
    private MongoDatabase mongoDatabase;
    //private static MongoRepository instance;

    /*public static MongoRepository getInstance(){
        if (instance == null){
            instance = new MongoRepository();
        }
        return instance;
    }*/

    public MongoRepository() {
        String url = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";

        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        mongoDatabase = mongoClient.getDatabase("dna");
    }


    public void insertMutant(String id, String dna){
        Document document = new Document();
        document.append("_id", id);
        document.append("dna", dna);
        mongoDatabase.getCollection("mutant").insertOne(document);
    }

    public void insertHuman(String id, String dna){
        Document document = new Document();
        document.append("_id", id);
        document.append("dna", dna);
        mongoDatabase.getCollection("human").insertOne(document);
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
