package com.trendyol.member.handlers;

import com.trendyol.member.ErrorResponse;
import com.trendyol.member.MessageHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static com.trendyol.member.MessageHelper.UNKNOWN_ERROR_MESSAGE_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultErrorHandlerTest {

    @Mock
    MessageHelper messageHelper;

    @InjectMocks
    DefaultErrorHandler defaultErrorHandler;

    @Test
    void it_should_handle_any_exception(){
        assertTrue(defaultErrorHandler.canHandle(new RuntimeException()));
    }

    @Test
    void it_should_handle_and_set_metadata(){
        when(messageHelper.getMessage("message_key")).thenReturn("Actual message");

        var result = defaultErrorHandler.handle(new TestException());

        assertEquals("123", result.getCode());
        assertEquals("Actual message", result.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getHttpStatus());
    }

    @Test
    void it_should_handle_and_set_default_metadata(){
        when(messageHelper.getMessage(UNKNOWN_ERROR_MESSAGE_KEY)).thenReturn("Default message");

        var result = defaultErrorHandler.handle(new TestExceptionWithoutMetadata());

        assertNull(result.getCode());
        assertEquals("Default message", result.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getHttpStatus());
    }
}

@ErrorResponse(code = "123", message = "message_key", httpStatus = 401)
class TestException extends RuntimeException {}

class TestExceptionWithoutMetadata extends RuntimeException {}