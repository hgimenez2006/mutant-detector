package com.dna.persister;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.dna.persister.service.DnaPersisterService;
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
    DnaPersisterService dnaPersisterService;
    @Mock
    Context context;

    @Test
    public void handleRequest() throws IOException {
        PersisterHandler persisterHandler = new PersisterHandler(dnaPersisterService);
        String msgBody = "xxx";
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody(msgBody);

        SQSEvent sqsEvent = new SQSEvent();
        sqsEvent.setRecords(Arrays.asList(sqsMessage));

        persisterHandler.handleRequest(sqsEvent, context);
        Mockito.verify(dnaPersisterService).processAndSaveMessage(msgBody);
    }
}
