package com.trendyol.errorhandler;

import com.trendyol.errorhandler.handlers.ErrorHandler;
import com.trendyol.errorhandler.model.ApiError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
@Order(0)
public class ErrorHandlingAdvice {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingAdvice.class);
    private static final String REMOTE_IP_HEADER = "X-Forwarded-For";
    private final List<ErrorHandler> errorHandlers;
    private final Config config;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request) {
        logExceptions(ex, request);

        var errorResponse = ApiError.builder().build();
        var sortedHandlers = errorHandlers.stream()
                .sorted(Comparator.comparing(ErrorHandler::order))
                .collect(Collectors.toList());

        for (ErrorHandler handler : sortedHandlers) {
            if (handler.canHandle(ex)) {
                errorResponse = handler.handle(ex);
                break;
            }
        }

        var body = Objects.requireNonNullElse(errorResponse.getCustomBody(), errorResponse);
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(body);
    }

    private void logExceptions(Throwable ex, HttpServletRequest request) {
        var headers = "";
        if(config.getLogRequestHeaders()) {
            headers = getExtraInformation(request);
        }
        log.error(headers + "\nException cause by: ", ex);
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
