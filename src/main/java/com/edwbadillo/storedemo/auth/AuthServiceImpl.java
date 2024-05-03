package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.dto.AuthenticationDetails;
import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.auth.dto.LoginRequest;
import com.edwbadillo.storedemo.auth.jwt.JWT;
import com.edwbadillo.storedemo.auth.jwt.JwtService;
import com.edwbadillo.storedemo.auth.userdetails.CustomerUserDetails;
import com.edwbadillo.storedemo.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for login customers or admins
 *
 * @author edwbadillo
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public JWTResponse loginCustomer(LoginRequest loginRequest) {
        String email = "CUSTOMER#" + loginRequest.email();
        String password = loginRequest.password();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
        return jwtService.getToken(customerUserDetails.getCustomer());
    }

    @Override
    public AuthenticationDetails getAuthenticationDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof CustomerUserDetails customerUserDetails) {
                Customer customer = customerUserDetails.getCustomer();
                return new AuthenticationDetails(customer.getId(), customer.getName(), JWT.CUSTOMER_ROLE);
            }
        }
        return null;
    }
}
