package com.trendyol.member.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.trendyol.member.model.ApiError;
import com.trendyol.member.MessageHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import static com.trendyol.member.MessageHelper.INVALID_FIELD_MESSAGE_KEY;
import static com.trendyol.member.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;

@Component
public class InvalidJsonErrorHandler implements ErrorHandler {
    private MessageHelper messageHelper;

    public InvalidJsonErrorHandler(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof HttpMessageNotReadableException && exception.getCause() instanceof InvalidFormatException;
    }

    @Override
    public ApiError handle(Exception exception) {
        var ex = (InvalidFormatException) exception.getCause();
        var errorResponse = new ApiError();
        errorResponse.setMessage(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY));
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);

        ex.getPath().forEach(it -> {
            var messageKey = String.format(INVALID_FIELD_MESSAGE_KEY, it.getFieldName());
            errorResponse.addFieldError(it.getFieldName(), messageHelper.getMessage(messageKey));
        });

        return errorResponse;
    }
}
