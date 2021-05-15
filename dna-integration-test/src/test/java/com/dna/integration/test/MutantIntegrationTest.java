package com.dna.integration.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.dna.analyzer.AnalyzerHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.beans.EventHandler;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class MutantIntegrationTest {
    @Mock
    Context context;

    @Test
    public void testLoadEventBridgeEvent() throws IOException {
        // Given
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent("apigw_rest_event.json");
        //EventHandler<ScheduledEvent, String> handler = new EventHandler<>();
        AnalyzerHandler handler = new AnalyzerHandler();

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);
        System.out.println(response.getBody());
        // Then
        //assertThat(response).isEqualTo("something");
    }
}
