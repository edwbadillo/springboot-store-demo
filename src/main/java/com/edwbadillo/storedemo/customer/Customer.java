package com.edwbadillo.storedemo.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    public boolean isDisabled() {
        return disabledAt != null;
    }
}
