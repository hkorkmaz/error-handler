package com.trendyol.errorhandler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    @JsonInclude(Include.NON_EMPTY)
    private String code;
    private String message;
    @JsonInclude(Include.NON_EMPTY)
    private List<FieldError> errors;
    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonInclude(Include.NON_NULL)
    private Object customBody;

    public void addFieldError(String fieldName, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new FieldError(fieldName, message));
    }
}

