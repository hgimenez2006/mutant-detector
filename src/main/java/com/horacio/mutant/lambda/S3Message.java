package com.horacio.mutant.lambda;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class S3Message {
    private String s3BucketName;
    private String s3Key;
}
