package com.dna.persister.service;

import com.dna.common.DnaResult;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.s3.S3Repository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class DnaPersisterServiceTest {
    @Mock
    DnaRepository dnaRepository;
    @Mock
    S3Repository s3Repository;
    @InjectMocks
    DnaPersisterService dnaPersisterService;

    @Test
    public void processAndSaveMessage() throws IOException {
        DnaResult dnaResult = DnaResult.builder().dna("AAAA").mutant(false).build();
        String sqsMessageBody = new Gson().toJson(dnaResult);
        dnaPersisterService.processAndSaveMessage(sqsMessageBody);
        Mockito.verify(dnaRepository).insertDnaResult(dnaResult);
    }

    @Test
    public void processAndSaveMessage_contentFromS3() throws IOException {
        //sqs with s3 message example:
        String s3Bucket = "sqs-dna";
        String s3Key = "8b79b963-244f-4056-910f-ad6c06bca726";
        String sqsMessageBody = String.format("\"software.amazon.payloadoffloading.PayloadS3Pointer\", " +
                "{\"s3BucketName\":\"%s\",\"s3Key\":\"%s\"}]", s3Bucket, s3Key);

        DnaResult dnaResult = DnaResult.builder().dna("AAAA").mutant(false).build();
        Mockito.when(s3Repository.getFileContent(s3Bucket, s3Key))
                .thenReturn(new Gson().toJson(dnaResult));
        dnaPersisterService.processAndSaveMessage(sqsMessageBody);
        Mockito.verify(dnaRepository).insertDnaResult(dnaResult);
    }
}
