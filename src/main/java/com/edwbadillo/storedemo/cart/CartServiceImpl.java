package com.edwbadillo.storedemo.cart;

import com.edwbadillo.storedemo.auth.userdetails.CustomerUserDetails;
import com.edwbadillo.storedemo.cart.dto.CartMapper;
import com.edwbadillo.storedemo.cart.dto.CustomerCartDetails;
import com.edwbadillo.storedemo.customer.Customer;
import com.edwbadillo.storedemo.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link CartService} interface for managing a customer's shopping cart.
 *
 * @author edwbadillo
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public CustomerCartDetails getCart() {
        Customer customer = getAuthenticatedCustomer();
        List<CartProduct> cartProducts = cartProductRepository.findByCustomerId(customer.getId());
        return cartMapper.getCustomerCart(customer, cartProducts);
    }

    @Override
    public CustomerCartDetails addToCart(Integer productId, Integer quantity) {
        return null;
    }

    @Override
    public CustomerCartDetails removeFromCart(Integer productId) {
        return null;
    }

    private Customer getAuthenticatedCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
        return customerUserDetails.getCustomer();
    }
}
