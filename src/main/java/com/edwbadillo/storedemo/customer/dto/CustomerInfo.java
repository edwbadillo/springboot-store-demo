package com.edwbadillo.storedemo.customer.dto;

import java.time.LocalDateTime;

/**
 * Represents basic information about a customer.
 *
 * @author edwbadillo
 */
public record CustomerInfo(
    Integer id,
    String name,
    String email,
    LocalDateTime disabledAt
) { }
