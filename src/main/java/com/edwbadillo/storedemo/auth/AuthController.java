package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.auth.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for authentication operations (login, refresh token, etc.)
 *
 * @author edwbadillo
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/customers/login")
    public JWTResponse loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.loginCustomer(loginRequest);
    }
}
