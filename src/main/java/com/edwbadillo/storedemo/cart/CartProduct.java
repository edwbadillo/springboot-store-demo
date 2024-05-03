package com.edwbadillo.storedemo.cart;

import com.edwbadillo.storedemo.customer.Customer;
import com.edwbadillo.storedemo.product.Product;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a product in the customer's cart. A customer can have multiple products in the cart.
 *
 * @author edwbadillo
 */
@Entity
@Table(name = "cart_product")
@Data
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
