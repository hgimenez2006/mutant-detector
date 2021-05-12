package com.dna.persister.module;

import com.dna.common.Environment;
import com.mongodb.BasicDBObject;
import com.mongodb.ClientSessionOptions;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteError;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

public class MongoDdIntegraionTest {
    private static String DEFAULT_URL = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private static final String DEFAULT_DB_NAME = "dna";

    String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
    String dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);

    //@Test
    public void test() {
        String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
        String dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);

        odit();
        odit();
/*        MongoCredential credential = MongoCredential.createCredential("horacio", "dna",
                "mutantes2000".toCharArray());
        MongoClientOptions options = MongoClientOptions.builder()
                .sslEnabled(true)
                .maxConnectionIdleTime(1)
                .build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("7pfzt.mongodb.net", 27017),
                credential,
                options);

        //ClientSessionOptions clientSessionOptions = ClientSessionOptions.builder().
        ClientSessionOptions clientSessionOptions = ClientSessionOptions.builder().build();
        ClientSession session = mongoClient.startSession();*/
    }

    //@Test
    public void insertDuplicate(){
        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);

        MongoCollection mongoCollection = mongoDatabase.getCollection("mutant");
        Document document = new Document();
        document.append("_id", System.currentTimeMillis());
        document.append("createdAt", new Date());
        document.append("dna", "XX");

        mongoCollection.insertOne(document);
        try {
            mongoCollection.insertOne(document);
        }catch(com.mongodb.MongoWriteException e){
            if (ErrorCategory.fromErrorCode(e.getCode()) != ErrorCategory.DUPLICATE_KEY){
                // to be 100% percent sure, check if the object does really not exists
                BasicDBObject query = new BasicDBObject();
                query.put("_id", new ObjectId("id"));
                mongoCollection.find(query);

                throw e;
            }
        }

        mongoClient.close();

    }

    //@Test
    public void testClosingAndOpenningConnection(){
        odit();
    }

    private void odit(){
        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        mongoDatabase = mongoClient.getDatabase(dbName);

        MongoCollection mongoCollection = mongoDatabase.getCollection("mutant");
        // aca abre la coneccion
        long start = System.currentTimeMillis();
        long count = mongoCollection.countDocuments();
        long end = System.currentTimeMillis();
        System.out.println("time: " + (end-start) + " ms");

        mongoClient.close();

        start = System.currentTimeMillis();
        try{
            count = mongoCollection.countDocuments();
        }catch(java.lang.IllegalStateException e){
            mongoClient = new MongoClient(uri);
            mongoDatabase = mongoClient.getDatabase(dbName);
            mongoCollection = mongoDatabase.getCollection("mutant");
            count = mongoCollection.countDocuments();
        }
        end = System.currentTimeMillis();
        System.out.println("time 2: " + (end-start) + " ms");

        mongoClient.close();

    }
}
