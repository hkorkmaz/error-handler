package com.trendyol.member.handlers;

import com.trendyol.member.MessageHelper;
import com.trendyol.member.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.trendyol.member.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;

@Component
public class ValidationErrorHandler implements ErrorHandler {
    private MessageHelper messageHelper;

    public ValidationErrorHandler(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiError handle(Exception exception) {
        var ex = (MethodArgumentNotValidException) exception;
        var errorResponse = new ApiError();
        errorResponse.setMessage(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY));
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);

        ex.getBindingResult().getFieldErrors()
                .stream()
                .filter(it -> it.getDefaultMessage() != null)
                .forEach(it -> errorResponse.addFieldError(it.getField(), messageHelper.getMessage(it)));

        return errorResponse;
    }
}
