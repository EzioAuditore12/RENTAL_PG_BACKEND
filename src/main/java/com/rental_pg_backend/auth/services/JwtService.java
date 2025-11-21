package com.rental_pg_backend.auth.services;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rental_pg_backend.auth.dto.TokensDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.auth.enums.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${refresh-jwt.secret}")
    private String refreshJwtSecretKey;

    @Value("${jwt.expiration-minutes}")
    private long jwtExpirationTime;

    @Value("${refresh-jwt.expiration-days}")
    private long refreshJwtExpirationTime;

    @PostConstruct
    public void init() {
        jwtExpirationTime = Duration.ofMinutes(jwtExpirationTime).toMillis();
        refreshJwtExpirationTime = Duration.ofDays(refreshJwtExpirationTime).toMillis();
    }

     private SecretKey generateKey(TokenType tokenType) {
        String secret;
        if (tokenType == TokenType.ACCESS) {
            secret = jwtSecretKey;
        } else {
            secret = refreshJwtSecretKey;
        }
        byte[] decode = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decode);
    }

    public String generateToken(String userId, TokenType tokenType, RoleType roleType) {
        long expirationTime;

        if (tokenType == TokenType.ACCESS) {
            expirationTime = jwtExpirationTime;
        } else {
            expirationTime = refreshJwtExpirationTime;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", tokenType.name());
        claims.put("role", roleType.name());

        return Jwts.builder()
                .claims().add(claims)
                .subject(userId).issuer("DCB")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and().signWith(generateKey(tokenType)).compact();
    }

    public TokensDto generateTokens(String subject, RoleType roleType) {
        String accessToken = generateAccessToken(subject,roleType);
        String refreshToken = generateRefreshToken(subject,roleType);

        return new TokensDto(accessToken, refreshToken);
    }

    public String generateAccessToken(String payload, RoleType roleType) {
        return generateToken(payload, TokenType.ACCESS, roleType);
    }

    public String generateRefreshToken(String payload,RoleType roleType) {
        return generateToken(payload, TokenType.REFRESH, roleType);
    }

    public String extractUserId(String token, TokenType tokenType) {
        return extractClaims(token, Claims::getSubject, tokenType);
    }

    public String extractRole(String token, TokenType tokenType) {
        return extractClaims(token, claims -> claims.get("role", String.class), tokenType);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver, TokenType tokenType) {
        Claims claims = extractClaims(token, tokenType);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token, TokenType tokenType) {
        return Jwts.parser().verifyWith(generateKey(tokenType)).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        final String userId = extractUserId(token, tokenType);

        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType));
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    public Date extractIssuedAt(String token, TokenType tokenType) {
        return extractClaims(token, Claims::getIssuedAt, tokenType);
    }

    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaims(token, Claims::getExpiration, tokenType);
    }

}
