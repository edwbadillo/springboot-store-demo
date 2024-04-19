package com.edwbadillo.storedemo.exception;

import com.edwbadillo.storedemo.common.InvalidDataResponse;
import com.edwbadillo.storedemo.common.SimpleMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleMessageResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        return new SimpleMessageResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidDataResponse handleInvalidDataException(InvalidDataException e) {
        return new InvalidDataResponse(
            e.getType(),
            e.getMessage(),
            e.getField(),
            e.getValue()
        );
    }

    @ExceptionHandler(DataIntegrityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public SimpleMessageResponse handleDataIntegrityViolation(DataIntegrityException e) {
        return new SimpleMessageResponse(e.getMessage());
    }
}
