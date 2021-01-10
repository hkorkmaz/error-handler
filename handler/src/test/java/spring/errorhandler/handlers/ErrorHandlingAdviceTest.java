package spring.errorhandler.handlers;

import spring.errorhandler.Config;
import spring.errorhandler.ErrorHandlingAdvice;
import spring.errorhandler.model.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingAdviceTest {
    @Mock
    DefaultErrorHandler defaultErrorHandler;

    @Mock
    HttpServletRequest request;

    @Mock
    Config config;

    ErrorHandlingAdvice errorHandlingAdvice;

    @BeforeEach
    void init() {
        when(config.getLogRequestHeaders()).thenReturn(true);
        when(request.getRequestURI()).thenReturn("");
        when(request.getRemoteAddr()).thenReturn("");
        when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());

        errorHandlingAdvice = new ErrorHandlingAdvice(List.of(defaultErrorHandler), config);
    }

    @Test
    void it_should_handle_validation_errors() {
        var exception = new RuntimeException();
        var apiError = ApiError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();

        when(defaultErrorHandler.handle(exception)).thenReturn(apiError);
        when(defaultErrorHandler.canHandle(exception)).thenReturn(true);

        var result = errorHandlingAdvice.handle(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
