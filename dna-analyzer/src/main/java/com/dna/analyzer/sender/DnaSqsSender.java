package com.dna.analyzer.sender;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dna.analyzer.service.DnaResult;
import com.dna.common.AwsRegionUtil;
import com.dna.common.Environment;
import com.google.gson.Gson;

public class DnaSqsSender implements DnaSender {
    private final static String DEFAULT_BUCKET_NAME = "sqs-dna";
    private final static String DEFAULT_SQS_URL =
            "https://sqs.us-east-1.amazonaws.com/276662393644/s3_events";

    private final String sqsUrl = Environment.getInstance()
            .get(Environment.Variable.SQS_URL, DEFAULT_SQS_URL);
    private final Regions region = AwsRegionUtil.getAwsRegion();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(region).build();
    private final String bucketName = Environment.getInstance()
            .get(Environment.Variable.S3_BUCKET, DEFAULT_BUCKET_NAME);

    final ExtendedClientConfiguration extendedClientConfig =
            new ExtendedClientConfiguration()
                    .withPayloadSupportEnabled(s3, bucketName, true);

    final AmazonSQS sqsExtended =
            new AmazonSQSExtendedClient(
                    AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build(),
                    extendedClientConfig);

    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsUrl)
                .withMessageBody(new Gson().toJson(detectionResult));

        sqsExtended.sendMessage(sendMessageRequest);
    }
}
