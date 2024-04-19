package com.edwbadillo.storedemo.common;

/**
 * Represents a simple message response to be sent to the client.
 *
 * @param message the message to display
 *
 * @author edwbadillo
 */
public record SimpleMessageResponse(
    String message
) { }
