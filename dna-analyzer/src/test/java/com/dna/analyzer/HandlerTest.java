package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {
    @Mock
    Context context;
    @Mock
    LambdaLogger lambdaLogger;

    @Test
    public void handleRequest_badRequest(){
        APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
        Handler handler = new Handler();
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
        APIGatewayProxyResponseEvent response = handler.handleRequest(apiGatewayProxyRequestEvent, context);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void handleRequest_ok(){
        APIGatewayProxyRequestEvent requestEvent = EventLoader.loadApiGatewayRestEvent("apigw_rest_event.json");
        Handler handler = new Handler();
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
        APIGatewayProxyResponseEvent response = handler.handleRequest(requestEvent, context);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }

    @Test
    public void getResponse_Mutant(){
        DnaResult dnaResult = new DnaResult(true, "AAAABBBBCFDAFRTE");
        Handler handler = new Handler();
        APIGatewayProxyResponseEvent response = handler.getResponse(dnaResult);
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    public void getResponse_Human(){
        DnaResult dnaResult = new DnaResult(false, "ALAABCBBCFDAFRTE");
        Handler handler = new Handler();
        APIGatewayProxyResponseEvent response = handler.getResponse(dnaResult);
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void handleRequest_emptyBody(){
        Handler handler = new Handler();
        Assert.assertThrows(InvalidDnaException.class, () -> {
            handler.handleRequestPlease("");
        });
    }

    @Test
    public void handleRequest_malformedJson(){
        Handler handler = new Handler();
        Assert.assertThrows(InvalidDnaException.class, () -> {
            handler.handleRequestPlease("xx");
        });
    }

    @Test
    public void handleRequest_invalidJson(){
        Handler handler = new Handler();
        Assert.assertThrows(InvalidDnaException.class, () -> {
            handler.handleRequestPlease("{}");
        });
    }
}
