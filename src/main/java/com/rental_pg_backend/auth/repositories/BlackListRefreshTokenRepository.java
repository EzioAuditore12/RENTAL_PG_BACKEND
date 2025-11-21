package com.rental_pg_backend.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental_pg_backend.auth.entities.BlackListRefreshToken;

public interface BlackListRefreshTokenRepository extends JpaRepository<BlackListRefreshToken, Long> {
     
    Optional<BlackListRefreshToken> findByRefreshToken(String refreshToken);

}
