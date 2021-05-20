package com.dna.persister;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.dna.persister.module.DnaPersisterModule;
import com.dna.persister.service.DnaPersisterService;
import com.google.inject.Guice;

import java.io.IOException;

public class PersisterHandler implements RequestHandler<SQSEvent, Void> {
    private DnaPersisterService dnaPersisterService;

    public PersisterHandler(){
        dnaPersisterService = Guice.createInjector(new DnaPersisterModule())
                .getInstance(DnaPersisterService.class);
    }

    // For testing
    public PersisterHandler(DnaPersisterService dnaPersisterService){
        this.dnaPersisterService = dnaPersisterService;
    }

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {

        sqsEvent.getRecords().stream().forEach(sqsMessage -> {
            try {
                dnaPersisterService.processAndSaveMessage(sqsMessage.getBody());
            } catch (IOException e) {
                // rollbacking the entire batch here. a better approach would be sending back to
                // the queue only the message that thrown the error
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}