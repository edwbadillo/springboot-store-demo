package com.edwbadillo.storedemo.product.dto;

/**
 * Basic information about a product.
 *
 * @author edwbadillo
 */
public record ProductInfo(
    Integer id,
    String name,
    boolean isActive,
    double price,
    int quantity
) { }
