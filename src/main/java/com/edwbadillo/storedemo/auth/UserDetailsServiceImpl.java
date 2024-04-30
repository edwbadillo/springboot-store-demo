package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.userdetails.CustomerUserDetails;
import com.edwbadillo.storedemo.customer.Customer;
import com.edwbadillo.storedemo.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service for loading user details from customers or admins
 *
 * @author edwbadillo
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // The username can be either a customer email or an admin email
        // the format is CUSTOMER#email or ADMIN#email

        if (username.startsWith("CUSTOMER#")) {
            String email = username.replace("CUSTOMER#", "");
            return loadCustomer(email);
        }

        if (username.startsWith("ADMIN#")) {
            throw new UsernameNotFoundException("Admin not found, not implemented yet");
        }

        throw new UsernameNotFoundException("Admin not found, not implemented yet");
    }

    private UserDetails loadCustomer(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("Customer not found");
        }

        return new CustomerUserDetails(customer.get());
    }
}
