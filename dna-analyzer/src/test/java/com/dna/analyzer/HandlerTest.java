package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class HandlerTest {

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
