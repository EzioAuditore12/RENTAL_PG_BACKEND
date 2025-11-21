package com.rental_pg_backend.auth.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListRefreshTokenDto {

    private String refreshToken;

    private Date iat;

    private Date exp;
}
