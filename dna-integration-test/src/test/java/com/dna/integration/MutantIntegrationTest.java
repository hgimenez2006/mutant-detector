package com.dna.integration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.dna.analyzer.AnalyzerHandler;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @Mock
    LambdaLogger lambdaLogger;

    @Before
    public void init(){
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
    }

    @Test
    public void mutant_event(){
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent("mutant_event.json");
        AnalyzerHandler handler = new AnalyzerHandler();
        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode().longValue());
    }

    @Test
    public void human_event(){
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent("human_event.json");
        AnalyzerHandler handler = new AnalyzerHandler();
        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode().longValue());
    }
}
