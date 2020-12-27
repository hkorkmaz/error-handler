package com.trendyol.member.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors = new ArrayList<>();
    @JsonIgnore
    private HttpStatus httpStatus;

    public ApiError() { }

    public ApiError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void addFieldError(String fieldName, String message) {
        this.errors.add(new FieldError(fieldName, message));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

