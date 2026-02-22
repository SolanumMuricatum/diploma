package com.pepino.userservice.config;

import com.pepino.userservice.config.props.S3Props;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Props s3Props;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(s3Props.getEndpoint()))
                .region(Region.of(s3Props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3Props.getAccessKey(), s3Props.getSecretKey())))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public CommandLineRunner initBucket(S3Client s3Client) {
        return args -> {
            try {
                s3Client.headBucket(HeadBucketRequest.builder()
                        .bucket(s3Props.getBucketName())
                        .build());
            } catch (NoSuchBucketException e) {
                s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(s3Props.getBucketName())
                        .build());
                System.out.println("Бакет " + s3Props.getBucketName() + " успешно создан в SeaweedFS");
            } catch (Exception e) {
                System.err.println("Ошибка при проверке/создании бакета: " + e.getMessage());
            }
        };
    }
}
