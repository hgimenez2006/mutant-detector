package com.horacio.mutant.repository;

import com.horacio.mutant.Environment;
import com.horacio.mutant.service.DnaResult;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

public class MongoDnaRepository implements DnaRepository{
    // String url = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private MongoDatabase mongoDatabase;
    private final DnaKeyBuilder dnaKeyBuilder = new DnaKeyBuilderSHA256();

    public MongoDnaRepository() {
        String url = Environment.getInstance().get(Environment.Variable.DB_URL,
                "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority");
        String dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, "dna");

        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        //mongoDatabase = mongoClient.getDatabase("dna");
        mongoDatabase = mongoClient.getDatabase(dbName);
    }

    @Override
    public void insertDnaResult(DnaResult dnaResult){
        String id = dnaKeyBuilder.buildId(dnaResult.getDna());

        Document document = new Document();
        document.append("_id", id);
        document.append("createdAt", new Date());
        document.append("dna", dnaResult.getDna());

        try {
            if (dnaResult.isMutant()) {
                mongoDatabase.getCollection("mutant").insertOne(document);
            } else {
                mongoDatabase.getCollection("human").insertOne(document);
            }
        }catch(DuplicateKeyException e){
            // Do nothing
        }
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
