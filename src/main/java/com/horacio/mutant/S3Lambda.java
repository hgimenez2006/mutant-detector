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
import com.horacio.mutant.repository.MongoDnaRepository;
import com.horacio.mutant.repository.DnaKeyBuilderSHA256;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;

/**
 * Lambda invoked from s3 bucket
 */
@Deprecated
public class S3Lambda implements RequestHandler<S3Event, String> {
    private MongoDnaRepository mongoDnaRepository = new MongoDnaRepository(new DnaKeyBuilderSHA256());

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);

        String srcBucket = record.getS3().getBucket().getName();
        // Object key may have spaces or unicode non-ASCII characters.
        String srcKey = record.getS3().getObject().getUrlDecodedKey();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                    srcBucket, srcKey));
            InputStream objectData = s3Object.getObjectContent();

            String dna = IOUtils.toString(objectData);
            DnaKeyBuilderSHA256 dnaKeyBuilderSHA256 = new DnaKeyBuilderSHA256();
            String id = dnaKeyBuilderSHA256.buildId(dna);

            if (srcBucket.equals("mutant")){
               // mongoRepository.insertMutant(id, dna);
            }
            else{
              //  mongoRepository.insertHuman(id, dna);
            }

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(srcBucket, srcKey);
            s3Client.deleteObject(deleteObjectRequest);

        } catch (IOException e) {
        }
        return null;
    }
}
