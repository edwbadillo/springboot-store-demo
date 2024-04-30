package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.auth.dto.LoginRequest;

/**
 * Service for login customers or admins
 *
 * @author edwbadillo
 */
public interface AuthService {

    /**
     * Authenticates a customer and create a JWT.
     *
     * @param loginRequest the customer credentials
     * @return access token
     */
    JWTResponse loginCustomer(LoginRequest loginRequest);
}
