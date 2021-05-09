package com.horacio;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.google.gson.Gson;
import com.horacio.mutant.lambda.AnalyzedDnaService;
import com.horacio.mutant.lambda.S3Message;
import com.horacio.mutant.lambda.SqsMessageProcessor;
import com.horacio.mutant.service.DnaResult;
import org.junit.Test;

import java.io.IOException;

//TODO: mark this as integration test and exclude running when packaging
public class AnalyzedDnaSqsReceiverTest {
    private final String url = "https://sqs.us-east-1.amazonaws.com/276662393644/s3_events";

    private final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            //TODO: review this
            .withRegion(Regions.US_EAST_1)
            .build();
    private final String queueUrl = sqs.getQueueUrl("s3_events").getQueueUrl();
    private final int delaySeconds = 5;

    /*
    mensaje desde s3
    * ["software.amazon.payloadoffloading.PayloadS3Pointer",{"s3BucketName":"sqs-dna","s3Key":"8b79b963-244f-4056-910f-ad6c06bca726"}]

     * */
   // @Test
    public void testReceiver(){
        ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(url);

        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
        //sqs.deleteMessage();

        AnalyzedDnaService analyzedDnaService = new AnalyzedDnaService();
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
