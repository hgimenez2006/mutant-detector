package com.dna.persister.service;

import com.dna.common.DnaResult;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.s3.S3Repository;
import com.dna.persister.sqs.ExtendedSqsMessage;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;

import javax.inject.Inject;
import java.io.IOException;

public class DnaPersisterService {
    private final DnaRepository dnaRepository;
    private final S3Repository s3Repository;

    @Inject
    public DnaPersisterService(final DnaRepository dnaRepository,
                               final S3Repository s3Repository){
        this.dnaRepository = dnaRepository;
        this.s3Repository = s3Repository;
    }

    /**
     * Process the sqs message and persis it, extracting the dna result from the message itself or from an S3 bucket
     * (for dna too larges the dna result is stored in S3).
     */
    public void processAndSaveMessage(String sqsMessageBody) throws IOException {
        DnaMessage dnaMessage = getDnaMessage(sqsMessageBody);
        DnaResult dnaResult = new Gson().fromJson(dnaMessage.getDnaResultAsJson(), DnaResult.class);
        dnaRepository.insertDnaResult(dnaResult);

        if (dnaMessage.isFromS3()){
            removeFileFromS3(dnaMessage.getExtendedSqsMessage());
        }
    }

    private void removeFileFromS3(ExtendedSqsMessage extendedSqsMessage){
        try{
            s3Repository.deleteFile(extendedSqsMessage.getS3BucketName(), extendedSqsMessage.getS3Key());
        }catch(Exception e){
            // dont want to rollback the entire batch for this
            System.out.println("Error deleting file from S3: " + e.getMessage());
        }
    }

    private DnaMessage getDnaMessage(String msgBody) throws IOException {
        if (ExtendedSqsMessage.isExtendedSqsMessage(msgBody)){
            ExtendedSqsMessage extendedSqsMessage = ExtendedSqsMessage.of(msgBody);
            String dnaResultAsJson = s3Repository.getFileContent(
                    extendedSqsMessage.getS3BucketName(),
                    extendedSqsMessage.getS3Key());

            return DnaMessage.builder()
                    .extendedSqsMessage(extendedSqsMessage)
                    .dnaResultAsJson(dnaResultAsJson)
                    .build();
        }

        return DnaMessage.builder().dnaResultAsJson(msgBody).build();
    }

    @Data
    @Builder
    private static class DnaMessage {
        ExtendedSqsMessage extendedSqsMessage;
        String dnaResultAsJson;

        boolean isFromS3(){
            return extendedSqsMessage != null;
        }
    }
}
