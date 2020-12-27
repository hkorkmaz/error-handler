package com.trendyol.member.handlers;

import com.trendyol.member.model.ApiError;
import com.trendyol.member.ErrorResponse;
import com.trendyol.member.MessageHelper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.trendyol.member.MessageHelper.UNKNOWN_ERROR_MESSAGE_KEY;

@Component
public class DefaultErrorHandler implements ErrorHandler {
    private MessageHelper messageHelper;

    public DefaultErrorHandler(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Override
    public boolean canHandle(Exception exception) {
        return true;
    }

    @Override
    public Integer order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ApiError handle(Exception exception) {
        var errorMetadata = AnnotationUtils.getAnnotation(exception.getClass(), ErrorResponse.class);
        var error = new ApiError();

        if (errorMetadata != null) {
            error.setCode(errorMetadata.code());
            error.setMessage(messageHelper.getMessage(errorMetadata.message()));
            error.setHttpStatus(HttpStatus.resolve(errorMetadata.httpStatus()));
        } else {
            error.setMessage(messageHelper.getMessage(UNKNOWN_ERROR_MESSAGE_KEY));
            error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return error;
    }
}
