package com.edwbadillo.storedemo.exception;

import java.util.NoSuchElementException;

/**
 * Represents an exception thrown when a resource (rows, files, etc.) is not found.
 */
public class ResourceNotFoundException extends NoSuchElementException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
