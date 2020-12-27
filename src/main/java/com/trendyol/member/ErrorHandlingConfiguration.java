package com.trendyol.member;

import com.trendyol.member.handlers.ErrorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ComponentScan(basePackages = "com.trendyol.member.handlers")
public class ErrorHandlingConfiguration {

    @Bean
    public ErrorHandlingAdvice errorHandlingControllerAdvice(List<ErrorHandler> handlers) {
        return new ErrorHandlingAdvice(handlers);
    }

    @Bean
    public MessageHelper messageHelper(MessageSource messageSource) {
        return new MessageHelper(messageSource);
    }
}
