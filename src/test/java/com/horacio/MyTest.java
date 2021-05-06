package com.horacio;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

import java.util.Arrays;

public class MyTest {
    @Test
    public void go(){
        MongoCredential credential = MongoCredential.createCredential("horacio", "dna",
                "mustantes2000".toCharArray());

        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();

        String url = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
        /*MongoClient mongoClient = new MongoClient(new ServerAddress("cluster0.7pfzt.mongodb.net", 27017),
                                           Arrays.asList(credential),
                                           options);*/

        //MongoClientURI uri = new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1&ssl=true");
        MongoClientURI uri = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("dna");

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("mutant");
        System.out.println(mongoCollection.count());

        mongoCollection = mongoDatabase.getCollection("human");
        System.out.println(mongoCollection.count());
    }
}
