package spring.errorhandler.handlers;

import spring.errorhandler.model.ApiError;

public interface ErrorHandler {
    boolean canHandle(Exception exception);
    ApiError handle(Exception exception);
    default Integer order(){
        return 0;
    }
}
