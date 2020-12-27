package com.trendyol.member;

import com.trendyol.member.handlers.ErrorHandler;
import com.trendyol.member.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice(basePackageClasses = RestController.class)
@Order(0)
public class ErrorHandlingAdvice {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingAdvice.class);
    private static final String REMOTE_IP_HEADER = "X-Forwarded-For";
    private final List<ErrorHandler> errorHandlers;

    public ErrorHandlingAdvice(List<ErrorHandler> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(Exception ex, HttpServletRequest request) {
        logExceptions(ex, request);

        var errorResponse = new ApiError();
        var sortedHandlers = errorHandlers.stream()
                .sorted(Comparator.comparing(ErrorHandler::order))
                .collect(Collectors.toList());

        for (ErrorHandler handler : sortedHandlers) {
            if (handler.canHandle(ex)) {
                errorResponse = handler.handle(ex);
                break;
            }
        }

        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    private void logExceptions(Throwable ex, HttpServletRequest request) {
        log.error(getExtraInformation(request) + "\nException cause by: ", ex);
    }

    private String getExtraInformation(HttpServletRequest request) {
        var remoteIpAddress = Optional.ofNullable(request.getHeader(REMOTE_IP_HEADER)).orElse(request.getRemoteAddr());
        var path = request.getRequestURI();
        var headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(headerName -> headerName, request::getHeader));

        var builder = new StringBuilder();
        builder.append("*** Request Extra Information ***").append("\n");
        builder.append("Remote ip address => ").append(remoteIpAddress).append("\n");
        builder.append("Request path => ").append(path).append("\n");
        builder.append("--- Headers ---").append("\n");
        headers.forEach((key, value) -> builder.append(key).append(":").append(value).append("\n"));

        return builder.toString();
    }
}
