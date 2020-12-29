package com.trendyol.errorhandler.handlers;

import com.trendyol.errorhandler.model.ApiError;

public interface ErrorHandler {
    boolean canHandle(Exception exception);
    ApiError handle(Exception exception);
    default Integer order(){
        return 0;
    }
}
