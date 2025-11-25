package com.pepino.albumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.nio.file.Paths;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AlbumServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumServiceApplication.class, args);

/*        AwsBasicCredentials creds = AwsBasicCredentials.create(
                "pepino",
                "b7723e9c1f911897f3e73a35c7b4c1de6e0f6e8c358e42267eee1b0cadce6901"
        );

        S3Client s3 = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:8333"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .forcePathStyle(true)
                .build();

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket("sharephoto")
                        .key("avatars/1001.jpg")
                        .build(),
                Paths.get("D:/photo/banya.jpg")
        );


        System.out.println("Uploaded!");*/
    }
}

