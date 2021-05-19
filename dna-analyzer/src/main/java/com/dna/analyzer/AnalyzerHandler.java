package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.module.DnaAnalyzerModule;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaService;
import com.dna.analyzer.web.DnaRequestAdapter;
import com.dna.analyzer.web.DnaResponseAdapter;
import com.dna.common.DnaResult;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.http.HttpStatus;

public class AnalyzerHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Injector injector = Guice.createInjector(new DnaAnalyzerModule());
    private DnaService dnaService = injector.getInstance(DnaService.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Environment: " + new Gson().toJson(System.getenv()));

        try{
            DnaRequestAdapter dnaRequestAdapter = new DnaRequestAdapter(request.getBody());
            DnaResult dnaResult = dnaService.analyzeDnaAndSendResult(dnaRequestAdapter.getDnaRequest().getDna());

            DnaResponseAdapter dnaResponseAdapter = new DnaResponseAdapter(dnaResult);
            return  dnaResponseAdapter.getApiGatewayProxyResponse();

        } catch (InvalidDnaException e) {
            logger.log("Returing BAD _REQUEST :" + e.getMessage());
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withBody(e.getMessage())
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST);
            return response;
        }
    }
}
