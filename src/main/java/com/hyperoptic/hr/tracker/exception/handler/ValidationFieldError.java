package com.hyperoptic.hr.tracker.exception.handler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Model for field validation error details.
 */
@Getter
@Setter
@NoArgsConstructor
public class ValidationFieldError {
    /**
     * Field with validation errors
     */
    private String field;
    /**
     * Default validation error message
     */
    private String defaultMessage;
    /**
     * Collection of all validation error codes
     */
    private List<String> codes;
}
