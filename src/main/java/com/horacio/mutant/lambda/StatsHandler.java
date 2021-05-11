package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.horacio.mutant.guice.AnalyzedDnaModule;
import com.horacio.mutant.service.AnalyzedDnaService;
import com.horacio.mutant.service.RawDnaService;
import com.horacio.mutant.service.Stats;

public class StatsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Injector injector = Guice.createInjector(new AnalyzedDnaModule());
    private AnalyzedDnaService analyzedDnaService = injector.getInstance(AnalyzedDnaService.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        Stats stats = analyzedDnaService.getStats();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(new Gson().toJson(stats));

        return response;
    }
}
