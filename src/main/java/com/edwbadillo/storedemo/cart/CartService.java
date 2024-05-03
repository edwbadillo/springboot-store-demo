package com.edwbadillo.storedemo.cart;

import com.edwbadillo.storedemo.cart.dto.CustomerCart;

/**
 * Service for managing the authenticated customer's cart.
 *
 * @author edwbadillo
 */
public interface CartService {

    /**
     * Gets the cart for the authenticated customer.
     */
    CustomerCart getCart();

    /**
     * Adds an item to the customer's cart, or updates its quantity if it already exists.
     *
     * @param productId the product ID to add
     * @param quantity  the quantity of the product to add
     * @return the current customer cart
     */
    CustomerCart addToCart(Integer productId, Integer quantity);

    /**
     * Removes an item from the customer's cart.
     *
     * @param productId the product ID to remove
     * @return the current customer cart
     */
    CustomerCart removeFromCart(Integer productId);
}
