package com.trendyol.errorhandler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "error-handler")
public class Config {
    private Boolean logRequestHeaders;
    private Boolean enabled;
}
