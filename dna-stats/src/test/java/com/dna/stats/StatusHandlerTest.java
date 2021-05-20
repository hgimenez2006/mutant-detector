package com.dna.stats;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.stats.dto.Stats;
import com.dna.stats.service.StatsService;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatusHandlerTest {
    @Mock
    StatsService statsService;
    @Mock
    Context context;
    @Mock
    APIGatewayProxyRequestEvent request;

    @Test
    public void handleRequest(){
        Stats stats = Stats.builder().count_human_dna(100).count_mutant_dna(40).build();
        Mockito.when(statsService.getStats()).thenReturn(stats);
        StatsHandler statsHandler = new StatsHandler(statsService);

        APIGatewayProxyResponseEvent response = statsHandler.handleRequest(request, context);

        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(new Gson().toJson(stats), response.getBody());
    }
}
