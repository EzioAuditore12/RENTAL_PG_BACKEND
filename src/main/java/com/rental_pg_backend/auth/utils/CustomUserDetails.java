package com.rental_pg_backend.auth.utils;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rental_pg_backend.auth.dto.AuthenticatedUserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthenticatedUserDto authenticatedUserDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authenticatedUserDto.getRole().name()));
    }

    @Override
    public String getPassword() {
        return authenticatedUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return authenticatedUserDto.getId().toString();
    }

}
