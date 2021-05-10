package com.horacio.integration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.horacio.mutant.repository.MongoDnaRepository;
import com.horacio.mutant.s3.S3Repository;
import com.horacio.mutant.service.AnalyzedDnaService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

//TODO: mark this as integration test and exclude running when packaging
public class AnalyzedDnaSqsReceiverTest extends IntegrationTestBase{
    private final String url = "https://sqs.us-east-1.amazonaws.com/276662393644/s3_events";

    private final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();

    private S3Repository s3Repository;
    private AnalyzedDnaService analyzedDnaService;
    private MongoDnaRepository mongoDnaRepository;

    @Before
    public void init() {
        mongoDnaRepository = new MongoDnaRepository();
        s3Repository = new S3Repository();
        analyzedDnaService = new AnalyzedDnaService(mongoDnaRepository, s3Repository);
    }

    /*
    mensaje desde s3
    * ["software.amazon.payloadoffloading.PayloadS3Pointer",{"s3BucketName":"sqs-dna","s3Key":"8b79b963-244f-4056-910f-ad6c06bca726"}]

     * */
    @Test
    public void testReceiver(){
        ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(url);

        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
        //sqs.deleteMessage();

        receiveMessageResult.getMessages().stream()
                .forEach(message -> {
                    try {
                        //DnaResult dnaResult = new SqsMessageProcessor().getDnaResult(message.getBody());
                        analyzedDnaService.processAndSaveMessage(message.getBody());
                        //System.out.println(dnaResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
