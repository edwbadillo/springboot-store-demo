package com.edwbadillo.storedemo.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The customer repository.
 *
 * @author edwbadillo
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    boolean existsByDni(String dni);
    boolean existsByDniAndIdNot(String dni, Integer id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Integer id);
}
