package com.edwbadillo.storedemo.cart.dto;

import java.util.List;

/**
 * Represents the customer's cart.
 *
 * @author edwbadillo
 */
public record CustomerCart(
     String customerName,
     List<CartItem> items,
     Double subtotal
) { }
