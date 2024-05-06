package com.edwbadillo.storedemo.cart.dto;

import com.edwbadillo.storedemo.cart.CartProduct;
import com.edwbadillo.storedemo.customer.Customer;
import com.edwbadillo.storedemo.product.dto.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Mapper for customer cart objects.
 *
 * @author edwbadillo
 */
@Service
public class CartMapper {

    @Autowired
    private ProductMapper productMapper;

    public CustomerCartDetails getCustomerCart(Customer customer, List<CartProduct> cartProducts) {

        List<CartItem> cartItems = cartProducts
                .stream()
                .map(cartProduct -> new CartItem(cartProduct.getQuantity(), productMapper.getInfo(cartProduct.getProduct())))
                .toList();

        Double subtotal = cartProducts
                .stream()
                .mapToDouble(cartProduct -> cartProduct.getQuantity() * cartProduct.getProduct().getPrice())
                .sum();

        return new CustomerCartDetails(customer.getName(), cartItems, subtotal);
    }
}
