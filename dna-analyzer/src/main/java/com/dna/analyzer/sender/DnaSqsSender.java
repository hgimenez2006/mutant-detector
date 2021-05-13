package com.dna.analyzer.sender;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dna.analyzer.service.DnaResult;
import com.dna.common.Environment;
import com.google.gson.Gson;

import javax.inject.Inject;

public class DnaSqsSender implements DnaSender {
    private final String sqsUrl = Environment.getInstance()
            .get(Environment.Variable.SQS_URL,
                    "https://sqs.us-east-1.amazonaws.com/276662393644/s3_events");

    private AmazonSQS amazonSQS;

    @Inject
    public DnaSqsSender(AmazonSqsFactory amazonSqsFactory){
        amazonSQS = amazonSqsFactory.getAmazonSqs();
    }

    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsUrl)
                .withMessageBody(new Gson().toJson(detectionResult));

        amazonSQS.sendMessage(sendMessageRequest);
    }
}
