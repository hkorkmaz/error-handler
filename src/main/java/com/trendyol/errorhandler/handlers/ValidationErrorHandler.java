package com.trendyol.errorhandler.handlers;

import com.trendyol.errorhandler.MessageHelper;
import com.trendyol.errorhandler.model.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.trendyol.errorhandler.MessageHelper.INVALID_REQUEST_MESSAGE_KEY;

@RequiredArgsConstructor
public class ValidationErrorHandler implements ErrorHandler {
    private final MessageHelper messageHelper;

    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiError handle(Exception exception) {
        var ex = (MethodArgumentNotValidException) exception;
        var errorResponse = ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(messageHelper.getMessage(INVALID_REQUEST_MESSAGE_KEY))
                .build();

        ex.getBindingResult().getFieldErrors()
                .stream()
                .filter(it -> it.getDefaultMessage() != null)
                .forEach(it -> errorResponse.addFieldError(it.getField(), messageHelper.getMessage(it)));

        return errorResponse;
    }
}
