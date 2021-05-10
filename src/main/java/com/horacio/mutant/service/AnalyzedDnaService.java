package com.horacio.mutant.service;

import com.google.gson.Gson;
import com.horacio.mutant.lambda.AnalyzedDnaSqsHandler;
import com.horacio.mutant.repository.DnaRepository;
import com.horacio.mutant.s3.S3Repository;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;

public class AnalyzedDnaService {
    private final DnaRepository dnaRepository;
    private final S3Repository s3Repository;

    public AnalyzedDnaService(final DnaRepository dnaRepository,
                              final S3Repository s3Repository){
        this.dnaRepository = dnaRepository;
        this.s3Repository = s3Repository;
    }

    public void processAndSaveMessage(String sqsMessageBody) throws IOException {
        //TODO: borrar el archivo de s3 a manopla
        DnaResult dnaResult = getDnaResult(sqsMessageBody);
        dnaRepository.insertDnaResult(dnaResult);
    }

    private DnaResult getDnaResult(String msgBody) throws IOException {
        String dnaResultJson;
        if (msgBody.contains(AnalyzedDnaSqsHandler.SQS_MSG_BUCKET_NAME)){
            // get dna result from s3 file
            dnaResultJson = retrieveContent(msgBody);
        }
        else{
            dnaResultJson = msgBody;
        }
        DnaResult dnaResult = new Gson().fromJson(dnaResultJson, DnaResult.class);
        return dnaResult;
    }

    private String retrieveContent(String msgBody) throws IOException {
        int start = msgBody.indexOf(AnalyzedDnaSqsHandler.SQS_MSG_BUCKET_NAME)-2;
        int end = msgBody.length()-1;
        String s3MessageJson = msgBody.substring(start, end);
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
