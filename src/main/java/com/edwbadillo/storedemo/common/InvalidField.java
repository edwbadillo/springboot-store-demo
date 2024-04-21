package com.edwbadillo.storedemo.common;

/**
 * Represents an invalid data response to be sent to the client.
 *
 * @author edwbadillo
 */
public record InvalidField(
    String type,
    String message,
    String field,
    Object value
) { }
