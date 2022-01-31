package com.services.user.management.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix="app")
@Configuration
@Getter
@Setter
public class AppProperties {
    private String subscriptionManagementServiceUrl;
}
