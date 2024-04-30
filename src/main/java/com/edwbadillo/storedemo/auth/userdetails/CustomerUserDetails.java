package com.edwbadillo.storedemo.auth.userdetails;

import com.edwbadillo.storedemo.customer.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * The user details for customers.
 *
 * @author edwbadillo
 */
public class CustomerUserDetails implements UserDetails {

    @Getter
    private final Customer customer;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !customer.isDisabled();
    }

}
