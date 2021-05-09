package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.io.IOException;

public class AnalyzedDnaSqsHandler implements RequestHandler<SQSEvent, Void> {
    private AnalyzedDnaService analyzedDnaService = new AnalyzedDnaService();

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        System.out.println("requeste received");
        sqsEvent.getRecords().stream().forEach(sqsMessage -> {
            try {
                handleMessage(sqsMessage);
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        });
        return null;
    }

    // TODO: ver si se puede recibir el mensaje transparentemente en lugar de ir a buscarlo a s3
    // https://docs.aws.amazon.com/sns/latest/dg/large-message-payloads.html
    private void handleMessage(SQSEvent.SQSMessage sqsMessage) throws IOException {
        System.out.println("body=" + sqsMessage.getBody());
        analyzedDnaService.processAndSaveMessage(sqsMessage.getBody());
    }

}