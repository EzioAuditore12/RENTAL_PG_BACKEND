package com.rental_pg_backend.auth.dto;

import java.util.UUID;

import com.rental_pg_backend.auth.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUserDto {

    private UUID id;

    private String password;

    private RoleType role;

}
