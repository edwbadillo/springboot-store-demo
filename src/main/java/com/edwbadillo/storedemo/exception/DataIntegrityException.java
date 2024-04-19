package com.edwbadillo.storedemo.exception;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * Exception that is thrown when a possible data integration violation is
 * detected, for example, references to non-existent resources or that a
 * resource when deleted has dependencies with it.
 *
 * <p>This exception is mainly used to be captured by the ControllerAdvice
 * and display a response with status 409.
 *
 * @author edwbadillo
 */
public class DataIntegrityException extends DataIntegrityViolationException {

    public DataIntegrityException(String message) {
        super(message);
    }
}
