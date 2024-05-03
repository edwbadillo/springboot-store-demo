package com.edwbadillo.storedemo.cart.dto;

import com.edwbadillo.storedemo.product.dto.ProductInfo;

/**
 * Represents an item in the customer's cart.
 *
 * @author edwbadillo
 */
public record CartItem(
    Integer quantity,
    ProductInfo product
) { }
