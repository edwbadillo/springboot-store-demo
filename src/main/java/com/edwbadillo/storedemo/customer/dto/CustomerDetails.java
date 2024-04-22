package com.edwbadillo.storedemo.customer.dto;

import java.time.LocalDateTime;

/**
 * Complete details about a customer.
 *
 * @author edwbadillo
 */
public record CustomerDetails(
    Integer id,
    String dni,
    String name,
    String email,
    LocalDateTime disabledAt
) { }
