package com.hyperoptic.hr.tracker.exception.handler;

import com.hyperoptic.hr.tracker.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model for exception response.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    protected ErrorCode code;
    protected String message;

    public ErrorResponse() {
        this.code = ErrorCode.UNDEFINED;
    }
}
