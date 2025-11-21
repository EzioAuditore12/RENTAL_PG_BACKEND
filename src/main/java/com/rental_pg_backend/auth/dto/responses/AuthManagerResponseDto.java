package com.rental_pg_backend.auth.dto.responses;

import com.rental_pg_backend.auth.dto.TokensDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.manager.dto.ManagerPublicDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthManagerResponseDto {

    private ManagerPublicDto user;

    private TokensDto tokens;

    @Builder.Default
    private RoleType role = RoleType.MANAGER;

}
