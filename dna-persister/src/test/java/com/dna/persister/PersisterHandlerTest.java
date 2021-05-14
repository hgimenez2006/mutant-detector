package com.dna.persister;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class PersisterHandlerTest {
    @Mock
    Context context;
    @Mock
    LambdaLogger lambdaLogger;

    @Test
    public void handleRequest() throws IOException {
        SQSEvent sqsEvent = new SQSEvent();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsEvent.setRecords(Arrays.asList(sqsMessage));

        PersisterHandler persisterHandler = Mockito.spy(PersisterHandler.class);
        Mockito.doNothing().when(persisterHandler).handleMessage(sqsMessage);
        Mockito.when(context.getLogger()).thenReturn(lambdaLogger);

        persisterHandler.handleRequest(sqsEvent, context);
        Mockito.verify(persisterHandler).handleMessage(sqsMessage);
    }


}
