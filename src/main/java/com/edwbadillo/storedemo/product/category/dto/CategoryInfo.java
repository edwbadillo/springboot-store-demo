package com.edwbadillo.storedemo.product.category.dto;

/**
 * Represents the basic details of a category.
 *
 * @author edwbadillo
 */
public record CategoryInfo(
    Integer id,
    String name,
    Boolean isActive
) { }
