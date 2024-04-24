package com.edwbadillo.storedemo.customer.exception;

import com.edwbadillo.storedemo.exception.ResourceNotFoundException;

/**
 * Exception thrown when a customer is not found.
 *
 * @author edwbadillo
 */
public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException() {
        super("Customer not found");
    }
}
