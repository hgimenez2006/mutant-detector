package com.dna.persister.sqs;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ExtendedSqsMessageTest {

    @Test
    public void ofMessage() {
        String s3Bucket = "sqs-dna";
        String s3Key = "8b79b963-244f-4056-910f-ad6c06bca726";
        String sqsMessageBody = String.format("\"software.amazon.payloadoffloading.PayloadS3Pointer\", " +
                "{\"s3BucketName\":\"%s\",\"s3Key\":\"%s\"}]", s3Bucket, s3Key);

        ExtendedSqsMessage message = ExtendedSqsMessage.of(sqsMessageBody);
        assertNotNull(message);
        assertEquals(s3Key, message.getS3Key());
        assertEquals(s3Bucket, message.getS3BucketName());
    }

}
