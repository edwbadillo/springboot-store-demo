package com.edwbadillo.storedemo.customer;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Jon Snow");
        customer.setDni("12345678");
        customer.setEmail("j.snow@example.com");
        customer.setPassword("encryptedpassword");
    }
}
