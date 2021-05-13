package com.dna.analyzer.sender;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.dna.common.AwsRegionUtil;
import com.dna.common.Environment;

public class AmazonSqsFactory {
    private final static String DEFAULT_BUCKET_NAME = "sqs-dna";

    private final Regions region = AwsRegionUtil.getAwsRegion();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(region).build();
    private final String bucketName = Environment.getInstance()
            .get(Environment.Variable.S3_BUCKET, DEFAULT_BUCKET_NAME);

    public AmazonSQS getAmazonSqs() {
        ExtendedClientConfiguration extendedClientConfig =
                new ExtendedClientConfiguration()
                        .withPayloadSupportEnabled(s3, bucketName, true);

        AmazonSQS sqsExtended =
                new AmazonSQSExtendedClient(
                        AmazonSQSClientBuilder.standard()
                                .withRegion(region)
                                .build(),
                        extendedClientConfig);

        return sqsExtended;
    }
}
