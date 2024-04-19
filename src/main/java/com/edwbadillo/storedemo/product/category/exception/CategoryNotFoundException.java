package com.edwbadillo.storedemo.product.category.exception;


import com.edwbadillo.storedemo.exception.ResourceNotFoundException;

/**
 * Thrown when a requested category is not found.
 *
 * @author edwbadillo
 */
public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException() {
        super("Category not found");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
