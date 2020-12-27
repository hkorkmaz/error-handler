package com.trendyol.member.handlers;

import com.trendyol.member.model.ApiError;

public interface ErrorHandler {
    boolean canHandle(Exception exception);
    ApiError handle(Exception exception);
    default Integer order(){
        return 0;
    }
}
