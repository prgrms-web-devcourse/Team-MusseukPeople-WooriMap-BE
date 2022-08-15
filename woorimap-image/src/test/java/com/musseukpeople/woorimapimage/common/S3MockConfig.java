package com.musseukpeople.woorimapimage.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import io.findify.s3mock.S3Mock;

@TestConfiguration
public class S3MockConfig {

    private final String region;
    private final String bucket;

    public S3MockConfig(
        @Value("${cloud.aws.region.static}") String region,
        @Value("${cloud.aws.s3.bucket}") String bucket
    ) {
        this.region = region;
        this.bucket = bucket;
    }

    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

    @Bean
    @Primary
    public AmazonS3Client amazonS3Client(S3Mock s3Mock) {
        s3Mock.start();
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:8001", region);
        AmazonS3Client client = (AmazonS3Client)AmazonS3ClientBuilder
            .standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(endpoint)
            .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
            .build();
        client.createBucket(bucket);

        return client;
    }
}
