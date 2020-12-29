package com.trendyol.errorhandler.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.trendyol.errorhandler.MessageHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;

import static com.trendyol.errorhandler.MessageHelper.INVALID_FIELD_MESSAGE_KEY;
import static com.trendyol.errorhandler.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvalidJsonErrorHandlerTest {

    @Mock
    MessageHelper messageHelper;

    @InjectMocks
    InvalidJsonErrorHandler invalidJsonErrorHandler;

    @Test
    void it_should_only_handle_invalid_format_exception() {
        var exception1 = new HttpMessageNotReadableException("", new InvalidFormatException(null, null, null), null);
        var exception2 = new HttpMessageNotReadableException("", new IllegalArgumentException(""), null);

        assertTrue(invalidJsonErrorHandler.canHandle(exception1));
        assertFalse(invalidJsonErrorHandler.canHandle(exception2));
    }

    @Test
    void it_should_handle_invalid_format_exception() {
        var exception = new HttpMessageNotReadableException("", new StubInvalidFormatException(null, null, null, null), null);

        var messageKey1 = String.format(INVALID_FIELD_MESSAGE_KEY, "field1");

        when(messageHelper.getMessage(messageKey1)).thenReturn("field1 message");
        when(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY)).thenReturn("Actual message");

        var result = invalidJsonErrorHandler.handle(exception);

        assertEquals("Actual message", result.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, result.getHttpStatus());
        assertEquals(2, result.getErrors().size());
        assertEquals("field1", result.getErrors().get(0).getField());
        assertEquals("field1 message", result.getErrors().get(0).getMessage());
    }
}

class StubInvalidFormatException extends InvalidFormatException {

    public StubInvalidFormatException(JsonParser p, String msg, Object value, Class<?> targetType) {
        super(p, msg, value, targetType);
    }

    @Override
    public List<Reference> getPath() {
        return List.of(
                new Reference(null, "field1"),
                new Reference(null, "field2")
        );
    }
}