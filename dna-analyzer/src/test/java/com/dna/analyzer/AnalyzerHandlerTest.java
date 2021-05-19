package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzerHandlerTest {
    @Mock
    Context context;
    @Mock
    LambdaLogger lambdaLogger;

    @Test
    public void handleRequest_badRequest(){
        APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
        AnalyzerHandler analyzerHandler = new AnalyzerHandler();
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
        APIGatewayProxyResponseEvent response = analyzerHandler.handleRequest(apiGatewayProxyRequestEvent, context);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void handleRequest_ok(){
        APIGatewayProxyRequestEvent requestEvent = EventLoader.loadApiGatewayRestEvent("request_event.json");
        AnalyzerHandler analyzerHandler = new AnalyzerHandler();
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
        APIGatewayProxyResponseEvent response = analyzerHandler.handleRequest(requestEvent, context);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }
}
