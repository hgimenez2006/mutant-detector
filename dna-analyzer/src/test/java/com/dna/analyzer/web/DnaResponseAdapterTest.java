package com.dna.analyzer.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.AnalyzerHandler;
import com.dna.analyzer.service.DnaResult;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DnaResponseAdapterTest {
    @Test
    public void getResponse_Mutant(){
        DnaResult dnaResult = new DnaResult(true, "AAAABBBBCFDAFRTE");
        DnaResponseAdapter dnaResponseAdapter = new DnaResponseAdapter(dnaResult);
        APIGatewayProxyResponseEvent response = dnaResponseAdapter.getApiGatewayProxyResponse();
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    public void getResponse_Human(){
        DnaResult dnaResult = new DnaResult(false, "ALAABCBBCFDAFRTE");
        DnaResponseAdapter dnaResponseAdapter = new DnaResponseAdapter(dnaResult);
        APIGatewayProxyResponseEvent response = dnaResponseAdapter.getApiGatewayProxyResponse();
        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode());
    }
}
