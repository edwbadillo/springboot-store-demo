package com.edwbadillo.storedemo.auth.dto;

/**
 * Authentication details from JWT
 *
 * @author edwbadillo
 */
public record AuthenticationDetails(
    Integer id,
    String name,
    String role
) { }
