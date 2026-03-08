package com.pepino.userservice.config;

import com.pepino.userservice.config.props.AwsSesProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@RequiredArgsConstructor
public class AwsSesConfig {
    @Bean
    public SesV2Client sesV2Client(final AwsSesProps awsConfigProps) {
        if (awsConfigProps.getAccessKey() != null && !awsConfigProps.getAccessKey().isEmpty()) {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                    awsConfigProps.getAccessKey(),
                    awsConfigProps.getSecretKey()
            );
            return SesV2Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .region(Region.of(awsConfigProps.getRegion()))
                    .build();
        } else {
            return SesV2Client.builder()
                    .credentialsProvider(DefaultCredentialsProvider.builder().build())
                    .region(Region.of(awsConfigProps.getRegion()))
                    .build();
        }
    }
}
