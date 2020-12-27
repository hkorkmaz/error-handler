package com.trendyol.member.handlers;

import com.trendyol.member.MessageHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.trendyol.member.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationErrorHandlerTest {

    @Mock
    MessageHelper messageHelper;

    @InjectMocks
    ValidationErrorHandler validationErrorHandler;

    @Test
    void it_should_only_handle_validation_exception() {
        var exception1 = new MethodArgumentNotValidException(null, null);
        var exception2 = new HttpMessageNotReadableException("", new IllegalArgumentException(""), null);

        assertTrue(validationErrorHandler.canHandle(exception1));
        assertFalse(validationErrorHandler.canHandle(exception2));
    }

    @Test
    void it_should_handle_invalid_format_exception() {
        var exception = new MethodArgumentNotValidException(null, new StubValidationResult(""));

        when(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY)).thenReturn("Actual message");
        when(messageHelper.getMessage(any(FieldError.class))).thenReturn("Validation message");

        var result = validationErrorHandler.handle(exception);

        assertEquals("Actual message", result.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, result.getHttpStatus());
        assertEquals(2, result.getErrors().size());
        assertEquals("field1", result.getErrors().get(0).getField());
        assertEquals("field2", result.getErrors().get(1).getField());
        assertEquals("Validation message", result.getErrors().get(0).getMessage());
    }
}

class StubValidationResult extends AbstractBindingResult {
    protected StubValidationResult(String objectName) {
        super(objectName);
    }

    @Override
    public List<FieldError> getFieldErrors() {
        return List.of(new FieldError("", "field1", ""),
                new FieldError("", "field2", ""));
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    protected Object getActualFieldValue(String s) {
        return null;
    }
}