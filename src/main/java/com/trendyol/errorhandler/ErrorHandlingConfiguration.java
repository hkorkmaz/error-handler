package com.trendyol.errorhandler;

import com.trendyol.errorhandler.handlers.DefaultErrorHandler;
import com.trendyol.errorhandler.handlers.ErrorHandler;
import com.trendyol.errorhandler.handlers.InvalidJsonErrorHandler;
import com.trendyol.errorhandler.handlers.ValidationErrorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(Config.class)
public class ErrorHandlingConfiguration {

    @Bean
    public ErrorHandlingAdvice errorHandlingAdvice(List<ErrorHandler> errorHandlers, Config config){
        return new ErrorHandlingAdvice(errorHandlers, config);
    }

    @Bean
    public List<ErrorHandler> errorHandlers(MessageHelper messageHelper){
        return List.of(
                new DefaultErrorHandler(messageHelper),
                new InvalidJsonErrorHandler(messageHelper),
                new ValidationErrorHandler(messageHelper)
        );
    }

    @Bean
    public MessageHelper messageHelper(MessageSource messageSource){
        return new MessageHelper(messageSource);
    }
}
