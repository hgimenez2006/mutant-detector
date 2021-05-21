package com.dna.persister.sqs;

import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

/**
 * Represents the content of the sqs message received when the dna result was saved in S3 bucket
 */
@Data
@ToString
public class ExtendedSqsMessage {
    private final static String S3_BUCKET_NAME = "s3BucketName";

    private String s3BucketName;
    private String s3Key;

    private ExtendedSqsMessage(){}

    public static ExtendedSqsMessage of(String msgBody){
        // The extended sqs message format is:
        // [software.amazon.payloadoffloading.PayloadS3Pointer, {"s3BucketName": "<bucketName>", "s3Key": "<key>"}]

        try {
            // excuse my french
            String s3MessageJson = msgBody.substring(msgBody.indexOf(S3_BUCKET_NAME) - 2, msgBody.length() - 1);
            return new Gson().fromJson(s3MessageJson, ExtendedSqsMessage.class);
        }catch(Exception e){
            throw new RuntimeException("Error parsing extended sqs message [msgBody=" + msgBody + "]", e);
        }
    }

    public static boolean isExtendedSqsMessage(String msgBody){
        return msgBody.contains(S3_BUCKET_NAME);
    }
}
