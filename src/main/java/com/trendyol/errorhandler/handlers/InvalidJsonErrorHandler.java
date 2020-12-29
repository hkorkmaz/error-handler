package com.trendyol.errorhandler.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.trendyol.errorhandler.model.ApiError;
import com.trendyol.errorhandler.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import static com.trendyol.errorhandler.MessageHelper.INVALID_FIELD_MESSAGE_KEY;
import static com.trendyol.errorhandler.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;

@Component
@RequiredArgsConstructor
public class InvalidJsonErrorHandler implements ErrorHandler {
    private final MessageHelper messageHelper;

    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof HttpMessageNotReadableException && exception.getCause() instanceof InvalidFormatException;
    }

    @Override
    public ApiError handle(Exception exception) {
        var ex = (InvalidFormatException) exception.getCause();
        var errorResponse = ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY))
                .build();

        ex.getPath().forEach(it -> {
            var messageKey = String.format(INVALID_FIELD_MESSAGE_KEY, it.getFieldName());
            errorResponse.addFieldError(it.getFieldName(), messageHelper.getMessage(messageKey));
        });

        return errorResponse;
    }
}