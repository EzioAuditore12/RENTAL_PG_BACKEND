package com.rental_pg_backend.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rental_pg_backend.auth.dto.BlackListRefreshTokenDto;
import com.rental_pg_backend.auth.entities.BlackListRefreshToken;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    BlackListRefreshToken insertBlackListedToken(BlackListRefreshTokenDto dto);
    
}
