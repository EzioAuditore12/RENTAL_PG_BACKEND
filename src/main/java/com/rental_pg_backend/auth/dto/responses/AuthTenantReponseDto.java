package com.rental_pg_backend.auth.dto.responses;

import com.rental_pg_backend.auth.dto.TokensDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.tenant.dto.TenantPublicDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTenantReponseDto {

    private TenantPublicDto user;

    private TokensDto tokens;

    @Builder.Default
    private RoleType role = RoleType.TENANT;

}
