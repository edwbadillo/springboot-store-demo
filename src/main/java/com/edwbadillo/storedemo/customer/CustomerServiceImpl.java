package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import com.edwbadillo.storedemo.customer.exception.CustomerNotFoundException;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The default implementation of {@link CustomerService}.
 *
 * @author edwbadillo
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageDTO<CustomerInfo> paginate(Pageable pageable) {
        Page<Customer> page = customerRepository.findAll(pageable);
        return customerMapper.getPage(page);
    }

    @Override
    public CustomerDetails getById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer " + id + " not found")
        );
        return customerMapper.getDetails(customer);
    }

    @Override
    public CustomerDetails register(CustomerRegistration data) {
        if (customerRepository.existsByDni(data.dni())) {
            throw new InvalidDataException("already_exists", "dni", "DNI already exists", data.dni());
        }

        if (customerRepository.existsByEmail(data.email())) {
            throw new InvalidDataException("already_exists", "email", "Email already exists", data.email());
        }

        Customer customer = customerMapper.getEntity(data);

        //TODO: Password should be hashed with BCrypt in Spring Security
        String hashedPassword = data.password();
        customer.setPassword(hashedPassword);

        customerRepository.save(customer);
        return customerMapper.getDetails(customer);
    }

    @Override
    public CustomerDetails update(Integer id, CustomerUpdate data) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer " + id + " not found")
        );

        if (customerRepository.existsByDniAndIdNot(data.dni(), id)) {
            throw new InvalidDataException("already_exists", "dni", "DNI already exists", data.dni());
        }

        if (customerRepository.existsByEmailAndIdNot(data.email(), id)) {
            throw new InvalidDataException("already_exists", "email", "Email already exists", data.email());
        }

        customerMapper.updateEntity(data, customer);
        customerRepository.save(customer);
        return customerMapper.getDetails(customer);
    }

    @Override
    public CustomerStatusInfo disableById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer " + id + " not found")
        );
        if (!customer.isDisabled()) {
            customer.setDisabledAt(LocalDateTime.now());
            customerRepository.save(customer);
        }

        return customerMapper.getStatusInfo(customer);
    }

    @Override
    public CustomerStatusInfo enableById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer " + id + " not found")
        );
        if (customer.isDisabled()) {
            customer.setDisabledAt(null);
            customerRepository.save(customer);
        }

        return customerMapper.getStatusInfo(customer);
    }
}
