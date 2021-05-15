package com.dna.persister.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class S3Repository {

    private final AmazonS3 s3;

    @Inject
    public S3Repository(@Named("aws_region") final String awsRegion) {
        //Regions region = AwsRegionUtil.getAwsRegion();

        s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion)).build();
                //.withRegion(Regions.US_EAST_1).build();
    }

    /*public void uploadFile(String bucketName, String dnaKey, String dna) {
        try {
            s3.putObject(bucketName, dnaKey, dna);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }*/

    public String getFileContent(String s3BucketName, String s3Key) throws IOException {
        S3Object s3Object = s3.getObject(s3BucketName, s3Key);
        String text = IOUtils.toString(s3Object.getObjectContent(), StandardCharsets.UTF_8.name());
        return text;
    }

    public void deleteFile(String s3BucketName, String s3Key) {
        s3.deleteObject(s3BucketName, s3Key);
    }
}
