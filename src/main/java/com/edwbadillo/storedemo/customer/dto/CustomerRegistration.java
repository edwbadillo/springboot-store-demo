package com.edwbadillo.storedemo.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data for registration of a new customer.
 *
 * @author edwbadillo
 */
public record CustomerRegistration(
    @NotBlank
    @Size(min = 5, max = 20)
    String dni,

    @NotBlank
    @Size(min = 3, max = 100)
    String name,

    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 6, max = 12)
    String password
) { }
