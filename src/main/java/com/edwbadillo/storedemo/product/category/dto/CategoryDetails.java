package com.edwbadillo.storedemo.product.category.dto;

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
