package com.horacio.mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.horacio.mutant.service.DnaService;
import com.horacio.mutant.service.Stats;
import com.horacio.mutant.web.MutantRequest;
import com.horacio.mutant.web.MutantResponse;

import javax.inject.Inject;

public class StatsLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private DnaService dnaService = new DnaService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);

        Stats stats  = dnaService.getStats();
        response.setBody(new Gson().toJson(stats));
        return response;
    }
}
