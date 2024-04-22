package com.edwbadillo.storedemo.customer.dto;

/**
 * Data for registration of a new customer.
 *
 * @author edwbadillo
 */
public record CustomerRegistration(
    String dni,
    String name,
    String email,
    String password
) { }
