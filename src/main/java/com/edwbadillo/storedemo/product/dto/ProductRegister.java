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

    @NotNull
    Boolean isActive,

    @NotNull
    @DecimalMin(value = "0.0", message = "The price must be greater than or equal to 0")
    Double price,

    @NotNull
    @Min(value = 0, message = "The quantity must be greater than or equal to 0")
    Integer quantity,

    @NotNull
    @Min(0)
    Integer categoryId
) { }
