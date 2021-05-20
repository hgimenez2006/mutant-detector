package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.module.DnaAnalyzerModule;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaService;
import com.dna.analyzer.web.DnaRequestAdapter;
import com.dna.analyzer.web.DnaResponseAdapter;
import com.dna.common.DnaResult;
import com.google.inject.Guice;
import org.apache.http.HttpStatus;

public class AnalyzerHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private DnaService dnaService;

    public AnalyzerHandler(){
        dnaService = Guice.createInjector(new DnaAnalyzerModule())
                .getInstance(DnaService.class);
    }

    // For testing
    public AnalyzerHandler(DnaService dnaService){
        this.dnaService = dnaService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        try{
            DnaRequestAdapter dnaRequestAdapter = new DnaRequestAdapter(request.getBody());
            DnaResult dnaResult = dnaService.analyzeDnaAndSendResult(dnaRequestAdapter.getDnaRequest().getDna());

            DnaResponseAdapter dnaResponseAdapter = new DnaResponseAdapter(dnaResult);
            return  dnaResponseAdapter.getApiGatewayProxyResponse();

        } catch (InvalidDnaException e) {
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withBody(e.getMessage())
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST);
            return response;
        }
    }
}
