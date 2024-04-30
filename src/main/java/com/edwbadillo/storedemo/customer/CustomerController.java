package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Defines the endpoints for managing customer resources.
 *
 * @author edwbadillo
 */
@Tag(name = "Customer", description = "Customer management APIs")
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Paginate customers", description = "Get a slice of customers (paginated).")
    @GetMapping
    public PageDTO<CustomerInfo> paginate(@ParameterObject Pageable pageable) {
        return customerService.paginate(pageable);
    }

    @Operation(summary = "Get customer", description = "Get a existing customer by its ID.")
    @GetMapping("/{id}")
    public CustomerDetails findById(@PathVariable Integer id) {
        return customerService.getById(id);
    }

    @Operation(summary = "Create customer", description = "Create a new customer with the given data.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDetails create(@Valid @RequestBody CustomerRegistration data) {
        return customerService.register(data);
    }

    @Operation(summary = "Update customer", description = "Update an existing customer with the given data.")
    @PutMapping("/{id}")
    public CustomerDetails update(@PathVariable Integer id, @Valid @RequestBody CustomerUpdate customerData) {
        return customerService.update(id, customerData);
    }

    @Operation(summary = "Disable customer", description = "Disable an existing customer.")
    @PutMapping("/{id}/disable")
    public CustomerStatusInfo disableById(@PathVariable Integer id) {
        return customerService.disableById(id);
    }

    @Operation(summary = "Enable customer", description = "Enable an existing customer.")
    @PutMapping("/{id}/enable")
    public CustomerStatusInfo enableById(@PathVariable Integer id) {
        return customerService.enableById(id);
    }
}
