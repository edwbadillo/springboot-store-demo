package com.edwbadillo.storedemo.auth;

import com.edwbadillo.storedemo.auth.dto.AuthenticationDetails;
import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.auth.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for authentication operations (login, refresh token, etc.)
 *
 * @author edwbadillo
 */
@Tag(name = "Auth", description = "Authentication APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Login customer", description = "Get a JWT for a customer.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token issued" ,
                    content = { @Content(schema = @Schema(implementation = JWTResponse.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid credentials or inactive account",
                    content = { @Content(schema = @Schema(), mediaType = "application/json") }),
    })
    @PostMapping("/customers/login")
    public JWTResponse loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.loginCustomer(loginRequest);
    }

    @Operation(summary = "Get authentication details", description = "Get information about the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication details" ,
                    content = { @Content(schema = @Schema(implementation = AuthenticationDetails.class), mediaType = "application/json") }),
    })
    @GetMapping
    public AuthenticationDetails getAuthenticationDetails() {
        return authService.getAuthenticationDetails();
    }
}
