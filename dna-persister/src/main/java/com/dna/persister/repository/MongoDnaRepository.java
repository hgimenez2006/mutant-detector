package com.dna.persister.repository;

import com.dna.persister.Environment;
import com.dna.persister.service.DnaResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Date;

public class MongoDnaRepository implements DnaRepository{
    private static String DEFAULT_URL = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private static final String DEFAULT_DB_NAME = "dna";

    private MongoDatabase mongoDatabase;
    private final DnaKeyBuilder dnaKeyBuilder; // = new DnaKeyBuilderSHA256();

    @Inject
    public MongoDnaRepository(final DnaKeyBuilder dnaKeyBuilder) {
        this.dnaKeyBuilder = dnaKeyBuilder;
        String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
        String dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);

        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
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
            //TODO: check this
        //}catch(DuplicateKeyException e){
        }catch(Exception e){
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
