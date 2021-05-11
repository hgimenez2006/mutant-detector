package com.dna.persister.service;

import com.dna.persister.handler.AnalyzedDnaSqsHandler;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.s3.S3Repository;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import javax.inject.Inject;
import java.io.IOException;

public class AnalyzedDnaService {
    private final DnaRepository dnaRepository;
    private final S3Repository s3Repository;

    @Inject
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

   /*public Stats getStats(){
        long humanCount = dnaRepository.getHumanCount();
        long mutantCount = dnaRepository.getMutantCount();
        float ratio = mutantCount;
        if (humanCount > 0){
           ratio = (float)mutantCount/humanCount;
        }

        Stats stats = Stats.builder()
                .count_human_dna(humanCount)
                .count_mutant_dna(mutantCount)
                .ratio(ratio)
                .build();

        return stats;
    }*/

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
