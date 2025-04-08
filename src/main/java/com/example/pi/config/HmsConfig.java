package com.example.pi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hmssdk")
@Getter
@Setter
public class HmsConfig {
    private String appAccessKey;
    private String appSecret;
    private String templateId;
    private String managementKey;
    private String baseDomain;
}