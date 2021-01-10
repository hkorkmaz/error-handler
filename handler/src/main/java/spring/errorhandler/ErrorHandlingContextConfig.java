package spring.errorhandler;

import spring.errorhandler.handlers.DefaultErrorHandler;
import spring.errorhandler.handlers.ErrorHandler;
import spring.errorhandler.handlers.InvalidJsonErrorHandler;
import spring.errorhandler.handlers.ValidationErrorHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import(Config.class)
public class ErrorHandlingContextConfig {

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
