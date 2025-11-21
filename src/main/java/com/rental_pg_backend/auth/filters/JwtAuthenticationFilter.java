package com.rental_pg_backend.auth.filters;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental_pg_backend.auth.enums.TokenType;
import com.rental_pg_backend.auth.services.CustomUserDetailService;
import com.rental_pg_backend.auth.services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userId = jwtService.extractUserId(jwt, TokenType.ACCESS);
            final String userRole = jwtService.extractRole(jwt, TokenType.ACCESS);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userId != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadByUserIdAndRole(UUID.fromString(userId), userRole);

                if (jwtService.isTokenValid(jwt, userDetails, TokenType.ACCESS)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleJwtException(response, "JWT token has expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException e) {
            handleJwtException(response, "Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleJwtException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", message,
                "error", "Unauthorized");

        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
