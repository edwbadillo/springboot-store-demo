package com.edwbadillo.storedemo.customer;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.customer.dto.*;
import com.edwbadillo.storedemo.customer.exception.CustomerNotFoundException;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerMapper customerMapper;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "12345678Z", "John", "j@j.com", "password", null);
    }

    @Test
    void shouldPaginateCustomers() {
        Page<Customer> page = new PageImpl<>(List.of(customer));
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        when(customerRepository.findAll(pageable)).thenReturn(page);
        when(customerMapper.getPage(page)).thenReturn(new PageDTO<>(
                List.of(new CustomerInfo(
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getDisabledAt()
                )),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getNumberOfElements()
        ));

        PageDTO<CustomerInfo> result = service.paginate(pageable);

        assertEquals(page.getTotalElements(), result.totalCount());
        assertEquals(page.getTotalPages(), result.totalPages());
        assertEquals(page.getNumber(), result.numPage());
        assertEquals(page.getNumberOfElements(), result.numItems());
    }

    @Test
    void shouldGetCustomerById() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.getDetails(customer)).thenReturn(new CustomerDetails(
                customer.getId(),
                customer.getDni(),
                customer.getName(),
                customer.getEmail(),
                customer.getDisabledAt()
        ));

        CustomerDetails result = service.getById(customer.getId());

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getDni(), result.dni());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.getEmail(), result.email());
        assertEquals(customer.getDisabledAt(), result.disabledAt());
    }

    @Test
    void shouldThrowExceptionWhenGetCustomerByIdNotFound() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class, () -> service.getById(customer.getId())
        );

        assertEquals("Customer " + customer.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldRegisterCustomer() {
        CustomerRegistration registration = getCustomerRegistration();

        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerRepository.existsByDni(customer.getDni())).thenReturn(false);
        when(customerMapper.getEntity(registration)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.getDetails(customer)).thenReturn(new CustomerDetails(
                customer.getId(),
                customer.getDni(),
                customer.getName(),
                customer.getEmail(),
                customer.getDisabledAt()
        ));

        CustomerDetails result = service.register(registration);

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getDni(), result.dni());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.getEmail(), result.email());
        assertEquals(customer.getDisabledAt(), result.disabledAt());
    }

    @Test
    void shouldThrowExceptionWhenRegisterWithAlreadyExistsByDni() {
        CustomerRegistration registration = getCustomerRegistration();

        when(customerRepository.existsByDni(customer.getDni())).thenReturn(true);
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.register(registration)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("dni", exception.getField());
        assertEquals("DNI already exists", exception.getMessage());
        assertEquals(customer.getDni(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenRegisterWithAlreadyExistsByEmail() {
        CustomerRegistration registration = getCustomerRegistration();

        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);
        when(customerRepository.existsByDni(customer.getDni())).thenReturn(false);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.register(registration)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("email", exception.getField());
        assertEquals("Email already exists", exception.getMessage());
        assertEquals(customer.getEmail(), exception.getValue());
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerUpdate customerUpdate = getCustomerUpdate();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDniAndIdNot(customer.getDni(), customer.getId())).thenReturn(false);
        when(customerRepository.existsByEmailAndIdNot(customer.getEmail(), customer.getId())).thenReturn(false);
        doNothing().when(customerMapper).updateEntity(customerUpdate, customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.getDetails(customer)).thenReturn(new CustomerDetails(
                customer.getId(),
                customer.getDni(),
                customer.getName(),
                customer.getEmail(),
                customer.getDisabledAt()
        ));

        CustomerDetails result = service.update(customer.getId(), customerUpdate);

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getDni(), result.dni());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.getEmail(), result.email());
        assertEquals(customer.getDisabledAt(), result.disabledAt());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCustomerNotFound() {
        CustomerUpdate customerUpdate = getCustomerUpdate();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class, () -> service.update(customer.getId(), customerUpdate)
        );

        assertEquals("Customer " + customer.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithAlreadyExistsByDni() {
        CustomerUpdate customerUpdate = getCustomerUpdate();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDniAndIdNot(customer.getDni(), customer.getId())).thenReturn(true);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(customer.getId(), customerUpdate)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("dni", exception.getField());
        assertEquals("DNI already exists", exception.getMessage());
        assertEquals(customer.getDni(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithAlreadyExistsByEmail() {
        CustomerUpdate customerUpdate = getCustomerUpdate();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmailAndIdNot(customer.getEmail(), customer.getId())).thenReturn(true);
        when(customerRepository.existsByDniAndIdNot(customer.getDni(), customer.getId())).thenReturn(false);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(customer.getId(), customerUpdate)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("email", exception.getField());
        assertEquals("Email already exists", exception.getMessage());
        assertEquals(customer.getEmail(), exception.getValue());
    }

    @Test
    void shouldDisableCustomerById() {
        LocalDateTime now = LocalDateTime.now();
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.getStatusInfo(customer)).thenReturn(new CustomerStatusInfo(
                customer.getId(),
                customer.getName(),
                customer.getDisabledAt() == null,
                now
        ));
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerStatusInfo result = service.disableById(customer.getId());

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.isDisabled(), result.isActive());
        assertEquals(now, result.disabledAt());
    }

    @Test
    void shouldDoNothingWhenDisableCustomerDisabled() {
        LocalDateTime now = LocalDateTime.now();
        customer.setDisabledAt(now);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.getStatusInfo(customer)).thenReturn(new CustomerStatusInfo(
                customer.getId(),
                customer.getName(),
                customer.isDisabled(),
                now
        ));

        CustomerStatusInfo result = service.disableById(customer.getId());

        verify(customerRepository, never()).save(customer);

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.isDisabled(), result.isActive());
        assertEquals(now, result.disabledAt());

    }

    @Test
    void shouldThrowExceptionWhenDisableCustomerNotFound() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class, () -> service.disableById(customer.getId())
        );

        assertEquals("Customer " + customer.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldEnableCustomerById() {
        LocalDateTime now = LocalDateTime.now();
        customer.setDisabledAt(now);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.getStatusInfo(customer)).thenReturn(new CustomerStatusInfo(
                customer.getId(),
                customer.getName(),
                customer.isDisabled(),
                null
        ));
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerStatusInfo result = service.enableById(customer.getId());

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(!customer.isDisabled(), result.isActive());
        assertNull(result.disabledAt());
    }

    @Test
    void shouldThrowExceptionWhenEnableCustomerNotFound() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class, () -> service.enableById(customer.getId())
        );

        assertEquals("Customer " + customer.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldDoNothingWhenEnableCustomerEnabled() {
        customer.setDisabledAt(null);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.getStatusInfo(customer)).thenReturn(new CustomerStatusInfo(
                customer.getId(),
                customer.getName(),
                customer.isDisabled(),
                null
        ));

        CustomerStatusInfo result = service.enableById(customer.getId());

        verify(customerRepository, never()).save(customer);

        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.isDisabled(), result.isActive());
        assertNull(result.disabledAt());
    }

    private CustomerUpdate getCustomerUpdate() {
        return new CustomerUpdate(
                customer.getDni(),
                customer.getName(),
                customer.getEmail()
        );
    }

    private CustomerRegistration getCustomerRegistration() {
        return new CustomerRegistration(
                customer.getDni(),
                customer.getName(),
                customer.getEmail(),
                customer.getPassword()
        );
    }
}
