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
import com.rental_pg_backend.auth.dto.login.LoginManagerDto;
import com.rental_pg_backend.auth.dto.register.RegisterManagerDto;
import com.rental_pg_backend.auth.dto.responses.AuthManagerResponseDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.auth.services.AuthManagerService;
import com.rental_pg_backend.common.dto.BaseResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth/manager")
@Tag(name = "Authentication Manager", description = "Endpoints for validating manager")
@RequiredArgsConstructor
public class AuthManagerController {

    private final AuthManagerService authManagerService;

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Register a new manager", description = "Creates a new manager account with the provided details.")
    public BaseResponseDto<AuthManagerResponseDto> register(
            @Valid @ModelAttribute RegisterManagerDto registerManagerDto) {
        AuthManagerResponseDto credentials = authManagerService.registerManager(registerManagerDto);

        return new BaseResponseDto<>(true, "Manager Registered Successully", credentials);
    }

    @PostMapping("login")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Operation(summary = "Login manager", description = "Validates tenant credentials and logs in the manager.")
    public BaseResponseDto<AuthManagerResponseDto> login(@Valid @RequestBody LoginManagerDto loginManagerDto) {
        AuthManagerResponseDto credentials = authManagerService.validateManager(loginManagerDto);

        return new BaseResponseDto<>(true, "User logged in successfully", credentials);
    }

    @PostMapping("refresh")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @Operation(summary = "Refresh Tokens Manager", description = "Validates refresh token and regenerates new access and refresh tokens")
    public TokensDto refreshTenantTokens(@RequestBody RefershTokensDto refreshTokensDto) {
        return authManagerService.regenerateTokens(refreshTokensDto, RoleType.MANAGER);
    }

}
