package com.edwbadillo.storedemo.cart.dto;

import java.util.List;

/**
 * Represents the customer's cart information.
 *
 * @author edwbadillo
 */
public record CustomerCartDetails(
     String customerName,
     List<CartItem> items,
     Double subtotal
) { }
