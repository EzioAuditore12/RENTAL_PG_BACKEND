package com.rental_pg_backend.auth.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rental_pg_backend.auth.dto.AuthenticatedUserDto;
import com.rental_pg_backend.auth.enums.RoleType;
import com.rental_pg_backend.auth.utils.CustomUserDetails;
import com.rental_pg_backend.manager.dto.ManagerIdPasswordDto;
import com.rental_pg_backend.manager.repositories.ManagerRepository;
import com.rental_pg_backend.tenant.dto.TenantIdPasswordDto;
import com.rental_pg_backend.tenant.repositories.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final TenantRepository tenantRepository;
    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Not supported. Use loadByUserIdAndRole(UUID, String).");
    }

    public UserDetails loadByUserIdAndRole(UUID userId, String role) {
        if ("TENANT".equals(role)) {

            TenantIdPasswordDto tenant = tenantRepository.findIdAndPasswordById(userId);

            if (Objects.isNull(tenant))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given tenant with this id is not registered");

            AuthenticatedUserDto authenticatedTenant = AuthenticatedUserDto.builder()
                    .id(tenant.getId())
                    .password(tenant.getPassword())
                    .role(RoleType.TENANT)
                    .build();

            return new CustomUserDetails(authenticatedTenant);

        } else if ("MANAGER".equals(role)) {

            ManagerIdPasswordDto manager = managerRepository.findIdAndPasswordById(userId);

            if (Objects.isNull(manager))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user found");

            AuthenticatedUserDto authenticatedManager = AuthenticatedUserDto.builder()
                    .id(manager.getId())
                    .password(manager.getPassword())
                    .role(RoleType.MANAGER)
                    .build();

            return new CustomUserDetails(authenticatedManager);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role type");
        }
    }
}
