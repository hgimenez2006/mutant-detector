package com.dna.analyzer.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.common.DnaResult;
import org.apache.http.HttpStatus;

public class DnaResponseAdapter {
    private static final String MUTANT_DNA = "AND mutante";
    private static final String HUMAN_DNA = "AND humano";
    private DnaResult dnaResult;

    public DnaResponseAdapter(DnaResult dnaResult) {
        this.dnaResult = dnaResult;
    }

    public APIGatewayProxyResponseEvent getApiGatewayProxyResponse(){
        int statusCode = dnaResult.isMutant() ? HttpStatus.SC_OK : HttpStatus.SC_FORBIDDEN;
        String body = dnaResult.isMutant() ? MUTANT_DNA : HUMAN_DNA;

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(body);

        return response;
    }
}
