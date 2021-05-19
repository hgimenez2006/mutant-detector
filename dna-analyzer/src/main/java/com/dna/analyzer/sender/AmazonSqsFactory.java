package com.dna.analyzer.sender;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * Factory for AmazonSqs extended client
 */
public class AmazonSqsFactory {
    public AmazonSQS getAmazonSqs(final String region, final String s3bucket) {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region).build();

        ExtendedClientConfiguration extendedClientConfig =
                new ExtendedClientConfiguration()
                        .withPayloadSupportEnabled(s3, s3bucket, true);

        AmazonSQS sqsExtended =
                new AmazonSQSExtendedClient(
                        AmazonSQSClientBuilder.standard()
                                .withRegion(Regions.fromName(region))
                                .build(),
                        extendedClientConfig);

        return sqsExtended;
    }
}
