package com.edwbadillo.storedemo.customer.dto;

import java.time.LocalDateTime;

/**
 * The status information for a customer.
 *
 * @author edwbadillo
 */
public record CustomerStatusInfo(
    Integer id,
    String name,
    boolean isActive,
    LocalDateTime disabledAt
) { }
