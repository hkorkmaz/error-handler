package com.trendyol.errorhandler.handlers;

import com.trendyol.errorhandler.ErrorBody;
import com.trendyol.errorhandler.ErrorResponse;
import com.trendyol.errorhandler.MessageHelper;
import com.trendyol.errorhandler.model.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.trendyol.errorhandler.MessageHelper.UNKNOWN_ERROR_MESSAGE_KEY;

@Slf4j
@RequiredArgsConstructor
public class DefaultErrorHandler implements ErrorHandler {
    private final MessageHelper messageHelper;

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
        var errorBuilder = ApiError.builder();

        if (errorMetadata != null) {
            errorBuilder.code(errorMetadata.code())
                    .httpStatus(HttpStatus.resolve(errorMetadata.httpStatus()))
                    .customBody(getCustomBody(exception))
                    .message(messageHelper.getMessage(errorMetadata.message()));
        } else {
            errorBuilder
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(messageHelper.getMessage(UNKNOWN_ERROR_MESSAGE_KEY));
        }
        return errorBuilder.build();
    }

    private Object getCustomBody(Exception exception) {
        return Arrays.stream(exception.getClass().getMethods())
                .filter(field -> field.isAnnotationPresent(ErrorBody.class))
                .findFirst()
                .map(method -> executeMethod(method, exception))
                .orElse(null);
    }

    private Object executeMethod(Method method, Exception exception) {
        if (method.getReturnType() != Void.TYPE && method.getParameterCount() == 0) {
            try {
                method.setAccessible(true);
                return method.invoke(exception);
            } catch (Exception e) {
                log.error(String.format("Unable to use method result of method %s.%s", exception.getClass().getName(), method.getName()));
            }
        }
        return null;
    }
}
