package com.edwbadillo.storedemo.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data for updating a customer.
 *
 * @author edwbadillo
 */
public record CustomerUpdate(
        @NotBlank
        @Size(min = 5, max = 20)
        String dni,

        @NotBlank
        @Size(min = 3, max = 100)
        String name,

        @NotBlank
        @Email
        String email
) { }
