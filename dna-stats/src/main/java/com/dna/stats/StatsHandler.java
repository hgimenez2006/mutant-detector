package com.dna.stats;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.stats.module.StatsDnaModule;
import com.dna.stats.dto.Stats;
import com.dna.stats.service.StatsService;
import com.google.gson.Gson;
import com.google.inject.Guice;
import org.apache.http.HttpStatus;

public class StatsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private StatsService statsService;

    public StatsHandler(){
        statsService = Guice.createInjector(new StatsDnaModule()).getInstance(StatsService.class);
    }

    // For testing
    public StatsHandler(StatsService statsService){
        this.statsService = statsService;
    }


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Stats stats = statsService.getStats();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatus.SC_OK)
                .withBody(new Gson().toJson(stats));

        return response;
    }
}
