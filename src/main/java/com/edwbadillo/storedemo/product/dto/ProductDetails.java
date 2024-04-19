package com.edwbadillo.storedemo.product.dto;

import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;

/**
 * Full details about a product.
 *
 * @author edwbadillo
 */
public record ProductDetails(
    Integer id,
    String name,
    String description,
    boolean isActive,
    double price,
    int quantity,
    CategoryInfo category
) { }
