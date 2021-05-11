package com.horacio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.horacio.mutant.guice.AnalyzedDnaModule;
import com.horacio.mutant.guice.RawDnaModule;
import com.horacio.mutant.service.AnalyzedDnaService;
import com.horacio.mutant.service.RawDnaService;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

public class MyTest {

    @Test
    public void testModules(){
        Injector injector = Guice.createInjector(new AnalyzedDnaModule());
        AnalyzedDnaService analyzedDnaService = injector.getInstance(AnalyzedDnaService.class);

        injector = Guice.createInjector(new RawDnaModule());
        RawDnaService rawDnaService = injector.getInstance(RawDnaService.class);
    }
    //@Test
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

    //@Test
    public void doit(){
       // new S3Repository().uploadFile("mutant-bucket","kaos", "kimba");
    }
}
