package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.horacio.mutant.repository.DnaRepository;
import com.horacio.mutant.repository.MongoDnaRepository;
import com.horacio.mutant.s3.S3Repository;
import com.horacio.mutant.service.AnalyzedDnaService;

import java.io.IOException;

public class AnalyzedDnaSqsHandler implements RequestHandler<SQSEvent, Void> {
    public final static String SQS_MSG_BUCKET_NAME = "s3BucketName";

    private DnaRepository dnaRepository = new MongoDnaRepository();
    private S3Repository s3Repository = new S3Repository();
    private AnalyzedDnaService analyzedDnaService = new AnalyzedDnaService(dnaRepository, s3Repository);

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