package com.edwbadillo.storedemo.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request for login customers or admins.
 *
 * @author edwbadillo
 */
public record LoginRequest(
    @NotBlank
    String email,
    @NotBlank
    String password
) { }
