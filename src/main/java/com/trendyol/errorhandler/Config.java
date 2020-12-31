package com.trendyol.errorhandler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "error-handler")
@PropertySource("classpath:/error-handler-defaults.yml")
public class Config {
    @Value("${log-request-headers}")
    private Boolean logRequestHeaders;
    private Boolean enabled;
}
