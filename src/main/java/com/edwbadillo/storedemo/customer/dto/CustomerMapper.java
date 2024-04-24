package com.edwbadillo.storedemo.customer.dto;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Defines a mapper for {@link Customer} and DTOs.
 *
 * @author edwbadillo
 */
@Service
public class CustomerMapper {

    public PageDTO<CustomerInfo> getPage(Page<Customer> page) {
        return new PageDTO<>(
            page.getContent().stream().map(this::getInfo).toList(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getNumberOfElements()
        );
    }

    public CustomerInfo getInfo(Customer customer) {
        return new CustomerInfo(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getDisabledAt()
        );
    }

    public CustomerDetails getDetails(Customer customer) {
        return new CustomerDetails(
            customer.getId(),
            customer.getDni(),
            customer.getName(),
            customer.getEmail(),
            customer.getDisabledAt()
        );
    }

    public Customer getEntity(CustomerRegistration data) {
        Customer customer = new Customer();
        customer.setDni(data.dni());
        customer.setName(data.name());
        customer.setEmail(data.email());

        // Password must be set with an encoder in service layer

        return customer;
    }

    public CustomerStatusInfo getStatusInfo(Customer customer) {
        return new CustomerStatusInfo(
            customer.getId(),
            customer.getName(),
            customer.getDisabledAt() == null,
            customer.getDisabledAt()
        );
    }

    public void updateEntity(CustomerUpdate data, Customer customer) {
        customer.setDni(data.dni());
        customer.setName(data.name());
        customer.setEmail(data.email());
    }
}
