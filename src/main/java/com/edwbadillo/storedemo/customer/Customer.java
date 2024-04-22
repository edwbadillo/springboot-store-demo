package com.edwbadillo.storedemo.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The customer entity, a customer is a person who purchases products.
 *
 * @author edwbadillo
 */
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dni;
    private String name;
    private String email;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime disabledAt;
}
