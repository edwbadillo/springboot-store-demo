package com.edwbadillo.storedemo.customer.dto;

/**
 * Data for updating a customer.
 *
 * @author edwbadillo
 */
public record CustomerUpdate(
    String dni,
    String name,
    String email
) { }
