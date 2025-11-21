package com.rental_pg_backend.auth.services;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.auth.AuthMapper;
import com.rental_pg_backend.auth.dto.BlackListRefreshTokenDto;
import com.rental_pg_backend.auth.dto.RefershTokensDto;
import com.rental_pg_backend.auth.dto.TokensDto;
import com.rental_pg_backend.auth.dto.login.LoginManagerDto;
import com.rental_pg_backend.auth.dto.register.RegisterManagerDto;
import com.rental_pg_backend.auth.dto.responses.AuthManagerResponseDto;
import com.rental_pg_backend.auth.entities.BlackListRefreshToken;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.auth.enums.TokenType;
import com.rental_pg_backend.auth.repositories.BlackListRefreshTokenRepository;
import com.rental_pg_backend.manager.ManagerMapper;
import com.rental_pg_backend.manager.ManagerService;
import com.rental_pg_backend.manager.dto.ManagerDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthManagerService {

    private final AuthMapper authMapper;
    private final ManagerService managerService;
    private final ManagerMapper managerMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;
    private final BlackListRefreshTokenRepository blackListRefreshTokenRepository;
    private final CustomUserDetailService userDetailService;

    public AuthManagerResponseDto registerManager(RegisterManagerDto registerManagerDto) {
        ManagerDto existingManager = managerService.findByEmail(registerManagerDto.getEmail());

        if (!Objects.isNull(existingManager))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Manager with this email is already registered");

        String hashedPassword = passwordEncoder.encode(registerManagerDto.getPassword());

        registerManagerDto.setPassword(hashedPassword);

        ManagerDto createdManager = managerService.createAccount(registerManagerDto);

        TokensDto tokens = jwtService.generateTokens(createdManager.getId().toString(), RoleType.MANAGER);

        return AuthManagerResponseDto.builder().user(managerMapper.toPublicDto(createdManager)).tokens(tokens).build();
    }

    public AuthManagerResponseDto validateManager(LoginManagerDto loginManagerDto) {
        ManagerDto existingManager = managerService.findByEmail(loginManagerDto.getEmail());

        if (Objects.isNull(existingManager))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given user with this email does not exist");

        boolean isValidPassword = passwordEncoder.matches(loginManagerDto.getPassword(), existingManager.getPassword());

        if (!isValidPassword)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Either entered email or password is wrong");

        TokensDto tokens = jwtService.generateTokens(existingManager.getId().toString(), RoleType.MANAGER);

        return AuthManagerResponseDto.builder().user(managerMapper.toPublicDto(existingManager)).tokens(tokens).build();
    }

    public TokensDto regenerateTokens(RefershTokensDto refreshTokensDto, RoleType role) {

            String userId = jwtService.extractUserId(refreshTokensDto.getRefreshToken(), TokenType.REFRESH);

             UserDetails userDetails = userDetailService.loadByUserIdAndRole(UUID.fromString(userId), "MANAGER" );

            boolean isTokenValid = jwtService.isTokenValid(refreshTokensDto.getRefreshToken(), userDetails,
                    TokenType.REFRESH);


            if (!isTokenValid)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Given refresh token is either invalid or expired");

            if (isRefreshTokenBlackListed(refreshTokensDto.getRefreshToken()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Given refresh token is blacklisted");

            Date iat = jwtService.extractIssuedAt(refreshTokensDto.getRefreshToken(), TokenType.REFRESH);
            Date exp = jwtService.extractExpiration(refreshTokensDto.getRefreshToken(), TokenType.REFRESH);

            BlackListRefreshTokenDto blackListRefreshTokenDto = new BlackListRefreshTokenDto(
                    refreshTokensDto.getRefreshToken(), iat, exp);
            insertBlackListedToken(blackListRefreshTokenDto);

            return jwtService.generateTokens(userId, role);
    }

    private void insertBlackListedToken(BlackListRefreshTokenDto blackListRefreshTokenDto) {
        BlackListRefreshToken entity = authMapper.insertBlackListedToken(blackListRefreshTokenDto);
        blackListRefreshTokenRepository.save(entity);
    }

    private boolean isRefreshTokenBlackListed(String refreshToken) {
        return blackListRefreshTokenRepository.findByRefreshToken(refreshToken).isPresent();
    }

}
