package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * The service for managing {@link Customer} objects.
 *
 * @author edwbadillo
 */
public interface CustomerService {

    /**
     * Get a paginated list of customers.
     *
     * @param pageable the pagination information
     * @return a {@link PageDTO} of the {@link CustomerInfo} objects
     */
    PageDTO<CustomerInfo> paginate(Pageable pageable);

    /**
     * Get a customer by its id.
     *
     * @param id the id of the customer, must be present
     * @return the customer
     */
    CustomerDetails getById(Integer id);

    /**
     * Register a new customer.
     *
     * @param data the customer data
     * @return the created customer
     */
    CustomerDetails register(CustomerRegistration data);

    /**
     * Update an existing customer.
     *
     * @param id the id of the customer, must be present
     * @param data the customer data
     * @return the updated customer
     */
    CustomerDetails update(Integer id, CustomerUpdate data);

    /**
     * Disable a customer by its id.
     *
     * @param id the id of the customer
     * @return a status information
     */
    CustomerStatusInfo disableById(Integer id);

    /**
     * Enable a customer by its id.
     *
     * @param id the id of the customer
     * @return a status information
     */
    CustomerStatusInfo enableById(Integer id);
}
