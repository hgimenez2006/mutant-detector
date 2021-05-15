package com.dna.analyzer.sender;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dna.analyzer.service.DnaResult;
import com.dna.common.Environment;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

public class DnaSqsSender implements DnaSender {
    private AmazonSQS amazonSQS;
    private String sqsUrl;

    @Inject
    public DnaSqsSender(@Named("aws_region") final String awsRegion,
                        @Named("sqs_url") final String sqsUrl,
                        @Named("s3_bucket") String s3bucket,
                        final AmazonSqsFactory amazonSqsFactory){
        this.sqsUrl = sqsUrl;
        s3bucket = s3bucket;
        this.amazonSQS = amazonSqsFactory.getAmazonSqs(awsRegion, s3bucket);
    }

    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsUrl)
                .withMessageBody(new Gson().toJson(detectionResult));

        amazonSQS.sendMessage(sendMessageRequest);
    }
}
