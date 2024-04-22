package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomerMapperTest {

    private CustomerMapper customerMapper;
    private Customer customer;

    @BeforeEach
    void setup() {
        customerMapper = new CustomerMapper();
        customer = new Customer();
        customer.setId(1);
        customer.setDni("12345678");
        customer.setName("Jon Snow");
        customer.setEmail("j.snow@example.com");
        customer.setPassword("encryptedPassword");
    }

    @Test
    void shouldMapCustomerPageToCustomerInfoPageDTO() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        PageDTO<CustomerInfo> customerInfoPage = customerMapper.getPage(customerPage);

        assertEquals(customerPage.getTotalElements(), customerInfoPage.totalCount());
        assertEquals(customerPage.getTotalPages(), customerInfoPage.totalPages());
        assertEquals(customerPage.getNumber(), customerInfoPage.numPage());
        assertEquals(customerPage.getNumberOfElements(), customerInfoPage.numItems());

        assertEquals(customerInfoPage.items().get(0).id(), 1);
        assertEquals(customerInfoPage.items().get(0).name(), "Jon Snow");
        assertEquals(customerInfoPage.items().get(0).email(), "j.snow@example.com");
        assertNull(customerInfoPage.items().get(0).disabledAt());
    }

    @Test
    void shouldMapCustomerEntityToDetails() {
        CustomerDetails customerDetails = customerMapper.getDetails(customer);

        assertEquals(customer.getId(), customerDetails.id());
        assertEquals(customer.getDni(), customerDetails.dni());
        assertEquals(customer.getName(), customerDetails.name());
        assertEquals(customer.getEmail(), customerDetails.email());
        assertNull(customerDetails.disabledAt());
    }

    @Test
    void shouldMapCustomerEntityToInfo() {
        CustomerInfo customerInfo = customerMapper.getInfo(customer);

        assertEquals(customer.getId(), customerInfo.id());
        assertEquals(customer.getName(), customerInfo.name());
        assertEquals(customer.getEmail(), customerInfo.email());
        assertNull(customerInfo.disabledAt());
    }

    @Test
    void shouldMapCustomerRegistrationToEntity() {
        CustomerRegistration registration = new CustomerRegistration(
                "12345678",
                "Jon Snow",
                "j.snow@example.com",
                "encryptedPassword"
        );

        Customer customer = customerMapper.getEntity(registration);
        assertEquals("12345678", customer.getDni());
        assertEquals("Jon Snow", customer.getName());
        assertEquals("j.snow@example.com", customer.getEmail());

        // Password must be set with an encoder in service layer
        assertNull(customer.getPassword());
    }

    @Test
    void shouldUpdateToEntity() {
        CustomerUpdate update = new CustomerUpdate(
                "123456789000",
                "Jon Snow Updated",
                "jon.snow@example.com"
        );

        customerMapper.updateEntity(update, customer);

        assertEquals("123456789000", customer.getDni());
        assertEquals("Jon Snow Updated", customer.getName());
        assertEquals("jon.snow@example.com", customer.getEmail());
        assertEquals("encryptedPassword", customer.getPassword());
    }

}
