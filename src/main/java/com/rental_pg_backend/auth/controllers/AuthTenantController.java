package com.rental_pg_backend.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rental_pg_backend.auth.dto.RefershTokensDto;
import com.rental_pg_backend.auth.dto.TokensDto;
import com.rental_pg_backend.auth.dto.login.LoginTenantDto;
import com.rental_pg_backend.auth.dto.register.RegisterTenantDto;
import com.rental_pg_backend.auth.dto.responses.AuthTenantReponseDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.auth.services.AuthTenantService;
import com.rental_pg_backend.common.dto.BaseResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth/tenant")
@Tag(name = "Authentication Tenant", description = "Endpoints for validating tenant")
@RequiredArgsConstructor
public class AuthTenantController {

    private final AuthTenantService authTenantService;

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Register a new Tenant", description = "Creates a new tenant account with the provided details.")
    public BaseResponseDto<AuthTenantReponseDto> register(
            @Valid @ModelAttribute RegisterTenantDto registerTenantDto) {
        AuthTenantReponseDto credentials = authTenantService.registerTenant(registerTenantDto);

        return new BaseResponseDto<>(true, "Tenant Registered Successully", credentials);
    }

    @PostMapping("login")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Operation(summary = "Login Tenant", description = "Validates tenant credentials and logs in the tenant.")
    public BaseResponseDto<AuthTenantReponseDto> login(@Valid @RequestBody LoginTenantDto loginTenantDto) {
        AuthTenantReponseDto credentials = authTenantService.validateTenant(loginTenantDto);

        return new BaseResponseDto<>(true, "User logged in successfully", credentials);
    }

    @PostMapping("refresh")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Operation(summary = "Refresh Tokens Tenant", description = "Validates refresh token and regenerates new access and refresh tokens")
    public TokensDto refreshTenantTokens(@RequestBody RefershTokensDto refreshTokensDto) {
        return authTenantService.regenerateTokens(refreshTokensDto, RoleType.TENANT);
    }

}
