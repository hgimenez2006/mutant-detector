package com.horacio.mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.horacio.mutant.repository.MongoRepository;
import com.horacio.mutant.service.DnaIdBuilderSHA256;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class SqsLambda implements RequestHandler<S3Event, String> {
    private MongoRepository mongoRepository = new MongoRepository();

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        log.info("processing s3 event");
        S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);

        String srcBucket = record.getS3().getBucket().getName();
        log.info("bucket=" + srcBucket);
        // Object key may have spaces or unicode non-ASCII characters.
        String srcKey = record.getS3().getObject().getUrlDecodedKey();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                    srcBucket, srcKey));
            InputStream objectData = s3Object.getObjectContent();

            String dna = IOUtils.toString(objectData);
            DnaIdBuilderSHA256 dnaIdBuilderSHA256 = new DnaIdBuilderSHA256();
            String id = dnaIdBuilderSHA256.buildId(dna);

            if (srcBucket.equals("mutant")){
                mongoRepository.insertMutant(id, dna);
            }
            else{
                mongoRepository.insertHuman(id, dna);
            }

            log.info("deleting object");
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(srcBucket, srcKey);
            s3Client.deleteObject(deleteObjectRequest);
            log.info("object deleted");

        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }
}
