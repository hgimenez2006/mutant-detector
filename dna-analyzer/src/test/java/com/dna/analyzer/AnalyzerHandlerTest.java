package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaService;
import com.dna.common.DnaResult;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzerHandlerTest {
    @Mock
    Context context;
    @Mock
    DnaService dnaService;

    @Test
    public void handleRequest_badRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

        AnalyzerHandler analyzerHandler = new AnalyzerHandler(dnaService);
        APIGatewayProxyResponseEvent response = analyzerHandler.handleRequest(request, context);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void handleRequest_ok() throws InvalidDnaException {
        APIGatewayProxyRequestEvent request = EventLoader.loadApiGatewayRestEvent("request_event.json");

        DnaResult dnaResult = DnaResult.builder().mutant(true).dna("AAAA").build();
        Mockito.when(dnaService.analyzeDnaAndSendResult(Matchers.anyObject())).thenReturn(dnaResult);
        AnalyzerHandler analyzerHandler = new AnalyzerHandler(dnaService);
        APIGatewayProxyResponseEvent response = analyzerHandler.handleRequest(request, context);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }
}
