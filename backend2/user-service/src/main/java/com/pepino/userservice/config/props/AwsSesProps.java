package com.pepino.userservice.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.ses")
public class AwsSesProps {
    private String accessKey;
    private String secretKey;
    private String region;
    private String senderAddress;
}
