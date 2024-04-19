package com.edwbadillo.storedemo.product.category.dto;

import java.time.LocalDateTime;

/**
 * Represents the details of a category.
 *
 * @author edwbadillo
 */
public record CategoryDetails(
    Integer id,
    String name,
    String description,
    Boolean isActive
) { }
