package com.test.imageapplication.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

public class AWSUtil {
    public static final Region AWS_REGION = Region.EU_CENTRAL_1;
    public static final String BUCKET_NAME = "$BucketName";

    public static S3Client getS3Client() {
        var s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build();

        if (!checkIfBucketExist(s3Client)) {
            //TODO add exception
        }
        return s3Client;
    }

    public static RekognitionClient getRekognitionClient() {
        return RekognitionClient.builder()
                .region(AWS_REGION)
                .build();
    }

    private static boolean checkIfBucketExist(S3Client client) {
        var headBucketRequest = HeadBucketRequest.builder()
                .bucket(BUCKET_NAME)
                .build();

        try {
            client.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

}
