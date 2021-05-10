package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.horacio.mutant.guice.AnalyzedDnaModule;
import com.horacio.mutant.service.AnalyzedDnaService;

import java.io.IOException;

public class AnalyzedDnaSqsHandler implements RequestHandler<SQSEvent, Void> {
    public final static String SQS_MSG_BUCKET_NAME = "s3BucketName";

    private Injector injector = Guice.createInjector(new AnalyzedDnaModule());
    private AnalyzedDnaService analyzedDnaService = injector.getInstance(AnalyzedDnaService.class);

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("ENVIRONMENT VARIABLES: " + new Gson().toJson(System.getenv()));

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