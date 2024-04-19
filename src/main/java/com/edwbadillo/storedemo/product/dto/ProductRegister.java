package com.edwbadillo.storedemo.product.dto;

import jakarta.validation.constraints.*;

/**
 * Data for a product registration (create or update).
 *
 * @author edwbadillo
 */
public record ProductRegister(
    @NotNull
    @NotBlank
    @Size(max = 100)
    String name,
    String description,

    @NotBlank
    Boolean isActive,

    @NotNull
    @NotBlank
    @Min(0)
    Double price,

    @NotNull
    @NotBlank
    @Min(0)
    Integer quantity,

    @NotNull
    @NotBlank
    @Min(0)
    Integer categoryId
) { }
