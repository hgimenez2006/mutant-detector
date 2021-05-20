package com.dna.integration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.dna.analyzer.AnalyzerHandler;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.module.DnaAnalyzerModule;
import com.dna.analyzer.web.DnaRequestAdapter;
import com.dna.persister.module.DnaPersisterModule;
import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.MongoDnaRepository;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test the entire flow from dna-analyzer to dna-persister (bypassing mocked sqs), creating
 * final records in MongoDB
 * The use of Mockito is just for mocking the context and the lambda logger
 */
@RunWith(MockitoJUnitRunner.class)
public class MutantIntegrationTest {
    @Mock
    Context context;

    Injector injector;
    DnaKeyBuilder dnaKeyBuilder;
    MongoDatabase mongoDatabase;

    @Before
    public void init(){
        injector = Guice.createInjector(new DnaAnalyzerModule());
        dnaKeyBuilder = injector.getInstance(DnaKeyBuilder.class);

        MongoProperties mongoProperties = injector.getInstance(MongoProperties.class);
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoProperties.getMongodbUrl()));
        mongoDatabase = mongoClient.getDatabase(mongoProperties.getMongodbName());
    }

    @Test
    public void mutant_event() throws InvalidDnaException {
        String eventFile = "mutant_event.json";
        processEvent(eventFile, MongoDnaRepository.MUTANT_COLLECTION, HttpStatus.SC_OK);
    }

    @Test
    public void human_event() throws InvalidDnaException {
        String eventFile = "human_event.json";
        processEvent(eventFile, MongoDnaRepository.HUMAN_COLLECTION, HttpStatus.SC_FORBIDDEN);
    }

    private void processEvent(String eventFile, String collectionName, int httpStatus) throws InvalidDnaException {
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent(eventFile);
        DnaRequestAdapter dnaRequestAdapter = new DnaRequestAdapter(event.getBody());

        String dna = StringUtils.join(dnaRequestAdapter.getDnaRequest().getDna());
        String dnaKey = dnaKeyBuilder.buildKey(dna);

        // First delete the dna from the db if exists
        MongoCollection mongoCollection = mongoDatabase.getCollection(collectionName);
        Document myDocument = new Document("_id", dnaKey);
        mongoCollection.deleteOne(myDocument);

        // Handle the event
        AnalyzerHandler handler = new AnalyzerHandler();
        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);

        assertEquals(httpStatus, response.getStatusCode().longValue());

        // Check the dna was persisted
        FindIterable<Document> documents = mongoCollection.find(myDocument);
        Document documentFound = documents.first();
        Assert.assertNotNull(documentFound);

        mongoCollection.deleteOne(documentFound);
    }
}
