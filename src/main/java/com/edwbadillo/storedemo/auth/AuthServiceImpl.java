package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.auth.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Override
    public JWTResponse loginCustomer(LoginRequest loginRequest) {
        String email = "CUSTOMER#" + loginRequest.email();
        String password = loginRequest.password();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return new JWTResponse("ACCESS TOKEN CUSTOMER");
    }
}
