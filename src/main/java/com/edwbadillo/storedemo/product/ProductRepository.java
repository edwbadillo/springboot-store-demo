package com.edwbadillo.storedemo.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Product}.
 *
 * @author edwbadillo
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
}
