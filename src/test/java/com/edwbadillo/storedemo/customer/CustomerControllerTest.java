package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import com.edwbadillo.storedemo.customer.exception.CustomerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void shouldPaginateCustomers() throws Exception {
        CustomerInfo customer = new CustomerInfo(1, "Customer", "wZvZD@example.com", null);

        when(customerService.paginate(any(Pageable.class))).thenReturn(new PageDTO<>(
                List.of(customer),
                1,
                1,
                1,
                1
        ));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(customer.id()))
                .andExpect(jsonPath("$.items[0].name").value(customer.name()))
                .andExpect(jsonPath("$.items[0].email").value(customer.email()))
                .andExpect(jsonPath("$.items[0].disabledAt").value(customer.disabledAt()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.numPage").value(1))
                .andExpect(jsonPath("$.numItems").value(1));
    }


    @Test
    void shouldGetCustomerById() throws Exception {
        CustomerDetails customerDetails = buildCustomerDetails();
        when(customerService.getById(anyInt())).thenReturn(customerDetails);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerDetails.id()))
                .andExpect(jsonPath("$.dni").value(customerDetails.dni()))
                .andExpect(jsonPath("$.name").value(customerDetails.name()))
                .andExpect(jsonPath("$.email").value(customerDetails.email()))
                .andExpect(jsonPath("$.disabledAt").value(customerDetails.disabledAt()));
    }

    @Test
    void shouldGet404WhenCustomerDoesNotExist() throws Exception {
        when(customerService.getById(1))
                .thenThrow(new CustomerNotFoundException("Customer 1 not found"));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer 1 not found"));
    }

    @Test
    void shouldRegisterCustomer() throws Exception {
        CustomerDetails customerDetails = buildCustomerDetails();
        when(customerService.register(any(CustomerRegistration.class))).thenReturn(customerDetails);

        CustomerRegistration customerRegistration = new CustomerRegistration(
                "12345678",
                "Jon Smith",
                "jsmith@test.com",
                "password"
        );

        String jsonData = new ObjectMapper().writeValueAsString(customerRegistration);

        mockMvc.perform(
                    post("/api/customers")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customerDetails.id()))
                .andExpect(jsonPath("$.dni").value(customerDetails.dni()))
                .andExpect(jsonPath("$.name").value(customerDetails.name()))
                .andExpect(jsonPath("$.email").value(customerDetails.email()))
                .andExpect(jsonPath("$.disabledAt").value(customerDetails.disabledAt()));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        CustomerDetails customerDetails = buildCustomerDetails();
        when(customerService.update(anyInt(), any(CustomerUpdate.class))).thenReturn(customerDetails);

        CustomerUpdate customerUpdate = new CustomerUpdate(
                "12345678",
                "Jon Smith",
                "jsmith@test.com"
        );

        String jsonData = new ObjectMapper().writeValueAsString(customerUpdate);

        mockMvc.perform(
                    put("/api/customers/1")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerDetails.id()))
                .andExpect(jsonPath("$.dni").value(customerDetails.dni()))
                .andExpect(jsonPath("$.name").value(customerDetails.name()))
                .andExpect(jsonPath("$.email").value(customerDetails.email()))
                .andExpect(jsonPath("$.disabledAt").value(customerDetails.disabledAt()));
    }

    @Test
    void shouldDisableCustomer() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CustomerStatusInfo statusInfo = new CustomerStatusInfo(
                1,
                "Jon Smith",
                false,
                now
        );

        CustomerDetails customerDetails = buildCustomerDetails();
        when(customerService.disableById(1)).thenReturn(statusInfo);

        mockMvc.perform(
                    put("/api/customers/1/disable")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusInfo.id()))
                .andExpect(jsonPath("$.name").value(statusInfo.name()))
                .andExpect(jsonPath("$.isActive").value(statusInfo.isActive()))
                .andExpect(jsonPath("$.disabledAt").value(statusInfo.disabledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void shouldGet404WhenDisableCustomerDoesNotExist() throws Exception {
        when(customerService.disableById(1))
                .thenThrow(new CustomerNotFoundException("Customer 1 not found"));

        mockMvc.perform(
                    put("/api/customers/1/disable")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer 1 not found"));
    }

    @Test
    void shouldEnableCustomer() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CustomerStatusInfo statusInfo = new CustomerStatusInfo(
                1,
                "Jon Smith",
                true,
                now
        );

        CustomerDetails customerDetails = buildCustomerDetails();
        when(customerService.enableById(1)).thenReturn(statusInfo);

        mockMvc.perform(
                    put("/api/customers/1/enable")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusInfo.id()))
                .andExpect(jsonPath("$.name").value(statusInfo.name()))
                .andExpect(jsonPath("$.isActive").value(statusInfo.isActive()))
                .andExpect(jsonPath("$.disabledAt").value(statusInfo.disabledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void shouldGet404WhenEnableCustomerDoesNotExist() throws Exception {
        when(customerService.enableById(1))
                .thenThrow(new CustomerNotFoundException("Customer 1 not found"));

        mockMvc.perform(
                    put("/api/customers/1/enable")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer 1 not found"));
    }

    private CustomerDetails buildCustomerDetails() {
        return new CustomerDetails(1, "12345678", "Jon Smith", "jsmith@test.com", null);
    }
}
