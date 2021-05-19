package com.dna.persister.service;

import com.dna.common.DnaResult;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.s3.S3Repository;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import javax.inject.Inject;
import java.io.IOException;

public class DnaPersisterService {
    private final static String SQS_MSG_BUCKET_NAME = "s3BucketName";

    private final DnaRepository dnaRepository;
    private final S3Repository s3Repository;

    @Inject
    public DnaPersisterService(final DnaRepository dnaRepository,
                               final S3Repository s3Repository){
        this.dnaRepository = dnaRepository;
        this.s3Repository = s3Repository;
    }

    public void processAndSaveMessage(String sqsMessageBody) throws IOException {
        DnaResult dnaResult = getDnaResult(sqsMessageBody);
        dnaRepository.insertDnaResult(dnaResult);
    }

    private DnaResult getDnaResult(String msgBody) throws IOException {
        String dnaResultJson;
        if (msgBody.contains(SQS_MSG_BUCKET_NAME)){
            // get dna result from s3 file
            dnaResultJson = retrieveContentFromS3(msgBody);
        }
        else{
            dnaResultJson = msgBody;
        }
        DnaResult dnaResult = new Gson().fromJson(dnaResultJson, DnaResult.class);
        return dnaResult;
    }

    private String retrieveContentFromS3(String msgBody) throws IOException {
        String s3MessageJson = msgBody.substring(msgBody.indexOf(SQS_MSG_BUCKET_NAME)-2,
                msgBody.length()-1);
        S3Message s3Message = new Gson().fromJson(s3MessageJson, S3Message.class);

        String s3Content = s3Repository.getFileContent(s3Message.getS3BucketName(), s3Message.getS3Key());
        return s3Content;
    }

    @Data
    @ToString
    private class S3Message {
        private String s3BucketName;
        private String s3Key;
    }
}
