package com.hyperoptic.hr.tracker.exception;

import lombok.Getter;

import static com.hyperoptic.hr.tracker.exception.ErrorCode.NOT_FOUND;

/**
 * Exception class used for resources not found case.
 */
@Getter
public class ResourceNotFoundException extends TrackerException {

    public ResourceNotFoundException() {
        super(NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(NOT_FOUND, message);
    }

}
