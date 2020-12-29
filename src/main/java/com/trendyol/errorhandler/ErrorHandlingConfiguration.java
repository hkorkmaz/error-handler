package com.trendyol.errorhandler;

import com.trendyol.errorhandler.handlers.ErrorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ComponentScan(basePackages = {"com.trendyol.errorhandler"})
//@ConfigurationProperties(prefix = "error-handler")
public class ErrorHandlingConfiguration {

}
