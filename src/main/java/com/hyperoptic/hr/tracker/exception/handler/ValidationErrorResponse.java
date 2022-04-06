package com.hyperoptic.hr.tracker.exception.handler;

import com.hyperoptic.hr.tracker.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model for validation exception response
 */
public final class ValidationErrorResponse extends ErrorResponse {

    private List<ValidationFieldError> fieldErrors;

    public ValidationErrorResponse() {
        this.code = ErrorCode.VALIDATION_ERROR;
        this.fieldErrors = new ArrayList<>();
    }

    public ValidationErrorResponse(String message, List<ValidationFieldError> fieldErrors) {
        this.code = ErrorCode.VALIDATION_ERROR;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public List<ValidationFieldError> getFieldErrors() {
        return Collections.unmodifiableList(fieldErrors);
    }

}
