package com.dna.persister;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.dna.persister.service.DnaPersisterService;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {
    @Mock
    Context context;
    @Mock
    LambdaLogger lambdaLogger;

    @Test
    public void handleRequest() throws IOException {
        SQSEvent sqsEvent = new SQSEvent();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsEvent.setRecords(Arrays.asList(sqsMessage));

        Handler handler = Mockito.spy(Handler.class);
        Mockito.doNothing().when(handler).handleMessage(sqsMessage);
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);

        handler.handleRequest(sqsEvent, context);
        Mockito.verify(handler).handleMessage(sqsMessage);
    }


}
