package com.edwbadillo.storedemo.cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link CartProduct} objects.
 *
 * @author edwbadillo
 */
@Repository
public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

    Optional<CartProduct> findByCustomerId(Integer customerId);

    Optional<CartProduct> findByCustomerIdAndProductId(Integer customerId, Integer productId);
}
