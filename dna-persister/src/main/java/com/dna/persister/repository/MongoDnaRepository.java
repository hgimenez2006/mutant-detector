package com.dna.persister.repository;

import com.dna.common.Environment;
import com.dna.persister.service.DnaResult;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Date;

public class MongoDnaRepository implements DnaRepository{
    private static String DEFAULT_URL = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
    private static final String DEFAULT_DB_NAME = "dna";
    private static final String MUTANT_COLLECTION = "mutant";
    private static final String HUMAN_COLLECTION = "human";

    private final DnaKeyBuilder dnaKeyBuilder; // = new DnaKeyBuilderSHA256();

    //@Named("${mongodb.url}")
    private String defaultMongoDbUrl;
    //@Named("mongodb.dbName")
    private String defaultMongoDbName;
    private String dbName;
    private MongoClientURI uri;

    @Inject
    public MongoDnaRepository(final DnaKeyBuilder dnaKeyBuilder) {
        this.dnaKeyBuilder = dnaKeyBuilder;
        this.dbName = Environment.getInstance().get(Environment.Variable.DB_NAME, DEFAULT_DB_NAME);
        String url = Environment.getInstance().get(Environment.Variable.DB_URL, DEFAULT_URL);
        this.uri = new MongoClientURI(url);
        //createNewDatabaseConnection(uri, dbName);
    }

    private MongoDatabase getMongoDatabase(){
        // TODO: try this
        //MongoClientOptions.builder().maxConnectionIdleTime() // miliseconds
        MongoClient mongoClient = new MongoClient(uri);
        return mongoClient.getDatabase(dbName);
    }

    @Override
    public void insertDnaResult(DnaResult dnaResult) {
        MongoDatabase mongoDatabase = getMongoDatabase();
        Document document = buildDocument(dnaResult);
        String collectionName = dnaResult.isMutant() ? MUTANT_COLLECTION : HUMAN_COLLECTION;

        try {
            insertDocument(mongoDatabase, collectionName, document);
            //TODO: check this to catch only duplicate key exceptions
        } catch (IllegalStateException e) {
            // connection was closed
            mongoDatabase = getMongoDatabase();
            insertDocument(mongoDatabase, collectionName, document);
        } catch (com.mongodb.MongoWriteException e) {
            if (ErrorCategory.fromErrorCode(e.getCode()) != ErrorCategory.DUPLICATE_KEY) {
                // TODO: we could search for the doc anyway, just to be 100% percent sure
                throw e;
            }
        }
    }

    protected Document buildDocument(DnaResult dnaResult) {
        String id = dnaKeyBuilder.buildKey(dnaResult.getDna());

        Document document = new Document();
        document.append("_id", id);
        document.append("createdAt", new Date());
        document.append("dna", dnaResult.getDna());

        return document;
    }

    private void insertDocument(MongoDatabase mongoDatabase, String collectionName, Document document){
        mongoDatabase.getCollection(collectionName).insertOne(document);
    }
}
