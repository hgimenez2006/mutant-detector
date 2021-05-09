package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.horacio.mutant.s3.S3Repository;
import com.horacio.mutant.service.DnaResult;

import java.io.IOException;

public class SqsMessageProcessor {
    private S3Repository s3Repository = new S3Repository();

    public DnaResult getDnaResult(String msgBody) throws IOException {
        String bucketName = "s3BucketName";
        if (msgBody.contains(bucketName)){
            int start = msgBody.indexOf("s3BucketName")-2;
            int end = msgBody.length()-1;
            String s3MessageJson = msgBody.substring(start, end);
            S3Message s3Message = new Gson().fromJson(s3MessageJson, S3Message.class);

            String s3Content = s3Repository.getFileContent(s3Message.getS3BucketName(), s3Message.getS3Key());
            DnaResult dnaResult = new Gson().fromJson(s3Content, DnaResult.class);
            return dnaResult;
        }
        else{
            DnaResult dnaResult = new Gson().fromJson(msgBody, DnaResult.class);
            return dnaResult;
        }
    }
}
