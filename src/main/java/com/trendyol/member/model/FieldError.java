package com.trendyol.member.model;

public class FieldError {

    private String field;
    private String message;

    public FieldError(String message) {
        this.message = message;
    }

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
