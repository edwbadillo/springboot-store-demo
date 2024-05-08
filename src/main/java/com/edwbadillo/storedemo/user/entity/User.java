package com.edwbadillo.storedemo.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a user, may have roles to manage
 * some parts of the system.
 *
 * @author edwbadillo
 */
@Entity
@Table(name = "user_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dni;
    private String name;
    private String email;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime disabledAt;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public boolean isDisabled() {
        return disabledAt != null;
    }
}
