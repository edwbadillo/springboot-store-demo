package com.edwbadillo.storedemo.cart;

import com.edwbadillo.storedemo.auth.userdetails.CustomerUserDetails;
import com.edwbadillo.storedemo.cart.dto.CartMapper;
import com.edwbadillo.storedemo.cart.dto.CustomerCartDetails;
import com.edwbadillo.storedemo.customer.Customer;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.Product;
import com.edwbadillo.storedemo.product.ProductRepository;
import com.edwbadillo.storedemo.product.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return getCustomerCartDetails(getAuthenticatedCustomer());
    }

    @Override
    public CustomerCartDetails addToCart(Integer productId, Integer quantity) {
        Customer customer = getAuthenticatedCustomer();
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (!product.isActive()) {
            throw new InvalidDataException(
                    "invalid_value", "productId", "Product is not active, can't be added to cart", productId
            );
        }

        // Check if product is already in cart
        Optional<CartProduct> result = cartProductRepository.findByCustomerIdAndProductId(customer.getId(), productId);
        CartProduct cartProduct;

        if (result.isEmpty()) {
            cartProduct = new CartProduct(customer, product, quantity);
        } else {
            cartProduct = result.get();
            cartProduct.setQuantity(quantity);
        }

        cartProductRepository.save(cartProduct);
        return getCustomerCartDetails(customer);
    }

    @Transactional
    @Override
    public CustomerCartDetails removeFromCart(Integer productId) {
        Customer customer = getAuthenticatedCustomer();
        cartProductRepository.deleteByCustomerIdAndProductId(customer.getId(), productId);
        return getCustomerCartDetails(customer);
    }

    /**
     * Gets all products in the customer's cart.
     *
     * @param customer the customer authenticated
     * @return customer's cart details
     */
    private CustomerCartDetails getCustomerCartDetails(Customer customer) {
        List<CartProduct> cartProducts = cartProductRepository.findByCustomerId(customer.getId());
        return cartMapper.getCustomerCart(customer, cartProducts);
    }

    /**
     * Gets the authenticated customer.
     */
    private Customer getAuthenticatedCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
        return customerUserDetails.getCustomer();
    }
}
