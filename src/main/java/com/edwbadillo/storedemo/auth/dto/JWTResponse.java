package com.edwbadillo.storedemo.auth.dto;

/**
 * Response for login customers or admins
 *
 * @author edwbadillo
 */
public record JWTResponse(
    String accessToken
) { }
