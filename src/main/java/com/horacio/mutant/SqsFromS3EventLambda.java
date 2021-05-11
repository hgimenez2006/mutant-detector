package com.horacio.mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.horacio.mutant.repository.MongoDnaRepository;
import com.horacio.mutant.repository.DnaKeyBuilderSHA256;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Lambda invoked from sqs
 *
 * client --> mutant-dagger --> s3 --> sqs --> SqsLambda --> mongoDB
 *
 */
//@Log4j2
@Deprecated
public class SqsFromS3EventLambda implements RequestHandler<SQSEvent, Void> {
    private MongoDnaRepository mongoDnaRepository;

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        sqsEvent.getRecords().stream().forEach(sqsMessage -> {
            handleMessage(sqsMessage);
        });
        return null;
    }

    @Data
    private class BodyMessage{
        ArrayList<RecordMessage> Records;
    }

    @Data
    private class RecordMessage{
        S3Message s3;
    }

    @Data
    private class S3Message{
        S3MessageObject object;
        BucketMessage bucket;
    }

    @Data
    private class BucketMessage{
        String name;
    }

    @Data
    private class S3MessageObject{
        String key;
    }

    private void handleMessage(SQSEvent.SQSMessage sqsMessage){
        System.out.println("body=" + sqsMessage.getBody());

        BodyMessage bodyMessage = new Gson().fromJson(sqsMessage.getBody(), BodyMessage.class);
        if (bodyMessage==null){
            System.out.println("bodyMessage is null");
            return;
        }
        List<RecordMessage> recordMessages = bodyMessage.getRecords();
        S3Message s3Message = recordMessages.get(0).getS3();
        String srcKey = s3Message.getObject().getKey();
        String srcBucket = s3Message.getBucket().getName();

        System.out.println("key=" + s3Message.getObject().getKey());
        System.out.println("bucket=" + srcBucket);

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                    srcBucket, srcKey));
            InputStream objectData = s3Object.getObjectContent();

            String dna = IOUtils.toString(objectData);
            DnaKeyBuilderSHA256 dnaKeyBuilderSHA256 = new DnaKeyBuilderSHA256();
            String id = dnaKeyBuilderSHA256.buildId(dna);

            if (srcBucket.equals("mutant-bucket")){
                System.out.println("is mutant");
                //mongoRepository.insertMutant(id, dna);
            }
            else{
                System.out.println("is human");
                //mongoRepository.insertHuman(id, dna);
            }

            System.out.println("deleting object");
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(srcBucket, srcKey);
            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("object deleted");

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
