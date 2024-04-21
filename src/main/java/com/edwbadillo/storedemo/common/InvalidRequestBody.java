package com.edwbadillo.storedemo.common;

import java.util.List;

/**
 * Represents an invalid request body to be sent to the client.
 *
 * @author edwbadillo
 */
public record InvalidRequestBody(
    String message,
    List<InvalidField> errors
) { }
