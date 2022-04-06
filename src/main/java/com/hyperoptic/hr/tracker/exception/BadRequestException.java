package com.hyperoptic.hr.tracker.exception;

import lombok.Getter;

import static com.hyperoptic.hr.tracker.exception.ErrorCode.BAD_REQUEST;

/**
 * Exception class used for resources not found case
 */
@Getter
public class BadRequestException extends TrackerException {

    public BadRequestException() {
        super(BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
    }

}
