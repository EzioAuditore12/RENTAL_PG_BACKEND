package com.rental_pg_backend.tenant.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rental_pg_backend.tenant.dto.TenantIdPasswordDto;
import com.rental_pg_backend.tenant.entities.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    @Query("SELECT new com.rental_pg_backend.tenant.dto.TenantIdPasswordDto(t.id, t.password) FROM Tenant t WHERE t.id = :id")
    TenantIdPasswordDto findIdAndPasswordById(@Param("id") UUID id);

    Optional<Tenant> findByEmail(String email);

}
