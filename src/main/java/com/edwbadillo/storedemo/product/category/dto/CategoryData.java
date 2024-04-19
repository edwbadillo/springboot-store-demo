package com.edwbadillo.storedemo.product.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents the data for creating or updating a category.
 *
 * @author edwbadillo
 */
public record CategoryData(
    @NotNull
    @NotBlank
    @Size(max = 100)
    String name,

    String description,

    @NotNull
    Boolean isActive
) { }