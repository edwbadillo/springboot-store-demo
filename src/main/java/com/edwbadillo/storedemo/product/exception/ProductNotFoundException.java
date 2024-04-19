package com.edwbadillo.storedemo.product.exception;

import com.edwbadillo.storedemo.exception.ResourceNotFoundException;

/**
 * Exception thrown when a product is not found.
 *
 * @author edwbadillo
 */
public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException() {
        super("Product not found");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
