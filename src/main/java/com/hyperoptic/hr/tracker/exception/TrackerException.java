package com.hyperoptic.hr.tracker.exception;

import lombok.Getter;

import static com.hyperoptic.hr.tracker.exception.ErrorCode.BAD_REQUEST;

/**
 * Custom exception class used for various API rest actions.
 */
@Getter
public class TrackerException extends RuntimeException {
    private final ErrorCode code;

    public TrackerException() {
        this.code = BAD_REQUEST;
    }

    public TrackerException(ErrorCode code) {
        this.code = code;
    }

    public TrackerException(String message) {
        super(message);
        this.code = BAD_REQUEST;
    }

    public TrackerException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public TrackerException(ErrorCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public TrackerException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
