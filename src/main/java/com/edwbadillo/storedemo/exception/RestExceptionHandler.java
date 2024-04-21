package com.edwbadillo.storedemo.exception;

import com.edwbadillo.storedemo.common.InvalidField;
import com.edwbadillo.storedemo.common.InvalidRequestBody;
import com.edwbadillo.storedemo.common.SimpleMessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<InvalidField> invalidFields = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            FieldError fieldError = (FieldError) error;
            invalidFields.add(new InvalidField(
                fieldError.getCode(),
                fieldError.getDefaultMessage(),
                fieldError.getField(),
                fieldError.getRejectedValue()
            ));
        });

        InvalidRequestBody invalidRequestBody = new InvalidRequestBody(
            "Invalid request body, check errors",
            invalidFields
        );

        return new ResponseEntity<>(invalidRequestBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleMessageResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        return new SimpleMessageResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidField handleInvalidDataException(InvalidDataException e) {
        return new InvalidField(
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
