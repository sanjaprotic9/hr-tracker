package com.hyperoptic.hr.tracker.exception.handler;

import com.hyperoptic.hr.tracker.exception.TrackerException;
import com.hyperoptic.hr.tracker.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static com.hyperoptic.hr.tracker.exception.ErrorCode.ACCESS_DENIED;
import static com.hyperoptic.hr.tracker.exception.ErrorCode.INVALID_REQUEST_DATA;
import static com.hyperoptic.hr.tracker.exception.ErrorCode.UNDEFINED;
import static com.hyperoptic.hr.tracker.exception.ErrorCode.VALIDATION_ERROR;
import static java.util.stream.Collectors.toList;;

/**
 * Custom exception handler for REST endpoints.
 */
@ControllerAdvice
@RestController
@Getter
@Slf4j
@RequiredArgsConstructor
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ModelMapper modelMapper;

    @ExceptionHandler(TrackerException.class)
    public final ResponseEntity<ErrorResponse> handleTrackerExceptionException(TrackerException ex, WebRequest request) {
        var message = ObjectUtils.isEmpty(ex.getMessage()) ? ex.getCode().name() : ex.getMessage();

        log.error("Handling TrackerException exception: {}", message);

        ErrorResponse exceptionResponse = new ErrorResponse(ex.getCode(), message);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                               WebRequest request) {
        log.error("Handling resource not found exception", ex);

        ErrorResponse exceptionResponse = new ErrorResponse(ex.getCode(), ex.getCode().name());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                                                                           WebRequest request) {
        log.error("Handling access denied exception", ex);

        ErrorResponse exceptionResponse = new ErrorResponse(ACCESS_DENIED, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Handling exception", ex);

        ErrorResponse exceptionResponse = new ErrorResponse(UNDEFINED, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.debug("Handling validation exception: {}", ex.getMessage());

        final List<ValidationFieldError> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> modelMapper.map(objectError, ValidationFieldError.class))
                .collect(toList());

        ErrorResponse exceptionResponse = new ValidationErrorResponse(VALIDATION_ERROR.name(), errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.debug("Handling json convert to object validation exception: {}", ex.getMessage());

        ErrorResponse exceptionResponse = new ErrorResponse(INVALID_REQUEST_DATA, "Invalid request data");

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
