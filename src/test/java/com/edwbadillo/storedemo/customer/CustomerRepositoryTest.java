package com.edwbadillo.storedemo.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldPaginate() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Customer customer = new Customer();
            customer.setName("Customer " + i);
            customer.setDni("11223345" + i);
            customer.setEmail("j.snow"+ i +"@example.com");
            customer.setPassword("encryptedpassword");
            customers.add(customer);
        }

        customerRepository.saveAll(customers);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Customer> pageResult = customerRepository.findAll(pageable);
        assertEquals(10, pageResult.getSize());
        assertEquals(15, pageResult.getTotalElements());
    }

    @Test
    void shouldCustomerSavedThenFoundById() {
        customerRepository.save(customer);
        em.detach(customer);

        Optional<Customer> result = customerRepository.findById(customer.getId());
        assertTrue(result.isPresent());

        assertEquals(result.get().getId(), customer.getId());
        assertEquals(result.get().getName(), customer.getName());
        assertEquals(result.get().getDni(), customer.getDni());
        assertEquals(result.get().getEmail(), customer.getEmail());
        assertEquals(result.get().getPassword(), customer.getPassword());
        assertNull(result.get().getDisabledAt());
    }

    @Test
    void shouldUpdateCustomer() {
        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.flush();

        LocalDateTime now = LocalDateTime.now();
        savedCustomer.setName("Jon Snow Updated");
        savedCustomer.setDni("99887654");
        savedCustomer.setEmail("updated@example.com");
        savedCustomer.setDisabledAt(now);

        customerRepository.save(savedCustomer);
        customerRepository.flush();

        Optional<Customer> result = customerRepository.findById(savedCustomer.getId());
        assertTrue(result.isPresent());

        assertEquals("Jon Snow Updated", customer.getName());
        assertEquals("99887654", customer.getDni());
        assertEquals("updated@example.com", customer.getEmail());
        assertEquals(now, customer.getDisabledAt());
    }

    @Test
    void shouldDeleteCustomer() {
        customerRepository.save(customer);
        customerRepository.deleteById(customer.getId());
        assertFalse(customerRepository.existsById(customer.getId()));
    }

    @Test
    void shouldExistsByDni() {
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByDni(customer.getDni()));
        assertFalse(customerRepository.existsByDni("22223333"));
    }

    @Test
    void shouldExistsByDniAndIdNot() {
        customerRepository.save(customer);

        int excludeId = customer.getId() + 1;
        assertTrue(customerRepository.existsByDniAndIdNot(customer.getDni(), excludeId));
        assertFalse(customerRepository.existsByDniAndIdNot(customer.getDni(), customer.getId()));
        assertFalse(customerRepository.existsByDniAndIdNot("22223333", customer.getId()));
        assertFalse(customerRepository.existsByDniAndIdNot("12345679", excludeId));
    }

    @Test
    void shouldExistsByEmail() {
        customerRepository.save(customer);

        assertTrue(customerRepository.existsByEmail(customer.getEmail()));
        assertFalse(customerRepository.existsByEmail("another@example.com"));
    }

    @Test
    void shouldExistsByEmailAndIdNot() {
        customerRepository.save(customer);

        int excludeId = customer.getId() + 1;
        assertTrue(customerRepository.existsByEmailAndIdNot(customer.getEmail(), excludeId));
        assertFalse(customerRepository.existsByEmailAndIdNot(customer.getEmail(), customer.getId()));
        assertFalse(customerRepository.existsByEmailAndIdNot("another@example.com", customer.getId()));
        assertFalse(customerRepository.existsByEmailAndIdNot("another@example.com", excludeId));
    }
}
