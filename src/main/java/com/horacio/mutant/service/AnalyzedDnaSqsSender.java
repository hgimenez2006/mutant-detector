package com.horacio.mutant.service;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import com.horacio.mutant.EnvrironmentVariables;

public class AnalyzedDnaSqsSender implements AnalyzedDnaSender {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

    final ExtendedClientConfiguration extendedClientConfig =
            new ExtendedClientConfiguration()
                    .withPayloadSupportEnabled(s3, EnvrironmentVariables.S3_BUCKET_NAME, true);
                    //Message threshold controls the maximum message size that will be allowed to be published
                    //through SNS using the extended client. Payload of messages exceeding this value will be stored in
                    //S3. The default value of this parameter is 256 KB which is the maximum message size in SNS (and SQS).
                    //.withPayloadSizeThreshold();
                    //.withLargePayloadSupportEnabled(s3, S3_BUCKET_NAME);

    final AmazonSQS sqsExtended =
            new AmazonSQSExtendedClient(
                    AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build(),
                    extendedClientConfig);


    //TODO: review this
    private final String url = "https://sqs.us-east-1.amazonaws.com/276662393644/s3_events";

    /*private final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            //TODO: review this
            .withRegion(Regions.US_EAST_1)
            .build();*/
    //private final String queueUrl = sqs.getQueueUrl("s3_events").getQueueUrl();
    private final int delaySeconds = 5;

    @Override
    public void sendAnalyzedDna(DnaResult detectionResult) {
        System.out.println("Invoking ulr=" + url);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(url)
                //.withQueueUrl(queueUrl)
                .withMessageBody(new Gson().toJson(detectionResult))
                .withDelaySeconds(delaySeconds);

        sqsExtended.sendMessage(sendMessageRequest);
    }
}
