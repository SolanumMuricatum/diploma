package com.pepino.albumservice.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "s3")
public class S3Props {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String region;
    private String bucketName;
}
