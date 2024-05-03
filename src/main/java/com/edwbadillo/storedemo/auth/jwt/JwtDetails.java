package com.edwbadillo.storedemo.auth.jwt;

/**
 * Represents the user information from a validated JWT.
 */
public record JwtDetails(
    Integer subject,
    String role
) {
    public boolean isCustomer() {
        return role.equals(JWT.CUSTOMER_ROLE);
    }

    public boolean isAdmin() {
        return role.equals(JWT.USER_ADMIN_ROLE);
    }
}
